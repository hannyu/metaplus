package com.outofstack.metaplus.syncer.hive;

import com.outofstack.metaplus.common.file.DailyRollingLogger;
import com.outofstack.metaplus.common.json.JsonObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.metastore.MetaStoreEventListener;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.Partition;
import org.apache.hadoop.hive.metastore.events.*;
import org.apache.hadoop.hive.metastore.tools.SQLGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.util.Iterator;

public class PrintJsonHook extends MetaStoreEventListener {

    private static final Logger log = LoggerFactory.getLogger(PrintJsonHook.class);

    private DailyRollingLogger patchlog = null;
    public PrintJsonHook(Configuration config) {
        super(config);
        try {
            patchlog = new DailyRollingLogger("");
        } catch (IOException e) {
            log.error("Metaplus: Init patchlog fail.", e);
        }

    }

    private void printLog(String msg) {
//        System.out.println(msg);
        try {
            patchlog.writeLine(msg);
        } catch (IOException e) {
            log.error("Metaplus: Write line fail.", e);
        }

    }

    @Override
    public void onConfigChange(ConfigChangeEvent tableEvent) throws MetaException {
        printLog("onConfigChange key: " + tableEvent.getKey() + ", old:" + tableEvent.getOldValue() +
                ", new:" + tableEvent.getNewValue());
    }

    @Override
    public void onCreateTable(CreateTableEvent tableEvent) throws MetaException {
        printLog("onCreateTable Table: " + JsonObject.object2JsonString(tableEvent.getTable()));
    }

    @Override
    public void onAlterTable(AlterTableEvent tableEvent) throws MetaException {
        printLog("onAlterTable OldTable: " + JsonObject.object2JsonString(tableEvent.getOldTable()));
        printLog("onAlterTable NewTable: " + JsonObject.object2JsonString(tableEvent.getNewTable()));
    }

    @Override
    public void onDropTable(DropTableEvent tableEvent) throws MetaException {
        printLog("onDropTable Table: " + JsonObject.object2JsonString(tableEvent.getTable()));
    }

    @Override
    public void onAddPartition(AddPartitionEvent partitionEvent) throws MetaException {
        printLog("onAddPartition Table: " + JsonObject.object2JsonString(partitionEvent.getTable()));
        Iterator<Partition> itr = partitionEvent.getPartitionIterator();
        while (itr.hasNext()) {
            printLog("onAddPartition Partition: " + JsonObject.object2JsonString(itr.next()));
        }
        printLog("onAddPartition PartitionIterator: " + JsonObject.object2JsonString(itr));
    }

    @Override
    public void onDropPartition(DropPartitionEvent partitionEvent) throws MetaException {
        printLog("onDropPartition Table: " + JsonObject.object2JsonString(partitionEvent.getTable()));
        Iterator<Partition> itr = partitionEvent.getPartitionIterator();
        while (itr.hasNext()) {
            printLog("onDropPartition Partition: " + JsonObject.object2JsonString(itr.next()));
        }
        printLog("onDropPartition DeleteData: " + JsonObject.object2JsonString(partitionEvent.getDeleteData()));
    }

    @Override
    public void onAlterPartition(AlterPartitionEvent partitionEvent) throws MetaException {
        printLog("onAlterPartition Table: " + JsonObject.object2JsonString(partitionEvent.getTable()));
        printLog("onAlterPartition OldPart: " + JsonObject.object2JsonString(partitionEvent.getOldPartition()));
        printLog("onAlterPartition NewPart: " + JsonObject.object2JsonString(partitionEvent.getNewPartition()));
        printLog("onAlterPartition IsTruncateOp: " + JsonObject.object2JsonString(partitionEvent.getIsTruncateOp()));
    }

    @Override
    public void onCreateDatabase(CreateDatabaseEvent dbEvent) throws MetaException {
        printLog("onCreateDatabase Db: " + JsonObject.object2JsonString(dbEvent.getDatabase()));
    }

    @Override
    public void onAlterDatabase(AlterDatabaseEvent dbEvent) throws MetaException {
        printLog("onAlterDatabase OldDb: " + JsonObject.object2JsonString(dbEvent.getOldDatabase()));
        printLog("onAlterDatabase NewDb: " + JsonObject.object2JsonString(dbEvent.getNewDatabase()));
    }

    @Override
    public void onDropDatabase(DropDatabaseEvent dbEvent) throws MetaException {
        printLog("onDropDatabase Db: " + JsonObject.object2JsonString(dbEvent.getDatabase()));
    }

    @Override
    public void onCreateDataConnector(CreateDataConnectorEvent connectorEvent) throws MetaException {
        printLog("onCreateDataConnector DataConnector: " + JsonObject.object2JsonString(connectorEvent.getDataConnector()));
    }

    @Override
    public void onDropDataConnector(DropDataConnectorEvent connectorEvent) throws MetaException {
        printLog("onDropDataConnector DataConnector: " + JsonObject.object2JsonString(connectorEvent.getDataConnector()));
    }

    @Override
    public void onAlterDataConnector(AlterDataConnectorEvent dcEvent) throws MetaException {
        printLog("onDropDataConnector OldDataConnector: " + JsonObject.object2JsonString(dcEvent.getOldDataConnector()));
        printLog("onDropDataConnector NewDataConnector: " + JsonObject.object2JsonString(dcEvent.getNewDataConnector()));
    }

    @Override
    public void onLoadPartitionDone(LoadPartitionDoneEvent partSetDoneEvent) throws MetaException {
        printLog("onLoadPartitionDone Table: " + JsonObject.object2JsonString(partSetDoneEvent.getTable()));
        printLog("onLoadPartitionDone PartName: " + JsonObject.object2JsonString(partSetDoneEvent.getPartitionName()));
    }

    @Override
    public void onCreateFunction(CreateFunctionEvent fnEvent) throws MetaException {
        printLog("onCreateFunction Function: " + JsonObject.object2JsonString(fnEvent.getFunction()));
    }

    @Override
    public void onDropFunction(DropFunctionEvent fnEvent) throws MetaException {
        printLog("onDropFunction Function: " + JsonObject.object2JsonString(fnEvent.getFunction()));
    }

    @Override
    public void onInsert(InsertEvent insertEvent) throws MetaException {
        printLog("onInsert Table: " + JsonObject.object2JsonString(insertEvent.getTableObj()));
        printLog("onInsert Part: " + JsonObject.object2JsonString(insertEvent.getPartitionObj()));
        printLog("onInsert Files: " + JsonObject.object2JsonString(insertEvent.getFiles()));
    }

    @Override
    public void onAddPrimaryKey(AddPrimaryKeyEvent addPrimaryKeyEvent) throws MetaException {
        printLog("onAddPrimaryKey PrimaryKey: " + JsonObject.object2JsonString(addPrimaryKeyEvent.getPrimaryKeyCols()));
    }

    @Override
    public void onAddForeignKey(AddForeignKeyEvent addForeignKeyEvent) throws MetaException {
        printLog("onAddForeignKey ForeignKey: " + JsonObject.object2JsonString(addForeignKeyEvent.getForeignKeyCols()));
    }

    @Override
    public void onAddUniqueConstraint(AddUniqueConstraintEvent addUniqueConstraintEvent) throws MetaException {
        printLog("onAddUniqueConstraint UniqueConstraint: " +
                JsonObject.object2JsonString(addUniqueConstraintEvent.getUniqueConstraintCols()));
    }

    @Override
    public void onAddNotNullConstraint(AddNotNullConstraintEvent addNotNullConstraintEvent) throws MetaException {
        printLog("onAddNotNullConstraint NotNullConstraint: " +
                JsonObject.object2JsonString(addNotNullConstraintEvent.getNotNullConstraintCols()));
    }

    @Override
    public void onAddDefaultConstraint(AddDefaultConstraintEvent addDefaultConstraintEvent) throws MetaException {
        printLog("onAddDefaultConstraint DefaultConstraint: " +
                JsonObject.object2JsonString(addDefaultConstraintEvent.getDefaultConstraintCols()));
    }

    @Override
    public void onAddCheckConstraint(AddCheckConstraintEvent addCheckConstraintEvent) throws MetaException {
        printLog("onAddCheckConstraint CheckConstraint: " +
                JsonObject.object2JsonString(addCheckConstraintEvent.getCheckConstraintCols()));
    }

    @Override
    public void onDropConstraint(DropConstraintEvent dropConstraintEvent) throws MetaException {
        printLog("onDropConstraint ConstraintName: " + JsonObject.object2JsonString(dropConstraintEvent.getConstraintName()));
    }

    @Override
    public void onCreateISchema(CreateISchemaEvent createISchemaEvent) throws MetaException {
        printLog("onCreateISchema Schema: " + JsonObject.object2JsonString(createISchemaEvent.getSchema()));
    }

    @Override
    public void onAlterISchema(AlterISchemaEvent alterISchemaEvent) throws MetaException {
        printLog("onAlterISchema OldSchema: " + JsonObject.object2JsonString(alterISchemaEvent.getOldSchema()));
        printLog("onAlterISchema NewSchema: " + JsonObject.object2JsonString(alterISchemaEvent.getNewSchema()));
    }

    @Override
    public void onDropISchema(DropISchemaEvent dropISchemaEvent) throws MetaException {
        printLog("onDropISchema Schema: " + JsonObject.object2JsonString(dropISchemaEvent.getSchema()));
    }

    @Override
    public void onAddSchemaVersion(AddSchemaVersionEvent addSchemaVersionEvent) throws MetaException {
        printLog("onAddSchemaVersion SchemaVersion: " + JsonObject.object2JsonString(addSchemaVersionEvent.getSchemaVersion()));
    }

    @Override
    public void onAlterSchemaVersion(AlterSchemaVersionEvent alterSchemaVersionEvent) throws MetaException {
        printLog("onAlterSchemaVersion OldSchemaVersion: " +
                JsonObject.object2JsonString(alterSchemaVersionEvent.getOldSchemaVersion()));
        printLog("onAlterSchemaVersion NewSchemaVersion: " +
                JsonObject.object2JsonString(alterSchemaVersionEvent.getNewSchemaVersion()));
    }

    @Override
    public void onDropSchemaVersion(DropSchemaVersionEvent dropSchemaVersionEvent) throws MetaException {
        printLog("onDropSchemaVersion SchemaVersion: " + JsonObject.object2JsonString(dropSchemaVersionEvent.getSchemaVersion()));
    }

    @Override
    public void onCreateCatalog(CreateCatalogEvent createCatalogEvent) throws MetaException {
        printLog("onCreateCatalog Catalog: " + JsonObject.object2JsonString(createCatalogEvent.getCatalog()));
    }

    @Override
    public void onAlterCatalog(AlterCatalogEvent alterCatalogEvent) throws MetaException {
        printLog("onAlterCatalog OldCatalog: " + JsonObject.object2JsonString(alterCatalogEvent.getOldCatalog()));
        printLog("onAlterCatalog NewCatalog: " + JsonObject.object2JsonString(alterCatalogEvent.getNewCatalog()));
    }

    @Override
    public void onDropCatalog(DropCatalogEvent dropCatalogEvent) throws MetaException {
        printLog("onDropCatalog Catalog: " + JsonObject.object2JsonString(dropCatalogEvent.getCatalog()));
    }

    @Override
    public void onOpenTxn(OpenTxnEvent openTxnEvent, Connection dbConn, SQLGenerator sqlGenerator) throws MetaException {
        printLog("onOpenTxn TxnIds: " + JsonObject.object2JsonString(openTxnEvent.getTxnIds()));
        printLog("onOpenTxn TxnType: " + JsonObject.object2JsonString(openTxnEvent.getTxnType()));
    }

    @Override
    public void onCommitTxn(CommitTxnEvent commitTxnEvent, Connection dbConn, SQLGenerator sqlGenerator) throws MetaException {
        printLog("onCommitTxn TxnIds: " + JsonObject.object2JsonString(commitTxnEvent.getTxnId()));
        printLog("onCommitTxn TxnType: " + JsonObject.object2JsonString(commitTxnEvent.getTxnType()));
    }

    @Override
    public void onAbortTxn(AbortTxnEvent abortTxnEvent, Connection dbConn, SQLGenerator sqlGenerator) throws MetaException {
        printLog("onAbortTxn TxnIds: " + JsonObject.object2JsonString(abortTxnEvent.getTxnId()));
        printLog("onAbortTxn TxnType: " + JsonObject.object2JsonString(abortTxnEvent.getTxnType()));
    }

    @Override
    public void onAllocWriteId(AllocWriteIdEvent allocWriteIdEvent, Connection dbConn, SQLGenerator sqlGenerator) throws MetaException {
        printLog("onAllocWriteId DbName: " + JsonObject.object2JsonString(allocWriteIdEvent.getDbName()));
        printLog("onAllocWriteId TableName: " + JsonObject.object2JsonString(allocWriteIdEvent.getTableName()));
        printLog("onAllocWriteId TxnToWriteIdList: " + JsonObject.object2JsonString(allocWriteIdEvent.getTxnToWriteIdList()));
    }

    @Override
    public void onAcidWrite(AcidWriteEvent acidWriteEvent, Connection dbConn, SQLGenerator sqlGenerator) throws MetaException {
        printLog("onAcidWrite Table: " + JsonObject.object2JsonString(acidWriteEvent.getTableObj()));
        printLog("onAcidWrite Partition: " + JsonObject.object2JsonString(acidWriteEvent.getPartitionObj()));
        printLog("onAcidWrite TxnId: " + JsonObject.object2JsonString(acidWriteEvent.getTxnId()));
    }

    @Override
    public void onBatchAcidWrite(BatchAcidWriteEvent batchAcidWriteEvent, Connection dbConn, SQLGenerator sqlGenerator) throws MetaException {
        printLog("onBatchAcidWrite NumRequest: " + JsonObject.object2JsonString(batchAcidWriteEvent.getNumRequest()));
    }

    @Override
    public void onUpdateTableColumnStat(UpdateTableColumnStatEvent updateTableColumnStatEvent) throws MetaException {
        printLog("onUpdateTableColumnStat Table: " + JsonObject.object2JsonString(updateTableColumnStatEvent.getTableObj()));
        printLog("onUpdateTableColumnStat ColStats: " + JsonObject.object2JsonString(updateTableColumnStatEvent.getColStats()));
        printLog("onUpdateTableColumnStat TableParameters: " +
                JsonObject.object2JsonString(updateTableColumnStatEvent.getTableParameters()));

    }

    @Override
    public void onDeleteTableColumnStat(DeleteTableColumnStatEvent deleteTableColumnStatEvent) throws MetaException {
        printLog("onDeleteTableColumnStat Catalog: " + deleteTableColumnStatEvent.getCatName() +
                ", Db: " + deleteTableColumnStatEvent.getDBName() +
                ", Table: " + deleteTableColumnStatEvent.getTableName() +
                ", Col: " + deleteTableColumnStatEvent.getColName());
    }

    @Override
    public void onUpdatePartitionColumnStat(UpdatePartitionColumnStatEvent updatePartColStatEvent) throws MetaException {
        printLog("onUpdatePartitionColumnStat Table: " + JsonObject.object2JsonString(updatePartColStatEvent.getTableObj()));
        printLog("onUpdatePartitionColumnStat ColStats: " + JsonObject.object2JsonString(updatePartColStatEvent.getPartColStats()));
    }

    @Override
    public void onUpdatePartitionColumnStatInBatch(UpdatePartitionColumnStatEventBatch updatePartColStatEvent, Connection dbConn, SQLGenerator sqlGenerator) throws MetaException {
        printLog("onUpdatePartitionColumnStatInBatch NumEntries: " + JsonObject.object2JsonString(updatePartColStatEvent.getNumEntries()));
    }

    @Override
    public void onDeletePartitionColumnStat(DeletePartitionColumnStatEvent deletePartColStatEvent) throws MetaException {
        printLog("onDeletePartitionColumnStat Catalog: " + deletePartColStatEvent.getCatName() +
                ", Db: " + deletePartColStatEvent.getDBName() +
                ", Table: " + deletePartColStatEvent.getTableName() +
                ", Col: " + deletePartColStatEvent.getColName());
    }

    @Override
    public void onCommitCompaction(CommitCompactionEvent commitCompactionEvent, Connection dbConn, SQLGenerator sqlGenerator) throws MetaException {
        printLog("onCommitCompaction Db: " + JsonObject.object2JsonString(commitCompactionEvent.getDbname()));
        printLog("onCommitCompaction Table: " + JsonObject.object2JsonString(commitCompactionEvent.getTableName()));
        printLog("onCommitCompaction Part: " + JsonObject.object2JsonString(commitCompactionEvent.getPartName()));
    }

    @Override
    public void onReload(ReloadEvent reloadEvent) throws MetaException {
        printLog("onReload Table: " + JsonObject.object2JsonString(reloadEvent.getTableObj()));
        printLog("onReload Part: " + JsonObject.object2JsonString(reloadEvent.getPartitionObj()));
    }

}
