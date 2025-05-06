package com.outofstack.metaplus.domain;

public class TableDomain {

    public static final String DOMAIN_TABLE = "table";
    public static final String DOMAIN_TABLE_COLUMN = "table_column";
    public static final String DOMAIN_TABLE_PARTITION = "table_partition";

    public static String packTableFqmn(String catalogName, String dbName, String tableName) {
        return catalogName + "." + dbName + "." + tableName;
    }

    public static String packTableColumnFqmn(String catalogName, String dbName, String tableName,
                                             String columnName) {
        return packTableFqmn(catalogName, dbName, tableName) + "." + columnName;
    }

    public static String packTablePartitionFqmn(String catalogName, String dbName, String tableName,
                                                String partitionValue) {
        return packTableFqmn(catalogName, dbName, tableName) + "(" + partitionValue + ")";
    }


}
