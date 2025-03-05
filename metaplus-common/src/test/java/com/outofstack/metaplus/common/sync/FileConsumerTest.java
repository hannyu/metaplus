package com.outofstack.metaplus.common.sync;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class FileConsumerTest {

    @Test
    public void testOne() throws IOException, InterruptedException {
        FileConsumer consumer = new FileConsumer(Paths.get("/tmp/metaplus/test.log"),
                Paths.get("/tmp/metaplus/test.log.pos"));
        consumer.start((line) -> {
            System.out.println("line: " + line);
        });
    }
}
