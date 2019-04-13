package com.github.jvm.time;

import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

/**
 * @author alex.chen
 * @Description: Java 8新的Date-Time API (JSR 310)受Joda-Time的影响，提供了新的java.time包
 * @date 2017/6/19
 */
public class Java8_date_test {
    //DateFormat中format()和parse()方法非线程安全
    @Test
    public void noSafeUse() {
        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        System.out.println(format.format(new Date()));
    }

    @Test
    public void clock() {
        Clock clock = Clock.systemDefaultZone();
        long millis = clock.millis();
        System.out.println(millis);
        Instant instant = clock.instant();
        Date legacy = Date.from(instant);
        System.out.println(legacy.getTime());
        System.out.println(ZoneId.systemDefault());
        System.out.println(ZoneId.getAvailableZoneIds());
        ZoneId zone1 = ZoneId.of("Brazil/East");
        System.out.println(zone1.getRules());
    }

    @Test
    public void localTime() {
        LocalTime now1 = LocalTime.now();
        LocalTime now2 = LocalTime.now(Clock.systemDefaultZone());
        System.out.println(now1.toString());
        System.out.println(now1.isBefore(now2));
        System.out.println(ChronoUnit.MINUTES.between(now1, now2));

        LocalTime late = LocalTime.of(23, 59, 59);
        System.out.println(late);

        DateTimeFormatter gernanFormater = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.GERMAN);
        LocalTime leetTime = LocalTime.parse("13:37", gernanFormater);
        System.out.println(leetTime);
    }

    @Test
    public void localDate() {
        LocalDate today = LocalDate.now();
        System.out.println(today);
        LocalDate independenceDay = LocalDate.of(2015, Month.APRIL, 4);
        System.out.println(independenceDay.getDayOfWeek());
    }

    @Test
    public void localDateTime() {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());
        System.out.println(LocalDateTime.of(2015, 12, 29, 19, 41, 59));
        System.out.println(LocalDateTime.parse("2017-06-19 10:59:13", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void localDateParseDate() {
        LocalDate localDate = LocalDate.now();
        Date date = parseDate(localDate, ZoneId.systemDefault());
        System.out.println(date);
        Assert.assertNotNull(date);
    }

    @Test
    public void dateParseDateTime() {
        Date input = new Date();
        LocalDateTime localDateTime = parseDateTime(input, ZoneId.systemDefault());
        System.out.println(localDateTime);
        Assert.assertNotNull(localDateTime);
    }

    @Test
    public void dateParseLocalDate() {
        Date input = new Date();
        LocalDate date = parseLocalDate(input, ZoneId.systemDefault());
        System.out.println(date);
        Assert.assertNotNull(date);
    }

    private LocalDateTime parseDateTime(Date date, ZoneId zone) {
        if (date == null)
            return null;
        if (date instanceof java.sql.Timestamp)
            return ((java.sql.Timestamp) date).toLocalDateTime();
        else
            return Instant.ofEpochMilli(date.getTime()).atZone(zone).toLocalDateTime();
    }

    public LocalDate parseLocalDate(Date date, ZoneId zone) {
        if (date == null)
            return null;
        if (date instanceof java.sql.Date)
            return ((java.sql.Date) date).toLocalDate();
        else
            return date.toInstant().atZone(zone).toLocalDate();
//            return Instant.ofEpochMilli(date.getTime()).atZone(zone).toLocalDate();
    }

    /**
     * @param date supports:<ul><li>{@link java.util.Date}
     *             <li>{@link java.sql.Date}
     *             <li>{@link java.sql.Timestamp}
     *             <li>{@link java.time.LocalDate}
     *             <li>{@link java.time.LocalDateTime}
     *             <li>{@link java.time.ZonedDateTime}
     *             <li>{@link java.time.Instant}
     * @param zone used only if the input object is LocalDate or LocalDateTime.
     * @return
     */
    private Date parseDate(Object date, ZoneId zone) {
        if (date == null)
            return null;
        if (date instanceof java.sql.Date || date instanceof java.sql.Timestamp)
            return new Date(((Date) date).getTime());
        if (date instanceof Date)
            return (Date) date;
        if (date instanceof LocalDate)
            return Date.from(((LocalDate) date).atStartOfDay(zone).toInstant());
        if (date instanceof LocalDateTime)
            return Date.from(((LocalDateTime) date).atZone(zone).toInstant());
        if (date instanceof ZonedDateTime)
            return Date.from(((ZonedDateTime) date).toInstant());
        if (date instanceof Instant)
            return Date.from((Instant) date);
        throw new UnsupportedOperationException("Don't know hot to convert " + date.getClass().getName() + " to java.util.Date");
    }
}
