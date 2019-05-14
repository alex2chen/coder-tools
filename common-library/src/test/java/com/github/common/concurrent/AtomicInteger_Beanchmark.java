package com.github.common.concurrent;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Author: alex
 * @Description: JMH参阅: HexBenchmark
 * @Date: created in 2018/5/14.
 * 性能大比拼:
 * Benchmark                                          Mode  Cnt       Score   Units
 * AtomicInteger_Beanchmark.nextAtomicInteger        thrpt    3   47904.510  ops/ms
 * AtomicInteger_Beanchmark.nextConcurAtomicInteger  thrpt    3  501656.877  ops/ms
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@Threads(4)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class AtomicInteger_Beanchmark {
    private final AtomicInteger atomicInteger = new AtomicInteger(new SecureRandom().nextInt());
    private final LongAdder longAdder = new LongAdder();
    private final ConcurrentAtomicInteger concurAtomicInteger = new ConcurrentAtomicInteger(new SecureRandom().nextInt());

    @Benchmark
    @Fork(1)
    public String uuidString() throws Exception {
        return UUID.randomUUID().toString();
    }

    @Benchmark
    @Fork(1)
    public int nextAtomicInteger() throws Exception {
        return atomicInteger.incrementAndGet();
    }

    @Benchmark
    @Fork(1)
    public int nextConcurAtomicInteger() throws Exception {
        return concurAtomicInteger.next();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(AtomicInteger_Beanchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

}
