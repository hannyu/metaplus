package com.outofstack.metaplus.common.sync;

import com.outofstack.metaplus.common.file.TextMmap;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSyncConsumer {

    private static final int READ_INTERVAL = 1000;

    private Path logpath;
    private Path pospath;
    private SyncProcessor processor;

    public FileSyncConsumer(Path logpath, Path pospath, SyncProcessor processor) {
//        if (!Files.isReadable(logpath)) throw new IllegalArgumentException("Can not read logpath '" + logpath + "'");
//        if (!Files.isWritable(pospath)) throw new IllegalArgumentException("Can not write pospath '" + pospath + "'");
        if (null == processor) throw new IllegalArgumentException("Processor can not be null");
        this.logpath = logpath;
        this.pospath = pospath;
        this.processor = processor;
    }

    public void start() throws IOException, InterruptedException {
        File logfile = logpath.toFile();
        RandomAccessFile raf = null;
        String lastDatetime = "";
        long lastReadPos = 0;
        long lastFileSize = 0;

        try {
            raf = waitNewRandomAccessFile(logfile);
            TextMmap mmap = new TextMmap(pospath, 50);
            String text = mmap.getText();
            if (!text.isEmpty()) {
                String[] ss = text.split(",");
                if (ss.length >= 2) {
                    lastDatetime = ss[0];
                    lastReadPos = Long.parseLong(ss[1]);
                    lastFileSize = lastReadPos;
                } else {
                    throw new IllegalArgumentException("Invalid format in file '" + pospath + "'");
                }
            }

            while (true) {
                long curFileSize = logfile.length();
                if (curFileSize < lastFileSize) {
                    // file is rotated
                    raf = waitNewRandomAccessFile(logfile);
                    lastReadPos = 0;
                    lastFileSize = 0;
                }

                if (curFileSize > lastFileSize) {
                    // need to read
                    raf.seek(lastReadPos);
                    String line = readUtf8Line(raf);
                    if (null != line) {
                        String sl[] = line.split(",", 2);
                        if (sl.length != 2) throw new RuntimeException("Read a unformatted line '" + line + "'");
                        String curDatetime = sl[0];
                        String json = sl[1];
                        if (curDatetime.compareTo(lastDatetime) >= 0) {
                            processor.doSync(json);
                        }
                        lastReadPos = raf.getFilePointer();
                        lastDatetime = curDatetime;
                        mmap.setText(lastDatetime + "," + lastReadPos);
                    }
                    lastFileSize = curFileSize;
                }

                Thread.sleep(READ_INTERVAL);
            }
        } finally {
            if (null != raf) {
                try {
                    raf.close();
                } catch (IOException e) {
                }

            }
        }
    }

    private static RandomAccessFile waitNewRandomAccessFile(File file) throws IOException, InterruptedException {
        while (!file.exists()) {
            Thread.sleep(READ_INTERVAL);
        }
        return new RandomAccessFile(file, "r");
    }



    private static final int READ_BUFFER_SIZE = 8192;

    private static String readUtf8Line(RandomAccessFile raf) throws IOException {
        long startReadPos = raf.getFilePointer();
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] readBytes = new byte[READ_BUFFER_SIZE];
        int readNum = 0;

        while ((readNum = raf.read(readBytes)) != -1) {
            for (int i = 0; i < readNum; i++) {
                if (readBytes[i] == '\n') {
                    byteBuffer.write(readBytes, 0, i);
                    raf.seek(raf.getFilePointer() - (readNum - i) + 1);
                    return byteBuffer.toString(StandardCharsets.UTF_8);
                }
            }
            byteBuffer.write(readBytes, 0, readNum);
        }

        if (byteBuffer.size() > 0) {
            // not fully line
            raf.seek(startReadPos);
        }
        return null;
    }

}
