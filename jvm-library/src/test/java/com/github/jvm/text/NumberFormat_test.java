package com.github.jvm.text;

import org.junit.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/4/11.
 */
public class NumberFormat_test {
    @Test
    public void doubleformat() {
        String format = "##.###";
        double value = 4.1345d;
        float value2 = 343545.00f;
        NumberFormat nbFormat = new DecimalFormat(format);
        System.out.println(nbFormat.format(value));//4.135
        System.out.println(nbFormat.format(value2));//343545

        System.out.println(BigDecimal.valueOf(value).setScale(2, BigDecimal.ROUND_HALF_DOWN));//4.13
        double fee = 5.647435d;
        System.out.println(BigDecimal.valueOf(fee).setScale(2, BigDecimal.ROUND_HALF_DOWN));//5.65
    }
}
