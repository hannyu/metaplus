package com.outofstack.metaplus.common;


import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final DateTimeFormatter F1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    /**
     * format: "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
     *
     * @param zonedDateTime
     * @return
     */
    public static String format(ZonedDateTime zonedDateTime) {
        return F1.format(zonedDateTime);
    }

    public static ZonedDateTime parse(String formated) {
        return ZonedDateTime.parse(formated, F1);
    }

    public static long toEpochMilli(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toInstant().toEpochMilli();
    }

    public static ZonedDateTime fromEpochMilli(long epochMilli) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
    }

    public static String epochMilli2Formatted(long epochMilli) {
        return format(fromEpochMilli(epochMilli));
    }

    public static long formatted2EpochMilli(String formatted) {
        return toEpochMilli(parse(formatted));
    }

    public static String formatNow() {
        return F1.format(ZonedDateTime.now());
    }


    /**
     * try to parse time in several formats
     *
     * @param formated
     * @return
     */
    public static ZonedDateTime tryParse(String formated) {
        try {
            return ZonedDateTime.parse(formated, F1);
        } catch (RuntimeException e) {
        }

        try {
            return LocalDateTime.parse(formated, DateTimeFormatter.ISO_DATE_TIME).atZone(ZoneId.systemDefault());
        } catch (RuntimeException e) {
        }

        try {
            return ZonedDateTime.parse(formated, DateTimeFormatter.ISO_DATE_TIME);
        } catch (RuntimeException e) {
        }

        throw new IllegalArgumentException("Can not parse the date, format '" + formated + "'");
    }

    public static int toYmdInt(LocalDate localDate) {
        return localDate.getYear() * 10000 + localDate.getMonthValue() * 100 + localDate.getDayOfMonth();
    }
}
