package com.github.common.concurrent;

import com.github.jvm.util.UnsafeUtils;
import com.google.common.base.Stopwatch;
import org.junit.Assert;
import org.junit.Test;
import sun.misc.Contended;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: alex
 * @Description: CPU读取内存数据时并非一次只读一个字节，而是会读一段64字节长度的连续的内存块(chunks of memory)，这些块我们称之为缓存行(Cache line)。
 * 伪共享指的是多个线程同时读写同一个缓存行的不同变量时导致的 CPU 缓存失效，尽管这些变量之间没有任何关系，但由于在主内存中邻近，存在于同一个缓存行之中，它们的相互覆盖会导致频繁的缓存未命中，引发性能下降。
 * @Date: created in 2017/5/17.
 */
public class FalseSharing_unit {
    private final static int MAX_THREADS = 50;
    private static DisabledSharingValue[] disValues = new DisabledSharingValue[MAX_THREADS];
    private static PaddingValue[] padValues = new PaddingValue[MAX_THREADS];
    private static ContendedValue[] contValues = new ContendedValue[MAX_THREADS];

    /**
     * 横向遍历 cost:17.55 ms
     * 纵向遍历 cost:61.29 ms
     */
    @Test
    public void showCacheLine(){
        long sum=0;
        long[][] arr=new long[1024 * 1024][8];
        Stopwatch stopWatch = Stopwatch.createStarted();
        for (int i = 0; i < 1024 * 1024; i += 1) {
            for (int j = 0; j < 8; j++) {
                sum += arr[i][j];
            }
        }
        System.out.printf("横向遍历 cost:%s\n",stopWatch);
        sum=0;
        stopWatch = Stopwatch.createStarted();
        for (int i = 0; i < 8; i += 1) {
            for (int j = 0; j < 1024 * 1024; j++) {
                sum += arr[j][i];
            }
        }
        System.out.printf("纵向遍历 cost:%s\n",stopWatch);
    }
    @Test
    public void disableShareRun() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(MAX_THREADS);
        Thread[] threads = new Thread[MAX_THREADS];
        for (int i = 0; i < MAX_THREADS; i++) {
            disValues[i] = new DisabledSharingValue();
            threads[i] = new DisabledSharingRun(countDownLatch, i, i % 2 == 0);
        }
        Stopwatch stopWatch = Stopwatch.createStarted();
        for (Thread t : threads) {
            t.start();
        }
        countDownLatch.await();
        System.out.printf("disableShareRun cost: %s \n", stopWatch);
    }

    @Test
    public void paddingValueRun() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(MAX_THREADS);
        Thread[] threadsPadding = new Thread[MAX_THREADS];
        for (int i = 0; i < MAX_THREADS; i++) {
            padValues[i] = new PaddingValue();
            threadsPadding[i] = new PaddingValueRun(countDownLatch, i, i % 2 == 0);
        }
        Stopwatch stopWatch = Stopwatch.createStarted();
        for (Thread t : threadsPadding) {
            t.start();
        }
        countDownLatch.await();
        System.out.printf("paddingValueRun cost: %s \n", stopWatch);
    }

    @Test
    public void contendedValueRun() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(MAX_THREADS);
        Thread[] threadsContended = new Thread[MAX_THREADS];
        for (int i = 0; i < MAX_THREADS; i++) {
            contValues[i] = new ContendedValue();
            threadsContended[i] = new ContendedValueRun(countDownLatch, i, i % 2 == 0);
        }
        Stopwatch stopWatch = Stopwatch.createStarted();
        for (Thread t : threadsContended) {
            t.start();
        }
        countDownLatch.await();
        System.out.printf("contendedValueRun cost: %s \n", stopWatch);
    }

    /**
     * Contended注解默认是无效的，启动参数中添加: -XX:-RestrictContended
     */
    @Test
    public void showOffset() throws NoSuchFieldException {
        long value = UnsafeUtils.unsafe().objectFieldOffset(DisabledSharingValue.class.getDeclaredField("value"));
        System.out.println(value);
        Assert.assertTrue(value < 64);
        value = UnsafeUtils.unsafe().objectFieldOffset(PaddingValue0.class.getDeclaredField("value"));
        System.out.println(value);
        Assert.assertTrue(value > 64);
        value = UnsafeUtils.unsafe().objectFieldOffset(ContendedValue.class.getDeclaredField("value"));
        System.out.println(value);
        Assert.assertTrue(value > 64);
        value = UnsafeUtils.unsafe().objectFieldOffset(VolatileLong4.class.getDeclaredField("x"));
        System.out.println(value);
        Assert.assertTrue(value < 64);
        value = UnsafeUtils.unsafe().objectFieldOffset(VolatileLong4.class.getDeclaredField("y"));
        System.out.println(value);
        Assert.assertTrue(value > 64);
    }

    public final static class DisabledSharingValue {
        protected volatile long value = 0L;
        protected volatile long value2 = 0L;
    }

    public static class DisabledSharingRun extends Thread {
        private final int arrayIndex;
        private CountDownLatch countDownLatch;
        private boolean isOtherRead;

        public DisabledSharingRun(CountDownLatch countDownLatch, int arrayIndex, boolean isOtherRead) {
            this.countDownLatch = countDownLatch;
            this.arrayIndex = arrayIndex;
            this.isOtherRead = isOtherRead;
        }

        @Override
        public void run() {
            long i = 500L * 1000L * 1000L;
            while (0 != --i) {
                if (isOtherRead) {
                    disValues[arrayIndex].value2 = i - 1;
                } else {
                    disValues[arrayIndex].value = i;
                }
            }
            countDownLatch.countDown();
        }
    }

    // long padding避免false sharing
    // 按理说jdk7以后long padding应该被优化掉了，但是从测试结果看padding仍然起作用
    public abstract class AbstractPaddingObject{
        protected volatile long p0, p1, p2, p3, p4, p5, p6;
    }
    public class PaddingValue0 extends AbstractPaddingObject{
        public volatile long value = 0;
    }
    public class AbstractPaddingObject2 extends PaddingValue0{
        protected volatile long q0, q1, q2, q3, q4, q5, q6;
    }
    public class PaddingValue extends AbstractPaddingObject2{
        public volatile long value2;
    }

    public static class PaddingValueRun extends Thread {

        private final int arrayIndex;
        private CountDownLatch countDownLatch;
        private boolean isOtherRead;

        public PaddingValueRun(CountDownLatch countDownLatch, int arrayIndex, boolean isOtherRead) {
            this.countDownLatch = countDownLatch;
            this.arrayIndex = arrayIndex;
            this.isOtherRead = isOtherRead;
        }

        @Override
        public void run() {
            long i = 500L * 1000L * 1000L;
            while (0 != --i) {
                if (isOtherRead) {
                    padValues[arrayIndex].value2 = i - 1;
                } else {
                    padValues[arrayIndex].value = i;
                }
            }
            countDownLatch.countDown();
        }
    }

    @Contended
    public final static class ContendedValue {
        protected volatile long value = 0L;
        protected volatile long value2;
    }

    public static class ContendedValueRun extends Thread {

        private final int arrayIndex;
        private CountDownLatch countDownLatch;
        private boolean isOtherRead;

        public ContendedValueRun(CountDownLatch countDownLatch, int arrayIndex, boolean isOtherRead) {
            this.countDownLatch = countDownLatch;
            this.arrayIndex = arrayIndex;
            this.isOtherRead = isOtherRead;

        }

        @Override
        public void run() {
            long i = 500L * 1000L * 1000L;
            while (0 != --i) {
                if (isOtherRead) {
                    contValues[arrayIndex].value2 = 0;
                } else {
                    contValues[arrayIndex].value = i;
                }
            }
            countDownLatch.countDown();
        }
    }

    public final static class VolatileLong4 {
        int x;
        /**
         * @Contented注解将y移动到远离对象头部的地方,(以避免和x一起被加载到同一个缓存行)
         */
        @Contended("a")
        int y;
    }
}
