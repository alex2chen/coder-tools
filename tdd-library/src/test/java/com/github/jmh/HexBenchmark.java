package com.github.jmh;

import com.google.common.base.Stopwatch;
import com.google.common.io.BaseEncoding;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @Author: alex
 * @Description: JMH是一个microbenchmark（微基准测试）框架（2013年首次发布）。
 * JMH Samples： http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/
 * Java Performance Tuning Guide：  http://java-performance.info/jmh/
 * Using JMH for Java Microbenchmarking：https://nitschinger.at/Using-JMH-for-Java-Microbenchmarking/
 * <p>
 * 与其他众多框架相比它的特色优势在于，它是由Oracle实现JIT的相同人员开发的。JMH可能与最新的Oracle JRE同步，其结果可信度很高。
 * @Date: created in 2019/4/16.
 */
@State(Scope.Benchmark)
public class HexBenchmark {
    private String str = "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈";
    private byte[] bytes = str.getBytes();

    @Benchmark //它是基准测试方法
    /**测试类型
     * Throughput	吞吐量，每段时间执行的次数，时间窗口是1秒，ops/s代表Operation per second
     * AverageTime	平均时间，每次操作的平均耗时，时间窗口是1秒
     * SampleTime	在测试中，随机进行采样执行的时间，时间窗口是1秒（会采样百分比）
     * SingleShotTime	在每次执行中计算耗时，无时间窗口
     * All	顾名思义，所有模式，这个在内部测试中常用
     */
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.SECONDS)//结果的时间类型
    public void do_nothing() {
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.SECONDS)
    public String toHexByGuava() {
//        Stopwatch stopwatch=Stopwatch.createStarted();
        String encode = BaseEncoding.base16().encode(bytes);
//        System.out.println(stopwatch);
        return encode;
    }

    /**
     * 思考：该方法每轮（单个Warmup或单个iteration）究竟会被执行多少次？
     * 要回答这个问题你可以这样理解：一个时间窗口（1秒，每轮就是1s）同时开启threads个线程执行calcTimes()方法，每个线程反复目标方法
     * 下面示例方法每轮理论上会被执行20次（实际值可能多于或少于该值），解析：20=4（开启了4个线程）*5（1秒可以运行5次）
     *
     * @return
     * @throws Exception
     */
    @Benchmark
    @BenchmarkMode({Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.SECONDS)
    public long calcTimes() throws Exception {
        Thread.sleep(200);
        System.out.print("JMH:");
        return System.currentTimeMillis();
    }

    public static void main(String[] args) throws RunnerException {
        int CONCURRENCY = Runtime.getRuntime().availableProcessors();
        Options opt = new OptionsBuilder()
                .include(HexBenchmark.class.getSimpleName())
                /**连续预热轮数
                 * # Warmup: 2 iterations, 1 s each，每秒预热1轮
                 * 等价于@Warmup(iterations = 2)
                 */
                .warmupIterations(2)
                /**度量
                 * # Measurement: 3 iterations, 1 s each，每秒进行1轮
                 * # Timeout: 10 min per iteration
                 * iterations进行测试的轮次,time每轮进行的时长,timeUnit时长单位
                 * 等价于@Measurement(iterations = 3)
                 */
                .measurementIterations(3)
                /**每个进程中的测试线程
                 * # Threads: 4 threads, will synchronize iterations，每轮开启4个线程同时运行
                 * 等价于@Threads(4)
                 */
                .threads(CONCURRENCY)
                /**进程数
                 * 为每个试验(迭代集合)fork一个新的java进程,等价于@Fork(2)
                 */
                .forks(2)
                .build();
        new Runner(opt).run();
    }
}
