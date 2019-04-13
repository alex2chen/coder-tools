package com.github.common;

import com.codahale.metrics.*;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author alex.chen
 * @Description metrics-core https://metrics.dropwizard.io/3.1.0/getting-started/
 * @date 2018/4/13
 */
public class Metrics_unit {
    private Random random = new Random();

    @Test
    public void monitorQueueSize() throws InterruptedException {
        Queue<String> q = new LinkedList<>();
        MetricRegistry registry = new MetricRegistry();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
        reporter.start(1, TimeUnit.SECONDS);
        registry.register(MetricRegistry.name(Metrics_unit.class, "queue", "size"), (Gauge<Integer>) () -> q.size());
        while (true) {
            Thread.sleep(1000);
            q.add("Job-xxx");
        }
    }

    @Test
    public void monitorGauges() throws InterruptedException {//待处理队列中任务的个数
        Queue<String> q = new LinkedList<String>();
        MetricRegistry registry = new MetricRegistry();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
        reporter.start(1, TimeUnit.SECONDS);

        registry.register(MetricRegistry.name(Metrics_unit.class, "queue", "size"),
                new Gauge<Integer>() {
                    public Integer getValue() {
                        return q.size();
                    }
                });

        while (true) {
            Thread.sleep(1000);
            q.add("Job-xxx");
        }
    }

    /**
     * Meter度量一系列事件发生的速率(rate)，例如TPS。Meters会统计最近1分钟，5分钟，15分钟，还有全部时间的速率。
     */
    @Test
    public void monitorMeters() throws InterruptedException {
        MetricRegistry registry = new MetricRegistry();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
        reporter.start(1, TimeUnit.SECONDS);
        Meter meterTps = registry.meter(MetricRegistry.name(Metrics_unit.class, "request", "tps"));
        while (true) {
            request(meterTps, random.nextInt(5));
            Thread.sleep(1000);
        }
    }

    @Test
    public void monitorHistograms() throws InterruptedException {
        //统计数据的分布情况:最小值，最大值，中间值，还有中位数，75%, 90%, 95%, 98%, 99%
        MetricRegistry registry = new MetricRegistry();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
        reporter.start(1, TimeUnit.SECONDS);
        Histogram histogram = new Histogram(new ExponentiallyDecayingReservoir());
        registry.register(MetricRegistry.name(Metrics_unit.class, "request", "histogram"), histogram);

        while (true) {
            Thread.sleep(1000);
            histogram.update(random.nextInt(100000));
        }
    }

    @Test
    public void monitorTimers() throws InterruptedException {
        //Timer其实是 Histogram 和 Meter 的结合， histogram 某部分代码/调用的耗时， meter统计TPS
        MetricRegistry registry = new MetricRegistry();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
        reporter.start(1, TimeUnit.SECONDS);
        Timer timer = registry.timer(MetricRegistry.name(Metrics_unit.class, "get-latency"));
        Timer.Context ctx;
        while (true) {
            ctx = timer.time();
            Thread.sleep(random.nextInt(1000));
            ctx.stop();
        }
    }

    public void request(Meter meter) {
        System.out.println("request");
        meter.mark();
    }

    public void request(Meter meter, int n) {
        while (n > 0) {
            request(meter);
            n--;
        }
    }
}
