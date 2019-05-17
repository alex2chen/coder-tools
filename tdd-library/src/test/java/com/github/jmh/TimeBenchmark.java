package com.github.jmh;

import com.github.util.SystemClock;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/4/16.
 * Benchmark                        Mode  Cnt   Score     Error  Units
 *
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)//基准测试类型
@Warmup(iterations = 2)//预热轮数
@Measurement(iterations = 3)//度量
@Threads(4)//测试线程
@OutputTimeUnit(TimeUnit.SECONDS)//基准测试结果的时间类型（秒）
public class TimeBenchmark {
//    @Benchmark
//    @Fork(2)
//    public long currentTimeMillis() throws Exception {
//        return System.currentTimeMillis();
//    }
//
//    @Benchmark
//    @Fork(2)
//    public long nanoTime() throws Exception {
//        return System.nanoTime();
//    }
//
//    @Benchmark
//    @Fork(2)
//    public Instant nowInstant() throws Exception {
//        return Instant.now();
//    }
    @Benchmark
    @Fork(2)
    public long fastSeconds() throws Exception {
        return SystemClock.fast().seconds();
    }
//    @Benchmark
//    @Fork(2)
//    public long realTimeSeconds() throws Exception {
//        return SystemClock.realTime().seconds();
//    }
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(TimeBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
