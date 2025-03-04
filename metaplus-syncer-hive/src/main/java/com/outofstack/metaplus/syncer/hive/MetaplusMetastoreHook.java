package com.outofstack.metaplus.syncer.hive;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.metastore.MetaStoreEventListener;

public class MetaplusMetastoreHook extends MetaStoreEventListener {

    public MetaplusMetastoreHook(Configuration config) {
        super(config);
    }
}
