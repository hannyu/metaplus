package com.outofstack.metaplus.common.file;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TextMmapTest {

    @Test
    public void testOne() throws IOException, InterruptedException {

        Path tmppath = Paths.get(System.getProperty("java.io.tmpdir"), "text.mmap");
        System.out.println("tmppath: " + tmppath);

        TextMmap textmmap = new TextMmap(tmppath, 100);
        for (int i=0; i<1000; i++) {
            textmmap.setText("id: " + i);
            Thread.sleep(10);
        }

        System.out.println("finally: " + textmmap.getText());
    }
}
