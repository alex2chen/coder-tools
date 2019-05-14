package com.github.common.concurrent;

import com.github.util.SystemClock;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: alex
 * @Description: JMH参阅: HexBenchmark
 * @Date: created in 2018/5/14.
 * 性能大比拼:
 * Benchmark                                  Mode  Cnt      Score   Units
 * FutureHolder_Benchmark.completableFuture  thrpt    3  23558.180  ops/ms
 * FutureHolder_Benchmark.futureContainer1   thrpt    3    976.991  ops/ms
 * FutureHolder_Benchmark.futureContainer2   thrpt    3   1144.061  ops/ms
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@Threads(4)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class FutureHolder_Benchmark {
    private AtomicInteger sequencer = new AtomicInteger(0);
    private final Long DEFAULT_TIME_OUT = 5 * 1000L;
    ResponseFutureContainer1 futureContainer1 = new ResponseFutureContainer1();
    ResponseFutureContainer2 futureContainer2 = new ResponseFutureContainer2();

    @Benchmark
    @Fork(1)
    public void completableFuture() {
        int requestId = sequencer.getAndIncrement();
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        Integer response = sequencer.getAndIncrement();
        completableFuture.complete(response);
    }

    @Benchmark
    @Fork(1)
    public void futureContainer1() {
        int requestId = sequencer.getAndIncrement();
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        futureContainer1.addFuture(requestId, completableFuture);
        Integer response = sequencer.getAndIncrement();
        futureContainer1.notifyResponse(response);
    }

    @Benchmark
    @Fork(1)
    public void futureContainer2() {
        int requestId = sequencer.getAndIncrement();
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        futureContainer2.addFuture(requestId, completableFuture);
        Integer response = sequencer.getAndIncrement();
        futureContainer2.notifyResponse(response);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(FutureHolder_Benchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }

    public final class FutureWithExpire<T> {
        public final CompletableFuture<T> future;
        public final long expireTime;

        public FutureWithExpire(CompletableFuture<T> future, long expireTime) {
            this.future = future;
            this.expireTime = expireTime;
        }
    }

    public final class ResponseFutureContainer1 implements Closeable {
        private volatile boolean isClosing = false;

        private final ConcurrentHashMap<Integer, FutureWithExpire<Integer>> futureMap = //
                new ConcurrentHashMap<>(32, 0.25F);

        public void addFuture(int requestId, CompletableFuture<Integer> future) {
            addFuture(requestId, future, DEFAULT_TIME_OUT);
        }

        public void addFuture(int requestId, CompletableFuture<Integer> future, long timeout) {
            if (future.isDone()) {
                return;
            }
            if (isClosing) {
                throw new RuntimeException("it's closed");
            }
            long expireTime = timeout + SystemClock.fast().mills();
            futureMap.put(requestId, new FutureWithExpire<>(future, expireTime));
        }

        public void remove(int requestId) {
            futureMap.remove(requestId);
        }

        public void notifyResponse(Integer responseId) {
            if (responseId == null) {
                return;
            }
            FutureWithExpire<Integer> futureWithExpire = futureMap.remove(responseId);

            if (futureWithExpire == null) {
                return;
            }
            futureWithExpire.future.complete(responseId);
        }

        /**
         * 外部线程周期性调用
         */
        public void doExpireJob() {
            if (isClosing) {
                return;
            }

            futureMap.forEach((key, value) -> {
                doExpire(key, value);
            });
        }

        private void doExpire(long requestId, FutureWithExpire<Integer> futureWithExpire) {
            CompletableFuture<Integer> future = futureWithExpire.future;
            if (future.isDone()) {
                return;
            }
            long currentTime = SystemClock.fast().mills();
            if (futureWithExpire.expireTime > currentTime) {
                return;
            }
            future.completeExceptionally(new RuntimeException("it's timeout"));

            futureMap.remove(requestId);
        }

        /**
         * 会尝试平滑退出, 不会实际抛出异常
         */
        @Override
        public void close() throws IOException {
            // 尝试平滑退出
            for (int i = 0; i < DEFAULT_TIME_OUT; i += 100) {
                doExpireJob();

                if (futureMap.isEmpty()) {
                    break;
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }

            isClosing = true;
        }
    }

    public final class ResponseFutureContainer2 implements Closeable {
        private volatile boolean isClosing = false;

        private static final int SEGMENT = 64;

        private final ConcurrentHashMap<Integer, FutureWithExpire<Integer>>[] futureMapArray;

        @SuppressWarnings("unchecked")
        public ResponseFutureContainer2() {
            futureMapArray = new ConcurrentHashMap[SEGMENT];

            for (int i = 0; i < SEGMENT; i++) {
                futureMapArray[i] = new ConcurrentHashMap<>();
            }
        }

        public void addFuture(int requestId, CompletableFuture<Integer> future) {
            addFuture(requestId, future, DEFAULT_TIME_OUT);
        }

        private ConcurrentHashMap<Integer, FutureWithExpire<Integer>> futureMap(int requestId) {
            int index = requestId & (SEGMENT - 1);
            return futureMapArray[index];
        }

        public void addFuture(int requestId, CompletableFuture<Integer> future, long timeout) {
            if (future.isDone()) {
                return;
            }

            if (isClosing) {
                throw new RuntimeException("it's closed");
            }

            long expireTime = timeout + SystemClock.fast().mills();

            futureMap(requestId).put(requestId, new FutureWithExpire<>(future, expireTime));
        }

        public void remove(int requestId) {
            futureMap(requestId).remove(requestId);
        }

        public void notifyResponse(Integer response) {
            if (response == null) {
                return;
            }

            int requestId = response;
            FutureWithExpire<Integer> futureWithExpire = futureMap(requestId).remove(requestId);

            if (futureWithExpire == null) {
                return;
            }
            futureWithExpire.future.complete(response);
        }

        /**
         * 外部线程周期性调用
         */
        public void doExpireJob() {
            if (isClosing) {
                return;
            }

        }

        /**
         * 会尝试平滑退出, 不会实际抛出异常
         */
        @Override
        public void close() throws IOException {
            // 尝试平滑退出
            for (int i = 0; i < DEFAULT_TIME_OUT; i += 100) {
                doExpireJob();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }

            isClosing = true;
        }
    }
}
