package com.outofstack.metaplus.syncer.metastore;

import com.outofstack.metaplus.common.PropertyConfig;
import com.outofstack.metaplus.common.json.JsonArray;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.common.model.search.Hits;
import com.outofstack.metaplus.domain.TableDomain;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.Partition;
import org.apache.hadoop.hive.metastore.api.StorageDescriptor;
import org.apache.hadoop.hive.metastore.api.Table;

import java.util.ArrayList;
import java.util.List;

public class MetastoreUtil {

    /// patchlogName
    public static final String KEY_SYNCER_METASTORE_PATCHLOG_NAME = "metaplus.syncer.metastore_patchlog_name";
    public static final String DEFAULT_SYNCER_METASTORE_PATCHLOG_NAME = "metaplus-metastore.patchlog";

    private static String patchlogName = null;
    public static String getPatchlogName() {
        if (null == patchlogName) {
            patchlogName = PropertyConfig.get(KEY_SYNCER_METASTORE_PATCHLOG_NAME,
                    DEFAULT_SYNCER_METASTORE_PATCHLOG_NAME);
        }
        return patchlogName;
    }


    /// max supported partitions.
    /// if exceeded, partition information will no longer be synchronized.
    public static final String KEY_SYNCER_MAX_SUPPORTED_PARTITIONS = "metaplus.syncer.max_supported_partitions";
    public static final int DEFAULT_SYNCER_MAX_SUPPORTED_PARTITIONS = 10_000;

    private static int maxSupportedPartitions = 0;
    public static int getMaxSupportedPartitions() {
        if (0 == maxSupportedPartitions) {
            String value = PropertyConfig.get(KEY_SYNCER_MAX_SUPPORTED_PARTITIONS);
            if (null != value && !value.isEmpty()) {
                try {
                    maxSupportedPartitions = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    // print warn log
                }
            }
        }
        if (0 == maxSupportedPartitions) {
            maxSupportedPartitions = DEFAULT_SYNCER_MAX_SUPPORTED_PARTITIONS;
        }
        return maxSupportedPartitions;
    }



    public static JsonArray packColumns(List<FieldSchema> fields) {
        JsonArray columns = new JsonArray();
        for (FieldSchema fs : fields) {
            columns.add(new JsonObject()
                    .put("name", fs.getName())
                    .put("type", fs.getType())
                    .put("comment", fs.getComment() == null ? "" : fs.getComment()));
        }
        return columns;
    }

    public static String packPartitionKeys(List<FieldSchema> partitionFields) {
        StringBuilder partitionKeys = new StringBuilder();
        boolean first = true;
        for (FieldSchema fs : partitionFields) {
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


    public static String packPartitionValues(Partition partition, List<FieldSchema> partitionFields) {
        List<String> partitionValues = partition.getValues();
        if (partitionValues.size() != partitionFields.size() ) {
            /// FIXME: how
            System.out.println("WHY? partitionValues.size()=" + partitionValues.size() +
                    ", partitionFields.size()=" + partitionFields.size());
            return "";
        }

        StringBuilder partitionValueStr = new StringBuilder();
        boolean first = true;
        for (int i=0; i < partitionFields.size(); i++) {
            if (first) {
                partitionValueStr.append(partitionFields.get(i).getName()).append("=").append(partitionValues.get(i));
                first = false;
            } else {
                partitionValueStr.append("/");
                partitionValueStr.append(partitionFields.get(i).getName()).append("=").append(partitionValues.get(i));
            }
        }
        return partitionValueStr.toString();
    }

    public static MetaplusDoc packTableDoc(Table table) {
        String catalogName = table.getCatName();
        String dbName = table.getDbName();
        String tableName = table.getTableName();
        StorageDescriptor sd = table.getSd();

        // produce table
        MetaplusDoc doc = new MetaplusDoc(PropertyConfig.getFqmnCorp(), TableDomain.DOMAIN_TABLE,
                TableDomain.packTableFqmn(catalogName, dbName, tableName));
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

    public static MetaplusDoc packColumnDoc(Table table, FieldSchema fs) {
        MetaplusDoc colDoc = new MetaplusDoc(PropertyConfig.getFqmnCorp(), TableDomain.DOMAIN_TABLE_COLUMN,
                TableDomain.packTableFqmn(table.getCatName(), table.getDbName(),
                        table.getTableName()) + "." + fs.getName());
        JsonObject colMeta = colDoc.getMeta();
        colMeta.put("catalogName", table.getCatName());
        colMeta.put("dbName", table.getDbName());
        colMeta.put("tableName", table.getTableName());
        colMeta.put("columnName", fs.getName());
        colMeta.put("columnType", fs.getType());
        colMeta.put("columnComment", fs.getComment() == null ? "" : fs.getComment());
        return colDoc;
    }

    public static MetaplusDoc packPartitionDoc(Partition partition, List<FieldSchema> partitionFields) {
        String partValues = packPartitionValues(partition, partitionFields);
        return packPartitionDoc(partition.getCatName(), partition.getDbName(), partition.getTableName(), partValues);
    }

    public static MetaplusDoc packPartitionDoc(String catalogName, String dbName, String tableName,
                                               String partitionValues) {
        MetaplusDoc partDoc = new MetaplusDoc(PropertyConfig.getFqmnCorp(), TableDomain.DOMAIN_TABLE_PARTITION,
                TableDomain.packTablePartitionFqmn(catalogName, dbName, tableName, partitionValues));
        JsonObject partMeta = partDoc.getMeta();
        partMeta.put("catalogName", catalogName);
        partMeta.put("dbName", dbName);
        partMeta.put("tableName", tableName);
        partMeta.put("partitionValues", partitionValues);
        return partDoc;
    }

    public static List<String> unpackPartitionsFromHits(Hits hits) {
        int size = hits.getHitsSize();
        List<String> partitions = new ArrayList<>(size);
        for (int i=0; i < size; i++) {
            JsonObject it = hits.getHitsItem(i);
            partitions.add(it.getStringByPath("$._source.meta.partitionValues"));
        }
        return partitions;
    }
}
