package com.github.jvm.base;

import com.google.common.base.Stopwatch;

import java.util.function.Consumer;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/4/29.
 */
public class TimeTestUnit {
    protected void runTimes(int times, String tag, Consumer consumer) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < times; i++) {
            consumer.accept(null);
        }
        System.out.println(tag + ":" + stopwatch);
    }
}
