package com.outofstack.metaplus.common.sync;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class FileSyncConsumerTest {

    @Test
    public void testOne() throws IOException, InterruptedException {
        FileSyncConsumer consumer = new FileSyncConsumer(Paths.get("/tmp/metaplus/test.log"),
                Paths.get("/tmp/metaplus/test.log.pos"), (line) -> {
                    System.out.println("line: " + line);
                });
        consumer.start();
    }
}
