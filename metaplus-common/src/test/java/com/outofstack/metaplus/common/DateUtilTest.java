package com.outofstack.metaplus.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

class DateUtilTest {

    @Test
    public void testOne() {

        ZonedDateTime now = ZonedDateTime.now();
        String s1 = DateUtil.format(now);
        String s2 = DateUtil.format(DateUtil.parse(s1));
        System.out.println(s2);
        assertEquals(s2, s1);

        String s3 = "2025-01-01T17:41:38.071+0800";
        ZonedDateTime t1 = DateUtil.parse(s3);
        assertEquals("+08:00", t1.getZone().getId());
        assertEquals(1735724498071L, t1.toInstant().toEpochMilli());

        String s4 = DateUtil.epochMilli2Formatted(1735724498071L);
        assertEquals(s3, s4);

        long ts3 = DateUtil.formatted2EpochMilli(s3);
        assertEquals(1735724498071L, ts3);

        assertThrows(RuntimeException.class, () -> {
            ZonedDateTime t2 = DateUtil.parse("sha ye bu shi");
        });

    }

    @Test
    public void testTwo() {

        ZonedDateTime t1 = ZonedDateTime.now();
        System.out.println("ISO_DATE_TIME: " + t1.format(DateTimeFormatter.ISO_DATE_TIME));
        System.out.println("ISO_ZONED_DATE_TIME: " + t1.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        System.out.println("ISO_DATE: " + t1.format(DateTimeFormatter.ISO_DATE));
        System.out.println("BASIC_ISO_DATE: " + t1.format(DateTimeFormatter.BASIC_ISO_DATE));
        System.out.println("ISO_INSTANT: " + t1.format(DateTimeFormatter.ISO_INSTANT));
        System.out.println("ISO_OFFSET_DATE_TIME: " + t1.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        System.out.println("ISO_LOCAL_DATE_TIME: " + t1.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

//        t1 = ZonedDateTime.parse("2025-01-02T12:49:52", DateTimeFormatter.ISO_DATE_TIME);
//        System.out.println(t1);


        LocalDateTime t2 = LocalDateTime.now();
        System.out.println("ISO_DATE_TIME: " + t2.format(DateTimeFormatter.ISO_DATE_TIME));
        System.out.println("ISO_LOCAL_DATE_TIME: " + t2.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        t2 = LocalDateTime.parse("2025-01-02T12:49:52", DateTimeFormatter.ISO_DATE_TIME);
        System.out.println(t2);
    }
}
