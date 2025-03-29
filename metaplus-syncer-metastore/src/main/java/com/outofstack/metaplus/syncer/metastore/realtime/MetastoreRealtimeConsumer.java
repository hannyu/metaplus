package com.outofstack.metaplus.syncer.metastore.realtime;

import com.outofstack.metaplus.common.PropertyConfig;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.MetaplusPatch;
import com.outofstack.metaplus.common.sync.FileConsumer;
import com.outofstack.metaplus.syncer.metastore.MetastoreUtil;

import java.io.IOException;
import java.nio.file.Paths;

public class MetastoreRealtimeConsumer {

    public static void main(String[] args) throws IOException, InterruptedException {

        String logname = MetastoreUtil.getPatchlogName();
        FileConsumer consumer = new FileConsumer(Paths.get(PropertyConfig.getSyncerDir(), logname));

        consumer.start((line) -> {
            System.out.println("line: " + line);
            MetaplusPatch patch = new MetaplusPatch(JsonObject.parse(line));

        });
    }
}
