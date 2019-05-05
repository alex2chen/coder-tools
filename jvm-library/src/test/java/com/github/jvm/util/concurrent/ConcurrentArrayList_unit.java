package com.github.jvm.util.concurrent;

import com.google.common.base.Stopwatch;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * @Author: alex
 * @Description: 数组ArrayList虽然insert和get是最快的，but非线程安全的，CopyOnWriteArrayList虽安全，但速度慢
 * @Date: created in 2018/4/29.
 */
public class ConcurrentArrayList_unit {
    private static final ArrayList<Boolean> arrayList = new ArrayList<>();
    private static final CopyOnWriteArrayList<Boolean> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
    private static final ConcurrentArrayList<Boolean> concurrentArrayList = new ConcurrentArrayList<>();

    /**
     * arrayListConsumer:3.679 ms
     * concurrentArrayList:5.446 ms
     * copyOnWriteArrayList:1.286 s
     * @throws InterruptedException
     */
    @Test
    public void setGet() throws InterruptedException {
        int maxConcurrent = 5;
        int maxSet=10000;
        int maxGet=0;
        Consumer arrayListConsumer = x -> {
            for (int i = 0; i < maxSet; i++) {
                arrayList.add(Boolean.TRUE);
            }
            for (int j = 0; j < maxGet; j++) {
                arrayList.get(j);
            }
        };
        runTimes(maxConcurrent, "arrayList", arrayListConsumer);
        Consumer copyOnWriteArrayListConsumer = x -> {
            for (int i = 0; i < maxSet; i++) {
                copyOnWriteArrayList.add(Boolean.TRUE);
            }
            for (int j = 0; j < maxGet; j++) {
                copyOnWriteArrayList.get(j);
            }
        };
        runTimes(maxConcurrent, "copyOnWriteArrayListConsumer", copyOnWriteArrayListConsumer);
        Consumer concurrentArrayListConsumer = x -> {
            for (int i = 0; i < maxSet; i++) {
                concurrentArrayList.add(Boolean.TRUE);
            }
            for (int j = 0; j < maxGet; j++) {
                concurrentArrayList.get(j);
            }
        };
        runTimes(maxConcurrent, "concurrentArrayListConsumer", concurrentArrayListConsumer);
    }

    private void runTimes(int maxConcurrent, String tag, Consumer consumer) throws InterruptedException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        CountDownLatch latch = new CountDownLatch(maxConcurrent);
        for (int i = 0; i < maxConcurrent; i++) {
            new Thread(() -> {
                consumer.accept(null);
                latch.countDown();
            }).start();
        }
        latch.await();
        System.out.println(tag + ":" + stopwatch);
    }
}
