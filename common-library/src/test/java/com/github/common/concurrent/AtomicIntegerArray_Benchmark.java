package com.github.common.concurrent;


import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @Author: alex
 * @Description: JMH参阅: HexBenchmark
 * @Date: created in 2018/5/14.
 * 性能大比拼:
 * Benchmark                                    Mode  Cnt       Score   Units
 * AtomicIntegerArray_Benchmark.getIntegerArraySet  thrpt    3  1646030.212  ops/ms
 * AtomicIntegerArray_Benchmark.getMuiltInteger     thrpt    3  2266805.353  ops/ms
 * AtomicIntegerArray_Benchmark.setIntegerArraySet  thrpt    3    47180.170  ops/ms
 * AtomicIntegerArray_Benchmark.setMuiltInteger     thrpt    3  134984.957  ops/ms
 * AtomicIntegerArray_Benchmark.sumIntegerArray     thrpt    3    33477.159  ops/ms
 * AtomicIntegerArray_Benchmark.sumMuiltInteger     thrpt    3   229574.495  ops/ms
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@Threads(4)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class AtomicIntegerArray_Benchmark {
    private final int length = 4;
    private final AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(length);
    private final AtomicMuiltInteger atomicMuiltInteger = new AtomicMuiltInteger(length);

    @Benchmark
    @Fork(1)
    public void getIntegerArraySet() {
        int index = ThreadLocalRandom.current().nextInt(length);
        int value = atomicIntegerArray.get(index);
    }
    @Benchmark
    @Fork(1)
    public void getMuiltInteger() {
        int index = ThreadLocalRandom.current().nextInt(length);
        int value = atomicMuiltInteger.get(index);
    }
    @Benchmark
    @Fork(1)
    public void setIntegerArraySet() {
        int index = ThreadLocalRandom.current().nextInt(length);
        int value = atomicIntegerArray.get(index);
        atomicIntegerArray.set(index, value + 1);
    }
    @Benchmark
    @Fork(1)
    public void setMuiltInteger() {
        int index = ThreadLocalRandom.current().nextInt(length);
        int value = atomicMuiltInteger.get(index);
        atomicMuiltInteger.set(index, value + 1);
    }
    @Benchmark
    @Fork(1)
    @Test
    public void sumIntegerArray() {
        atomicIntegerArray.set(1, 1);
        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += atomicIntegerArray.get(i);
        }
        //System.out.println(sum);
    }
    @Benchmark
    @Fork(1)
    @Test
    public void sumMuiltInteger() {
        atomicMuiltInteger.set(1, 1);
        int sum = atomicMuiltInteger.sum();
        //System.out.println(sum);
    }
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(AtomicIntegerArray_Benchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
