package com.github.common.identity;

import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: alex
 * @Description: JMH参阅: HexBenchmark
 * @Date: created in 2018/5/14.
 * 性能大比拼:
 * Benchmark                                Mode  Cnt       Score   Units
 * UUID_Benchmark.newObjectId128           thrpt    3  403354.861  ops/ms
 * UUID_Benchmark.newRandomId              thrpt    3  327816.477  ops/ms
 * UUID_Benchmark.objectId                 thrpt    3  319832.224  ops/ms
 * UUID_Benchmark.newRandomIdHexString     thrpt    3   63780.645  ops/ms
 * UUID_Benchmark.objectIdHexString        thrpt    3   53133.250  ops/ms
 * UUID_Benchmark.newObjectId128HexString  thrpt    3   45083.233  ops/ms
 * UUID_Benchmark.uuidString               thrpt    3    1703.114  ops/ms
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@Threads(4)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class UUID_Benchmark {

    @Benchmark
    @Fork(1)
    public String uuidString() throws Exception {
        return UUID.randomUUID().toString();
    }

    @Benchmark
    @Fork(1)
    public ObjectId objectId() throws Exception {
        return ObjectId.next();
    }

    @Benchmark
    @Fork(1)
    @Test
    public void objectIdHexString() throws Exception {
        String id = ObjectId.next().toHexString();
        //System.out.println(id);
    }

    @Benchmark
    @Fork(1)
    public void newObjectId128() throws Exception {
        ObjectId128 next = ObjectId128.next();
    }

    @Benchmark
    @Fork(1)
    @Test
    public void newObjectId128HexString() throws Exception {
        String id = ObjectId128.next().toHexString();
        //System.out.println(id);
    }

    @Benchmark
    @Fork(1)
    public RandomId newRandomId() throws Exception {
        return RandomId.next();
    }

    @Benchmark
    @Fork(1)
    @Test
    public void newRandomIdHexString() throws Exception {
        String id = RandomId.next().toHexString();
        //System.out.println(id);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(UUID_Benchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
