package com.outofstack.metaplus.domain;

public class TableDomain {

    public static final String DOMAIN_TABLE = "table";
    public static final String DOMAIN_TABLE_COLUMN = "table_column";
    public static final String DOMAIN_TABLE_PARTITION = "table_partition";

    public static String packFqmn4Table(String catalogName, String dbName, String tableName) {
        return catalogName + "." + dbName + "." + tableName;
    }

    public static String packFqmn4TableColumn(String catalogName, String dbName, String tableName,
                                                 String columnName) {
        return packFqmn4Table(catalogName, dbName, tableName) + "." + columnName;
    }

    public static String packFqmn4TablePartition(String catalogName, String dbName, String tableName,
                                                 String partitionValue) {
        return packFqmn4Table(catalogName, dbName, tableName) + "(" + partitionValue + ")";
    }


}
