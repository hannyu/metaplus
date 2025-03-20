package com.outofstack.metaplus.syncer.hive;

public class TableUtil {

    public static String packTableFqmnName(String catalogName, String dbName, String tableName) {
        return (catalogName == null ? "" : catalogName) + "." + dbName + "." + tableName;
    }



}
