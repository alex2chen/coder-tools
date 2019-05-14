package com.github.common.concurrent;

import org.eclipse.collections.impl.map.mutable.ConcurrentHashMapUnsafe;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @Author: alex
 * @Description: JMH参阅: HexBenchmark
 * @Date: created in 2018/5/14.
 * 性能大比拼:
 * Benchmark                                                    Mode  Cnt      Score   Units
 * ConcurrentHashMap_Benchmark.concurrentMap                   thrpt    3  12649.733  ops/ms
 * ConcurrentHashMap_Benchmark.eclipseConcurrentHashMap        thrpt    3  11562.642  ops/ms
 * ConcurrentHashMap_Benchmark.eclipseConcurrentHashMapUnsafe  thrpt    3  12718.617  ops/ms
 * ConcurrentHashMap_Benchmark.intToObjectMap                  thrpt    3  83356.263  ops/ms
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@Threads(4)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ConcurrentHashMap_Benchmark {
    private final ConcurrentIntToObjectMap<Integer> intToObjectMap = new ConcurrentIntToObjectMap<>();
    private final ConcurrentHashMap<Integer, Integer> concurrentMap = new ConcurrentHashMap<>();
    private org.eclipse.collections.impl.map.mutable.ConcurrentHashMap<Integer, Integer> eclipseConcurrentHashMap//
            = new org.eclipse.collections.impl.map.mutable.ConcurrentHashMap<>();
    private final ConcurrentHashMapUnsafe<Integer, Integer> eclipseConcurrentHashMapUnsafe = new ConcurrentHashMapUnsafe<>();

    public ConcurrentHashMap_Benchmark() {
        for (int i = 0; i < 1024 * 256; i++) {
            intToObjectMap.put(i, i);
            concurrentMap.put(i, i);
            eclipseConcurrentHashMap.put(i, i);
            eclipseConcurrentHashMapUnsafe.put(i, i);
        }
    }

    @Benchmark
    @Fork(1)
    public Integer intToObjectMap() {
        int random = ThreadLocalRandom.current().nextInt(1024 * 256);
        intToObjectMap.put(random, null);
        intToObjectMap.put(random, random);
        return intToObjectMap.get(random);
    }

    @Benchmark
    @Fork(1)
    public Integer concurrentMap() {
        int random = ThreadLocalRandom.current().nextInt(1024 * 256);
        concurrentMap.remove(random);
        concurrentMap.put(random, random);
        return concurrentMap.get(random);
    }

    @Benchmark
    @Fork(1)
    public Integer eclipseConcurrentHashMap() {
        int random = ThreadLocalRandom.current().nextInt(1024 * 256);
        eclipseConcurrentHashMap.remove(random);
        eclipseConcurrentHashMap.put(random, random);
        return eclipseConcurrentHashMap.get(random);
    }

    @Benchmark
    @Fork(1)
    public Integer eclipseConcurrentHashMapUnsafe() {
        int random = ThreadLocalRandom.current().nextInt(1024 * 256);
        eclipseConcurrentHashMapUnsafe.remove(random);
        eclipseConcurrentHashMapUnsafe.put(random, random);
        return eclipseConcurrentHashMapUnsafe.get(random);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ConcurrentHashMap_Benchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }

}
