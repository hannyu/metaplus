package com.outofstack.metaplus.common.file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Simulating mmap to Implement File Mapping
 */
public class TextMmap {

    private final MappedByteBuffer mappedBuffer;
    private final byte sufPadding;
    private final int maxByteSize;

    public TextMmap(Path mmappath, int maxByteSize) throws IOException {
        this.maxByteSize = maxByteSize;
//        this.sufPadding = sufPadding;
        this.sufPadding = ' ';
        try (RandomAccessFile file = new RandomAccessFile(mmappath.toFile(), "rw")) {
            mappedBuffer = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, maxByteSize);
            if (file.length() == 0) {
                setText("");
            }
        }
    }

    public void setText(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > maxByteSize) {
            throw new IllegalArgumentException("Text exceeds maximum size of " + maxByteSize + " bytes");
        }
        mappedBuffer.position(0);
        mappedBuffer.put(bytes);

        // If the length of the text is less than maxByteSize, pad the remaining part with sufPadding.
        if (bytes.length < maxByteSize) {
            byte[] paddings = new byte[maxByteSize - bytes.length];
            Arrays.fill(paddings, sufPadding);
            mappedBuffer.put(paddings);
        }
    }

    public String getText() {
        byte[] bytes = new byte[maxByteSize];
        mappedBuffer.position(0);
        mappedBuffer.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8).trim();
    }

    public void force() {
        mappedBuffer.force();
    }

}
