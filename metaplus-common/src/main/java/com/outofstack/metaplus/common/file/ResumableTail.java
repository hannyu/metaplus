package com.outofstack.metaplus.common.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Function;

public class ResumableTail {

    private Path logpath;
    private Path pospath;

    public ResumableTail(Path logpath, Path pospath) {
        if (!Files.isReadable(logpath)) throw new IllegalArgumentException("Can not read logpath '" + logpath + "'");
        if (!Files.isWritable(pospath)) throw new IllegalArgumentException("Can not write pospath '" + pospath + "'");

        this.logpath = logpath;
        this.pospath = pospath;
    }

    public void start(Consumer<String> consumer) throws IOException {
        File logfile = logpath.toFile();
        try (RandomAccessFile raf = new RandomAccessFile(logfile, "r")) {

        }
    }

}
