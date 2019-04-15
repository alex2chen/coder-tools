package com.github.common.date;

import org.junit.Test;
import org.springside.modules.utils.time.DateFormatUtil;

import java.text.ParseException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/4/15.
 */
public class DateFormatUtil_unit {
    @Test
    public void isoDateFormat() {
        Date date = new Date(116, 10, 1, 12, 23, 44);
        assertThat(DateFormatUtil.ISO_FORMAT.format(date)).contains("2016-11-01T12:23:44.000");
        assertThat(DateFormatUtil.ISO_ON_SECOND_FORMAT.format(date)).contains("2016-11-01T12:23:44");
        assertThat(DateFormatUtil.ISO_ON_DATE_FORMAT.format(date)).isEqualTo("2016-11-01");
    }

    @Test
    public void defaultDateFormat() {
        Date date = new Date(116, 10, 1, 12, 23, 44);
        assertThat(DateFormatUtil.DEFAULT_FORMAT.format(date)).isEqualTo("2016-11-01 12:23:44.000");
        assertThat(DateFormatUtil.DEFAULT_ON_SECOND_FORMAT.format(date)).isEqualTo("2016-11-01 12:23:44");
    }

    @Test
    public void formatWithPattern() {
        Date date = new Date(116, 10, 1, 12, 23, 44);
        assertThat(DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT, date))
                .isEqualTo("2016-11-01 12:23:44.000");
        assertThat(DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT, date.getTime()))
                .isEqualTo("2016-11-01 12:23:44.000");
    }

    @Test
    public void parseWithPattern() throws ParseException {
        Date date = new Date(116, 10, 1, 12, 23, 44);
        Date resultDate = DateFormatUtil.pareDate(DateFormatUtil.PATTERN_DEFAULT, "2016-11-01 12:23:44.000");
        assertThat(resultDate.getTime() == date.getTime()).isTrue();
    }

}
