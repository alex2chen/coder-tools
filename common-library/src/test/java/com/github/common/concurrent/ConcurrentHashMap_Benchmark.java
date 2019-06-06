package com.github.common.concurrent;

import com.github.common.base.map.FastMap;
import com.github.common.base.map.KeyNamespace;
import com.github.common.base.map.MapKey;
import org.apache.commons.collections.FastHashMap;
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
 * ConcurrentHashMap_Benchmark.concurrentFastMap               thrpt    3   3001.330  ops/ms
 * ConcurrentHashMap_Benchmark.concurrentMap                   thrpt    3  12854.327  ops/ms
 * ConcurrentHashMap_Benchmark.eclipseConcurrentHashMap        thrpt    3  13344.197  ops/ms
 * ConcurrentHashMap_Benchmark.eclipseConcurrentHashMapUnsafe  thrpt    3  12563.317  ops/ms
 * ConcurrentHashMap_Benchmark.intToObjectMap                  thrpt    3  71481.224  ops/ms
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@Threads(4)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ConcurrentHashMap_Benchmark {
    private int size = 1024 * 256;
    private final ConcurrentIntToObjectMap<Integer> intToObjectMap = new ConcurrentIntToObjectMap<>();
    private final ConcurrentHashMap<Integer, Integer> concurrentMap = new ConcurrentHashMap<>();
    private org.eclipse.collections.impl.map.mutable.ConcurrentHashMap<Integer, Integer> eclipseConcurrentHashMap//
            = new org.eclipse.collections.impl.map.mutable.ConcurrentHashMap<>();
    private final ConcurrentHashMapUnsafe<Integer, Integer> eclipseConcurrentHashMapUnsafe = new ConcurrentHashMapUnsafe<>();
    private MapKey[] initKeys = new MapKey[size];
    private KeyNamespace keyNamespace = KeyNamespace.createNamespace("order");
    private FastMap<Integer> fastMap = new ConcurrentFastMap(keyNamespace, size);

    public ConcurrentHashMap_Benchmark() {
        for (int i = 0; i < size; i++) {
            intToObjectMap.put(i, i);
            concurrentMap.put(i, i);
            eclipseConcurrentHashMap.put(i, i);
            eclipseConcurrentHashMapUnsafe.put(i, i);
            MapKey key = new MapKey(keyNamespace, "" + i);
            this.initKeys[i] = key;
            fastMap.put(key, i);
        }
    }

    @Benchmark
    @Fork(1)
    public Integer intToObjectMap() {
        int random = ThreadLocalRandom.current().nextInt(size);
        intToObjectMap.put(random, null);
        intToObjectMap.put(random, random);
        return intToObjectMap.get(random);
    }

    @Benchmark
    @Fork(1)
    public Integer concurrentFastMap() {
        int random = ThreadLocalRandom.current().nextInt(size);
        MapKey key = initKeys[random];
        fastMap.remove(key);
        fastMap.put(key, random);
        return fastMap.get(key);
    }

    @Benchmark
    @Fork(1)
    public Integer concurrentMap() {
        int random = ThreadLocalRandom.current().nextInt(size);
        concurrentMap.remove(random);
        concurrentMap.put(random, random);
        return concurrentMap.get(random);
    }

    @Benchmark
    @Fork(1)
    public Integer eclipseConcurrentHashMap() {
        int random = ThreadLocalRandom.current().nextInt(size);
        eclipseConcurrentHashMap.remove(random);
        eclipseConcurrentHashMap.put(random, random);
        return eclipseConcurrentHashMap.get(random);
    }

    @Benchmark
    @Fork(1)
    public Integer eclipseConcurrentHashMapUnsafe() {
        int random = ThreadLocalRandom.current().nextInt(size);
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
