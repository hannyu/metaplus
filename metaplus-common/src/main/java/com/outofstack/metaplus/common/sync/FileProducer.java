package com.outofstack.metaplus.common.sync;

import com.outofstack.metaplus.common.TimeUtil;
import com.outofstack.metaplus.common.file.DailyRollingLogger;

import java.io.IOException;
import java.nio.file.Path;

public class FileProducer {

    private Path logpath;
    private DailyRollingLogger patchlog = null;

    public FileProducer(Path logpath) {
        this.logpath = logpath;
        System.out.println("Metaplus: Init patchlog '" + logpath + "'");

        try {
            this.patchlog = new DailyRollingLogger(logpath);
        } catch (IOException e) {
            System.err.println("Metaplus: Init patchlog fail. Error: " + e.toString());
        }
    }

    public void produce(String line) {
        if (null != patchlog && null != line) {
            try {
                patchlog.writeLine(TimeUtil.formatNow(), ",", line);
            } catch (IOException e) {
                System.err.println("Metaplus: Write patchlog fail. Error: " + e.toString());
            }
        }
    }
}
