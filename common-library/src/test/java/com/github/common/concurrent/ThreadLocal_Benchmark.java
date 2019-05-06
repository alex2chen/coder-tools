package com.github.common.concurrent;

import io.netty.util.concurrent.FastThreadLocalThread;
import io.netty.util.internal.InternalThreadLocalMap;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author alex.chen
 * @Description: JMH参阅: HexBenchmark
 * 性能大比拼:
 * Benchmark                                      Mode  Cnt        Score   Units
 * ThreadLocal_Benchmark.directGet               thrpt    3  1370987.674  ops/ms
 * ThreadLocal_Benchmark.attachThread            thrpt    3  1014565.875  ops/ms
 * ThreadLocal_Benchmark.arrayFastThreadProducer thrpt    3  1001347.960  ops/ms
 * ThreadLocal_Benchmark.fastThread              thrpt    3   998538.489  ops/ms
 * ThreadLocal_Benchmark.holderArrayFastThread   thrpt    3   866939.228  ops/ms
 * ThreadLocal_Benchmark.arrayFastThread         thrpt    3   795228.062  ops/ms
 * ThreadLocal_Benchmark.threadLocal             thrpt    3   716136.844  ops/ms
 * ThreadLocal_Benchmark.concurrentMapThread     thrpt    3   608984.534  ops/ms
 * ThreadLocal_Benchmark.concurrentMapId         thrpt    3   531199.615  ops/ms
 * ThreadLocal_Benchmark.concurrentLinkedQueue   thrpt    3     7100.274  ops/ms
 * @date 2018/4/27
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@Threads(4)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ThreadLocal_Benchmark {
    private Integer value = 100;

    private ConcurrentHashMap<Long, Integer> concurrentMapId = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Thread, Integer> concurrentMapThread = new ConcurrentHashMap<>();
    private ConcurrentLinkedQueue<Integer> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
    private CopyOnWriteArrayList<Long> copyOnWrite = new CopyOnWriteArrayList<>();
    private ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
    private FastThreadLocalThread fastThread = new FastThreadLocalThread();
    private Supplier<Integer> supplier = () -> value;
    private AttachThread attachThread = new AttachThread();
    private ConcurrentIntToObjectMap<Integer> arrayFastThread = new ConcurrentIntToObjectMap<>(1024);
    private Holder holderArrayFastThread = new Holder(new Holder(new ConcurrentIntToObjectMap<>()));

    public ThreadLocal_Benchmark() {
        fastThread.setThreadLocalMap(InternalThreadLocalMap.get());
    }

    @Benchmark
    @Fork(1)
    public Integer directGet() {
        return value;
    }

    @Benchmark
    @Fork(1)
    public Integer threadLocal() {
        Integer data = threadLocal.get();
        if (data != null) {
            return data;
        }
        data = 100;
        threadLocal.set(data);
        return data;
    }

    @Benchmark
    @Fork(1)
    public Integer concurrentMapThread() {
        Thread key = Thread.currentThread();
        Integer data = concurrentMapThread.get(key);

        if (data != null) {
            return data;
        }
        data = 100;
        concurrentMapThread.put(key, data);
        return data;
    }

    @Benchmark
    @Fork(1)
    public Integer concurrentMapId() {
        Long key = Thread.currentThread().getId();
        Integer data = concurrentMapId.get(key);

        if (data != null) {
            return data;
        }
        data = 100;
        concurrentMapId.put(key, data);
        return data;
    }

//    @Benchmark
//    @Fork(1)
//    public Long copyOnWrite() {
//        int key = (int) Thread.currentThread().getId();
//        synchronized (copyOnWrite) {//不处理否则会IndexOutOfBoundsException
//            copyOnWrite.add(key, 100L);
//            return copyOnWrite.get(key);
//        }
//    }

    @Benchmark
    @Fork(1)
    public Integer concurrentLinkedQueue() {
        Integer data = concurrentLinkedQueue.poll();
        if (data == null) {
            data = 100;
        }
        concurrentLinkedQueue.add(data);
        return data;
    }

    @Benchmark
    @Fork(1)
    public Integer fastThread() {
        Object obj = fastThread.threadLocalMap().indexedVariable(256);
        if (obj != InternalThreadLocalMap.UNSET) {
            return (Integer) obj;
        }
        Integer data = 100;
        fastThread.threadLocalMap().setIndexedVariable(256, data);
        return data;
    }

    @Benchmark
    @Fork(1)
    public Integer attachThread() {
        return attachThread.getOrUpdate(1, supplier);
    }

    @Benchmark
    @Fork(1)
    public Integer arrayFastThread() {
        int key = (int) Thread.currentThread().getId();
        return arrayFastThread.getOrUpdate(key, () -> 1);
    }

    @Benchmark
    @Fork(1)
    public Integer arrayFastThreadProducer() {
        int key = (int) Thread.currentThread().getId();
        return arrayFastThread.getOrUpdate(key, supplier);
    }

    @Benchmark
    @Fork(1)
    public Integer holderArrayFastThread() {
        return ((ConcurrentIntToObjectMap<Integer>) ((Holder) holderArrayFastThread.obj).obj).getOrUpdate(1, () -> 1);
    }
//    public Integer fastThreadWithArrayMap() {
//        Object obj = fastThread.threadLocalMap().indexedVariable(1024);
//        ConcurrentIntToObjectArrayMap<Integer> map;
//
//        if (obj != InternalThreadLocalMap.UNSET) {
//            map = (ConcurrentIntToObjectArrayMap<Integer>) obj;
//            return map.getOrUpdate(1024, () -> value);
//        }
//        map = new ConcurrentIntToObjectArrayMap<>();
//        fastThread.threadLocalMap().setIndexedVariable(1024, map);
//        return map.getOrUpdate(1024, () -> value);
//    }

//    public Integer attachmentThreadUtils() {
//        return AttachmentThreadUtils.getOrUpdate(1, supplier);
//    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ThreadLocal_Benchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }

    class Holder {
        public final Object obj;

        public Holder(Object obj) {
            this.obj = obj;
        }

    }
}
