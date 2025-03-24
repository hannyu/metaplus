package com.outofstack.metaplus.syncer.hive;

import com.outofstack.metaplus.common.DateUtil;
import com.outofstack.metaplus.common.PropertyConfig;
import com.outofstack.metaplus.common.json.JsonArray;
import com.outofstack.metaplus.common.json.JsonDiff;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.common.model.MetaplusPatch;
import com.outofstack.metaplus.common.model.PatchMethod;
import com.outofstack.metaplus.common.model.search.Query;
import com.outofstack.metaplus.common.sync.FileProducer;
import com.outofstack.metaplus.domain.TableDomain;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.metastore.MetaStoreEventListener;
import org.apache.hadoop.hive.metastore.api.*;
import org.apache.hadoop.hive.metastore.events.*;
import org.apache.hadoop.hive.metastore.tools.SQLGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;


public class MetaplusMetastoreHook extends MetaStoreEventListener {

    private static final Logger log = LoggerFactory.getLogger(MetaplusMetastoreHook.class);
    private FileProducer producer;

    public MetaplusMetastoreHook(Configuration config) {
        super(config);

        String logname = MetastoreUtil.getPatchlogName();
        producer = new FileProducer(Paths.get(PropertyConfig.getSyncerDir(), logname));
    }

    private void produce(MetaplusPatch patch) {
        producer.produce(patch);
    }

    @Override
    public void onCreateTable(CreateTableEvent tableEvent) throws MetaException {
        // create table
        Table table = tableEvent.getTable();
        String createdAt = DateUtil.formatNow();
//        String owner = table.getOwner();

        MetaplusDoc doc = MetastoreUtil.packTableDoc(table);
        doc.setSyncCreatedAt(createdAt);
        doc.setSyncCreatedFrom(PropertyConfig.getSyncerHostname());
        produce(new MetaplusPatch(PatchMethod.META_CREATE, doc));

        // create column
        for (FieldSchema fs : table.getSd().getCols()) {
            MetaplusDoc colDoc = MetastoreUtil.packColumnDoc(table, fs);
//            colDoc.setSyncCreatedBy(owner);
            colDoc.setSyncCreatedAt(createdAt);
            colDoc.setSyncCreatedFrom(PropertyConfig.getSyncerHostname());
            produce(new MetaplusPatch(PatchMethod.META_CREATE, colDoc));
        }
    }


    @Override
    public void onAlterTable(AlterTableEvent tableEvent) throws MetaException {
        String updatedAt = DateUtil.formatNow();

        /// update table, except tableParams
        Table oldTable = tableEvent.getOldTable();
        Table newTable = tableEvent.getNewTable();
        MetaplusDoc oldDoc = MetastoreUtil.packTableDoc(oldTable);
        MetaplusDoc newDoc = MetastoreUtil.packTableDoc(newTable);
        List<String[]> diffs = JsonDiff.diff(oldDoc, newDoc);
//        System.out.println(JsonObject.object2JsonString(diffs));

        long difcnt = diffs.stream().filter(dif -> !dif[0].startsWith("$.meta.tableParams.")).count();
        if (difcnt > 0) {
            newDoc.setSyncUpdatedAt(updatedAt);
            newDoc.setSyncUpdatedFrom(PropertyConfig.getSyncerHostname());
            produce(new MetaplusPatch(PatchMethod.META_UPDATE, newDoc));
        }

        /// rename tableName
        if (diffs.stream().anyMatch(dif -> dif[0].equals("$.meta.tableName"))) {
            // update all table_column
            MetaplusPatch patch1 = new MetaplusPatch(PatchMethod.PATCH_UPDATE, TableDomain.DOMAIN_TABLE_COLUMN);
            Query query1 = new Query();
            query1.setQuery(new JsonObject("term",
                    new JsonObject("meta.tableName", oldDoc.getMeta().getString("tableName"))));
            query1.setScript(new JsonObject()
                    .put("params", new JsonObject()
                            .put("meta", new JsonObject()
                                    .put("tableName", newDoc.getMeta().getString("tableName")))
                            .put("sync", new JsonObject()
                                    .put("updatedAt", updatedAt)
                                    .put("updatedFrom", PropertyConfig.getSyncerHostname()))));
            patch1.setPatch(query1);
            produce(patch1);

            // update all table_partition
            MetaplusPatch patch2 = new MetaplusPatch(PatchMethod.PATCH_UPDATE, TableDomain.DOMAIN_TABLE_COLUMN);
            Query query2 = new Query();
            query2.setQuery(new JsonObject("term",
                    new JsonObject("meta.tableName", oldDoc.getMeta().getString("tableName"))));
            query2.setScript(new JsonObject()
                    .put("params", new JsonObject()
                            .put("meta", new JsonObject()
                                    .put("tableName", newDoc.getMeta().getString("tableName")))
                            .put("sync", new JsonObject()
                                    .put("updatedAt", updatedAt)
                                    .put("updatedFrom", PropertyConfig.getSyncerHostname()))));
            patch2.setPatch(query2);
            produce(patch2);
        }

        /// update/add/delete column
        diffs.forEach(dif -> {
            if (dif[0].startsWith("$.meta.columns[")) {
                int i = Integer.parseInt(dif[0].substring("$.meta.columns[".length(), dif[0].indexOf("]")));
                if (dif[1].equals("+")) {
                    MetaplusDoc colDoc = MetastoreUtil.packColumnDoc(newTable, newTable.getSd().getCols().get(i));
                    colDoc.setSyncUpdatedAt(updatedAt);
                    colDoc.setSyncUpdatedFrom(PropertyConfig.getSyncerHostname());
                    produce(new MetaplusPatch(PatchMethod.META_CREATE, colDoc));
                } else if (dif[1].equals("-")) {
                    // no way in hive?
                    MetaplusDoc colDoc = MetastoreUtil.packColumnDoc(newTable, oldTable.getSd().getCols().get(i));
                    colDoc.setSyncUpdatedAt(updatedAt);
                    colDoc.setSyncUpdatedFrom(PropertyConfig.getSyncerHostname());
                    produce(new MetaplusPatch(PatchMethod.META_DELETE, colDoc));
                } else if (dif[1].equals("!")) {
                    // no way in hive?
                    MetaplusDoc colDoc = MetastoreUtil.packColumnDoc(newTable, newTable.getSd().getCols().get(i));
                    colDoc.setSyncUpdatedAt(updatedAt);
                    colDoc.setSyncUpdatedFrom(PropertyConfig.getSyncerHostname());
                    produce(new MetaplusPatch(PatchMethod.META_UPDATE, colDoc));
                }
            }
        });

        /// update/add/delete partition
        // See onAddPartition, onDropPartition, onAlterPartition
    }


    @Override
    public void onDropTable(DropTableEvent tableEvent) throws MetaException {
        String updatedAt = DateUtil.formatNow();
        /// delete table
        MetaplusDoc doc = MetastoreUtil.packTableDoc(tableEvent.getTable());
        doc.setSyncUpdatedAt(updatedAt);
        doc.setSyncUpdatedFrom(PropertyConfig.getSyncerHostname());

        MetaplusPatch patch1 = new MetaplusPatch(PatchMethod.META_DELETE, doc);
        produce(patch1);

        /// delete all table_column
        MetaplusPatch patch2 = new MetaplusPatch(PatchMethod.PATCH_DELETE, TableDomain.DOMAIN_TABLE_COLUMN);
        Query query2 = new Query();
        query2.setQuery(new JsonObject("term",
                new JsonObject("meta.tableName", doc.getMeta().getString("tableName"))));
        query2.setScript(new JsonObject("params", new JsonObject("sync", new JsonObject()
                .put("updatedAt", updatedAt)
                .put("updatedFrom", PropertyConfig.getSyncerHostname()))));
        patch2.setPatch(query2);
        produce(patch2);

        /// delete all table_partition
        MetaplusPatch patch3 = new MetaplusPatch(PatchMethod.PATCH_DELETE, TableDomain.DOMAIN_TABLE_PARTITION);
        Query query3 = new Query();
        query3.setQuery(new JsonObject("term",
                new JsonObject("meta.tableName", doc.getMeta().getString("tableName"))));
        query3.setScript(new JsonObject("params", new JsonObject("sync", new JsonObject()
                .put("updatedAt", updatedAt)
                .put("updatedFrom", PropertyConfig.getSyncerHostname()))));
        patch3.setPatch(query3);
        produce(patch3);
    }


    @Override
    public void onAddPartition(AddPartitionEvent partitionEvent) throws MetaException {
        String updatedAt = DateUtil.formatNow();
        // add many table_partition
        List<FieldSchema> partitionFields = partitionEvent.getTable().getPartitionKeys();
        Iterator<Partition> pits = partitionEvent.getPartitionIterator();
        while (pits.hasNext()) {
            Partition partition = pits.next();
            MetaplusDoc partDoc = MetastoreUtil.packPartitionDoc(partition, partitionFields);
            partDoc.setSyncUpdatedAt(updatedAt);
            partDoc.setSyncUpdatedFrom(PropertyConfig.getSyncerHostname());
            produce(new MetaplusPatch(PatchMethod.META_CREATE, partDoc));
        }
    }

    @Override
    public void onDropPartition(DropPartitionEvent partitionEvent) throws MetaException {
        String updatedAt = DateUtil.formatNow();
        // delete many table_partition
        List<FieldSchema> partitionFields = partitionEvent.getTable().getPartitionKeys();
        Iterator<Partition> pits = partitionEvent.getPartitionIterator();
        while (pits.hasNext()) {
            Partition partition = pits.next();
            MetaplusDoc partDoc = MetastoreUtil.packPartitionDoc(partition, partitionFields);
            partDoc.setSyncUpdatedAt(updatedAt);
            partDoc.setSyncUpdatedFrom(PropertyConfig.getSyncerHostname());
            produce(new MetaplusPatch(PatchMethod.META_DELETE, partDoc));
        }
    }

    /// ///////////////////////////////////
    /// NOTHING TO DO
    /// ///////////////////////////////////

    @Override
    public void onAlterPartition(AlterPartitionEvent partitionEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onCreateDatabase(CreateDatabaseEvent dbEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onAlterDatabase(AlterDatabaseEvent dbEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onDropDatabase(DropDatabaseEvent dbEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onCreateDataConnector(CreateDataConnectorEvent connectorEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onDropDataConnector(DropDataConnectorEvent connectorEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onAlterDataConnector(AlterDataConnectorEvent dcEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onLoadPartitionDone(LoadPartitionDoneEvent partSetDoneEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onCreateFunction(CreateFunctionEvent fnEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onDropFunction(DropFunctionEvent fnEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onInsert(InsertEvent insertEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onAddPrimaryKey(AddPrimaryKeyEvent addPrimaryKeyEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onAddForeignKey(AddForeignKeyEvent addForeignKeyEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onAddUniqueConstraint(AddUniqueConstraintEvent addUniqueConstraintEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onAddNotNullConstraint(AddNotNullConstraintEvent addNotNullConstraintEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onAddDefaultConstraint(AddDefaultConstraintEvent addDefaultConstraintEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onAddCheckConstraint(AddCheckConstraintEvent addCheckConstraintEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onDropConstraint(DropConstraintEvent dropConstraintEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onCreateISchema(CreateISchemaEvent createISchemaEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onAlterISchema(AlterISchemaEvent alterISchemaEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onDropISchema(DropISchemaEvent dropISchemaEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onAddSchemaVersion(AddSchemaVersionEvent addSchemaVersionEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onAlterSchemaVersion(AlterSchemaVersionEvent alterSchemaVersionEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onDropSchemaVersion(DropSchemaVersionEvent dropSchemaVersionEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onCreateCatalog(CreateCatalogEvent createCatalogEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onAlterCatalog(AlterCatalogEvent alterCatalogEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onDropCatalog(DropCatalogEvent dropCatalogEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onOpenTxn(OpenTxnEvent openTxnEvent, Connection dbConn, SQLGenerator sqlGenerator) throws MetaException {
        // do nothing
    }

    @Override
    public void onCommitTxn(CommitTxnEvent commitTxnEvent, Connection dbConn, SQLGenerator sqlGenerator) throws MetaException {
        // do nothing
    }

    @Override
    public void onAbortTxn(AbortTxnEvent abortTxnEvent, Connection dbConn, SQLGenerator sqlGenerator) throws MetaException {
        // do nothing
    }

    @Override
    public void onAllocWriteId(AllocWriteIdEvent allocWriteIdEvent, Connection dbConn, SQLGenerator sqlGenerator)
            throws MetaException {
        // do nothing
    }

    @Override
    public void onAcidWrite(AcidWriteEvent acidWriteEvent, Connection dbConn, SQLGenerator sqlGenerator) throws MetaException {
        // do nothing
    }

    @Override
    public void onBatchAcidWrite(BatchAcidWriteEvent batchAcidWriteEvent, Connection dbConn, SQLGenerator sqlGenerator)
            throws MetaException {
        // do nothing
    }

    @Override
    public void onUpdateTableColumnStat(UpdateTableColumnStatEvent updateTableColumnStatEvent) throws MetaException {
        // do nothing

    }

    @Override
    public void onDeleteTableColumnStat(DeleteTableColumnStatEvent deleteTableColumnStatEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onUpdatePartitionColumnStat(UpdatePartitionColumnStatEvent updatePartColStatEvent)
            throws MetaException {
        // do nothing
    }

    @Override
    public void onUpdatePartitionColumnStatInBatch(UpdatePartitionColumnStatEventBatch updatePartColStatEvent,
                                                   Connection dbConn, SQLGenerator sqlGenerator)
            throws MetaException {
        // do nothing
    }

    @Override
    public void onDeletePartitionColumnStat(DeletePartitionColumnStatEvent deletePartColStatEvent) throws MetaException {
        // do nothing
    }

    @Override
    public void onCommitCompaction(CommitCompactionEvent commitCompactionEvent, Connection dbConn,
                                   SQLGenerator sqlGenerator) throws MetaException {
        // do nothing
    }

    @Override
    public void onReload(ReloadEvent reloadEvent) throws MetaException {
        // do nothing
    }
}
