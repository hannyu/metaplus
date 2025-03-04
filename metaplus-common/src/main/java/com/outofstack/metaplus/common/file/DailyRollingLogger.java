package com.outofstack.metaplus.common.file;

import com.outofstack.metaplus.common.DateUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * test.log -> test.log.20250302
 *
 * Not thread safe
 */
public class DailyRollingLogger {

    private static final int WRITE_BUFFER_SIZE = 8192;

    private volatile BufferedWriter buffer;
    private int lastWriteYmd;
    private Path logpath;

    public DailyRollingLogger(String filename) throws IOException {
        this(Paths.get(filename));
    }
    public DailyRollingLogger(Path logpath) throws IOException {
        this.logpath = logpath;
        if (Files.exists(logpath)) {
            long lastModified = Files.getLastModifiedTime(logpath).toMillis();
            Instant instant = Instant.ofEpochMilli(lastModified);
            lastWriteYmd = DateUtil.toYmdInt(LocalDate.ofInstant(instant, ZoneId.systemDefault()));
        } else {
            lastWriteYmd = DateUtil.toYmdInt(LocalDate.now());
        }
        buffer = Files.newBufferedWriter(logpath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                close();
            } catch (Throwable e) {
                // e.printStackTrace();
            }
        }) );
    }

    public void close() throws IOException {
        if (buffer != null) {
            buffer.close();
        }
    }

    public void writeLine(String line) throws IOException {
        checkAndBackup();
        buffer.write(line);
        buffer.newLine();
        buffer.flush();
    }

    public void checkAndBackup() throws IOException {
        int nowWriteYmd = DateUtil.toYmdInt(LocalDate.now());
        if (lastWriteYmd != nowWriteYmd) {
            close();
            Path backuppath = logpath.getParent().resolve(logpath.getFileName().toString() + "." + lastWriteYmd);
            Files.move(logpath, backuppath, StandardCopyOption.REPLACE_EXISTING);

            lastWriteYmd = nowWriteYmd;
            buffer = Files.newBufferedWriter(logpath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
    }

}
