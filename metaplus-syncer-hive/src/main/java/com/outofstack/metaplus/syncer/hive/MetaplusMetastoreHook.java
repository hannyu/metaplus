package com.outofstack.metaplus.syncer.hive;

import com.outofstack.metaplus.common.DateUtil;
import com.outofstack.metaplus.common.PropertyConfig;
import com.outofstack.metaplus.common.json.JsonArray;
import com.outofstack.metaplus.common.json.JsonDiff;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.common.model.MetaplusPatch;
import com.outofstack.metaplus.common.model.PatchMethod;
import com.outofstack.metaplus.common.sync.FileProducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.metastore.MetaStoreEventListener;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.StorageDescriptor;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.hive.metastore.events.AlterTableEvent;
import org.apache.hadoop.hive.metastore.events.ConfigChangeEvent;
import org.apache.hadoop.hive.metastore.events.CreateTableEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.List;


public class MetaplusMetastoreHook extends MetaStoreEventListener {

    public static final String DOMAIN_TABLE = "table";
    public static final String DOMAIN_TABLE_COLUMN = "table_column";

    private static final Logger log = LoggerFactory.getLogger(MetaplusMetastoreHook.class);
    private FileProducer producer;

    public MetaplusMetastoreHook(Configuration config) {
        super(config);

        String logname = PropertyConfig.get(PropertyConfig.KEY_SYNCER_METASTORE_PATCHLOG_NAME,
                PropertyConfig.DEFAULT_SYNCER_METASTORE_PATCHLOG_NAME);
        producer = new FileProducer(Paths.get(PropertyConfig.getSyncerDir(), logname));
    }

    private void produce(MetaplusPatch patch) {
        producer.produce(patch);
    }

    private JsonArray packColumns(List<FieldSchema> fields) {
        JsonArray columns = new JsonArray();
        for (FieldSchema fs : fields) {
            columns.add(fs.getName() + "," + fs.getType() + "," + fs.getComment());
        }
        return columns;
    }

    private String packPartitionKeys(List<FieldSchema> fields) {
        StringBuilder partitionKeys = new StringBuilder();
        boolean first = true;
        for (FieldSchema fs : fields) {
            if (first) {
                partitionKeys.append(fs.getName());
                first = false;
            } else {
                partitionKeys.append(",");
                partitionKeys.append(fs.getName());
            }
        }
        return partitionKeys.toString();
    }

    private MetaplusDoc packTableDoc(Table table) {
        String catalogName = table.getCatName();
        String dbName = table.getDbName();
        String tableName = table.getTableName();
        StorageDescriptor sd = table.getSd();

        // produce table
        MetaplusDoc doc = new MetaplusDoc(PropertyConfig.getFqmnCorp(), DOMAIN_TABLE,
                TableUtil.packTableFqmnName(catalogName, dbName, tableName));
        JsonObject meta = doc.getMeta();
        meta.put("catalogName", catalogName);
        meta.put("dbName", dbName);
        meta.put("tableName", tableName);
        meta.put("tableComment", table.getParameters().getOrDefault("comment", ""));
        meta.put("tableType", table.getTableType());
        meta.put("tableOwner", table.getOwner());
        meta.put("tableParams", new JsonObject(table.getParameters()));
        meta.put("columns", packColumns(sd.getCols()));
        meta.put("partitionKeys", packPartitionKeys(table.getPartitionKeys()));
        meta.put("sd", new JsonObject()
                .put("location", sd.getLocation())
                .put("inputFormat", sd.getInputFormat())
                .put("outputFormat", sd.getOutputFormat())
                .put("serdeLib", sd.getSerdeInfo().getSerializationLib())
                .put("compressed", sd.isCompressed()));
        return doc;
    }

    public MetaplusDoc packColumnDoc(Table table, FieldSchema fs) {
        MetaplusDoc colDoc = new MetaplusDoc(PropertyConfig.getFqmnCorp(), DOMAIN_TABLE_COLUMN,
                TableUtil.packTableFqmnName(table.getCatName(), table.getDbName(),
                        table.getTableName()) + "." + fs.getName());
        JsonObject colMeta = colDoc.getMeta();
        colMeta.put("catalogName", table.getCatName());
        colMeta.put("dbName", table.getDbName());
        colMeta.put("tableName", table.getTableName());
        colMeta.put("columnName", fs.getName());
        colMeta.put("columnType", fs.getType());
        colMeta.put("columnComment", fs.getComment());
        return colDoc;
    }

    @Override
    public void onCreateTable(CreateTableEvent tableEvent) throws MetaException {
        // table
        Table table = tableEvent.getTable();
        String createdAt = DateUtil.formatNow();
        String owner = table.getOwner();

        MetaplusDoc doc = packTableDoc(table);
        doc.setSyncCreatedBy(owner);
        doc.setSyncCreatedAt(createdAt);
        doc.setSyncCreatedFrom(PropertyConfig.getSyncerHostname());
        produce(new MetaplusPatch(PatchMethod.META_CREATE, doc));

        // column
        for (FieldSchema fs : table.getSd().getCols()) {
            MetaplusDoc colDoc = packColumnDoc(table, fs);
            colDoc.setSyncCreatedBy(owner);
            colDoc.setSyncCreatedAt(createdAt);
            colDoc.setSyncCreatedFrom(PropertyConfig.getSyncerHostname());
            produce(new MetaplusPatch(PatchMethod.META_CREATE, colDoc));
        }

    }


    @Override
    public void onAlterTable(AlterTableEvent tableEvent) throws MetaException {
        String updatedAt = DateUtil.formatNow();

        // table
        Table oldTable = tableEvent.getOldTable();
        Table newTable = tableEvent.getNewTable();
        MetaplusDoc oldDoc = packTableDoc(oldTable);
        MetaplusDoc newDoc = packTableDoc(newTable);
        List<String[]> diffs = JsonDiff.diff(oldDoc, newDoc);
        System.out.println(JsonObject.object2JsonString(diffs));
        if (!diffs.isEmpty()) {
            newDoc.setSyncUpdatedBy(newTable.getOwner());
            newDoc.setSyncUpdatedAt(updatedAt);
            newDoc.setSyncUpdatedFrom(PropertyConfig.getSyncerHostname());
            produce(new MetaplusPatch(PatchMethod.META_UPDATE, newDoc));
        }

        // column
        for (String[] it : diffs) {
            if (it[0].startsWith("$.columns[")) {
                int i = Integer.parseInt(it[0].substring(10, it[0].indexOf("]")));
                if (it[1].equals("+")) {
                    MetaplusDoc colDoc = packColumnDoc(newTable, newTable.getSd().getCols().get(i));
                    colDoc.setSyncUpdatedBy(newTable.getOwner());
                    colDoc.setSyncUpdatedAt(updatedAt);
                    colDoc.setSyncUpdatedFrom(PropertyConfig.getSyncerHostname());
                    produce(new MetaplusPatch(PatchMethod.META_CREATE, colDoc));
                } else if (it[1].equals("-")) {
                    // no way in hive
                    MetaplusDoc colDoc = packColumnDoc(newTable, oldTable.getSd().getCols().get(i));
                    colDoc.setSyncUpdatedBy(newTable.getOwner());
                    colDoc.setSyncUpdatedAt(updatedAt);
                    colDoc.setSyncUpdatedFrom(PropertyConfig.getSyncerHostname());
                    produce(new MetaplusPatch(PatchMethod.META_DELETE, colDoc));
                } else if (it[1].equals("!")) {
                    // no care
                    MetaplusDoc colDoc = packColumnDoc(newTable, newTable.getSd().getCols().get(i));
                    colDoc.setSyncUpdatedBy(newTable.getOwner());
                    colDoc.setSyncUpdatedAt(updatedAt);
                    colDoc.setSyncUpdatedFrom(PropertyConfig.getSyncerHostname());
                    produce(new MetaplusPatch(PatchMethod.META_UPDATE, colDoc));
                }
            }
        }
    }

    @Override
    public void onConfigChange(ConfigChangeEvent tableEvent) throws MetaException {
        // Nothing to do here.
    }



}
