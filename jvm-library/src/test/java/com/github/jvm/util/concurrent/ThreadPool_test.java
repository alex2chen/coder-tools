package com.github.jvm.util.concurrent;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: alex
 * @Description: ThreadPoolExecutor源码解析 https://blog.csdn.net/alex_xfboy/article/details/87806902
 * @Date: created in 2018/8/9.
 */
public class ThreadPool_test {
    private ExecutorService defaultExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors(),
            0L, TimeUnit.MILLISECONDS,
//            new SynchronousQueue(), new ThreadFactoryBuilder().setNameFormat("event-bus-pool-%d").build());
            new LinkedBlockingQueue<>(3), new ThreadFactoryBuilder().setNameFormat("event-bus-pool-%d").build());

    @Test
    public void threadPool_run() throws InterruptedException {
        for (int i = 0; i < 7; i++) {
            defaultExecutor.execute(() -> {
                System.out.println(new Date() + " " + Thread.currentThread().getName() + " start.");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(new Date() + " " + Thread.currentThread().getName() + " stop.");
            });
        }
        Thread.sleep(Integer.MAX_VALUE);
    }
}
