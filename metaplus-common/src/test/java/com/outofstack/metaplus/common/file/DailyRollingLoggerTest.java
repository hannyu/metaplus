package com.outofstack.metaplus.common.file;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DailyRollingLoggerTest {

    @Test
    public void testOne() throws IOException, InterruptedException {
        Path tmppath = Paths.get(System.getProperty("java.io.tmpdir"), "text.txt");
        System.out.println("tmppath: " + tmppath);

        DailyRollingLogger log = new DailyRollingLogger(tmppath);
        log.writeLine("1");
        log.writeLine("22");
        log.writeLine("我爱你～!");
        log.writeLine("yes my baby!");
        log.writeLine("时间到～ 放假了啊放假；打飞机的；啊分阿拉法金额啊；发啊");
    }
}
