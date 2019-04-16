package com.github.jmh;

import com.google.common.base.Stopwatch;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/4/16.
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)//基准测试类型
@Warmup(iterations = 2)//预热轮数
@Measurement(iterations = 3)//度量
@Threads(4)//测试线程
@OutputTimeUnit(TimeUnit.MILLISECONDS)//基准测试结果的时间类型（毫秒）
public class TimeBenchmark {
    @Benchmark
    @Fork(2)
    public long currentTimeMillis() throws Exception {
        return System.currentTimeMillis();
    }

    @Benchmark
    @Fork(2)
    public long nanoTime() throws Exception {
        return System.nanoTime();
    }

    @Benchmark
    @Fork(2)
    public Instant nowInstant() throws Exception {
        return Instant.now();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(TimeBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
