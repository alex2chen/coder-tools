package com.github.common.date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author alex.chen
 * @Description
 * @date 2017/6/19
 */
public class Joda_time_unit {
    @Test
    public void parseDateTime() {
        //String to DateTime
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dateTime = DateTime.parse("2017-6-21 9:22:45", format);
        Assert.assertNotNull(dateTime);
        //Date to DateTime
        Date date = new Date();
        DateTime dateTime2 = new DateTime(date);
        Assert.assertNotNull(dateTime2);
        //Calendar to DateTime
        Calendar calendar = Calendar.getInstance();
        DateTime dateTime3 = new DateTime(calendar);
        Assert.assertNotNull(dateTime3);
    }

    @Test
    public void useDateTime() {
        DateTime dateTime = DateTime.now();
        Date date = dateTime.toDate();
        System.out.println(date);

        Calendar calendar = dateTime.toCalendar(Locale.CHINA);
        System.out.println(calendar);

        System.out.println(dateTime.toString("yyyy/MM/dd HH:mm:ss EE"));
        System.out.println(dateTime.toString("yyyy年MM月dd日 HH:mm:ss EE", Locale.CHINESE));
    }

}
