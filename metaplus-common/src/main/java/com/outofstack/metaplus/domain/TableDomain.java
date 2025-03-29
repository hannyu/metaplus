package com.outofstack.metaplus.domain;

public class TableDomain {

    public static final String DOMAIN_TABLE = "table";
    public static final String DOMAIN_TABLE_COLUMN = "table_column";
    public static final String DOMAIN_TABLE_PARTITION = "table_partition";

    public static String packFqmnName(String catalogName, String dbName, String tableName) {
        return catalogName + "." + dbName + "." + tableName;
    }


}
