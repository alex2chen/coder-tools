package com.github.common.concurrent;

import io.netty.util.internal.InternalThreadLocalMap;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * @Author: alex
 * @Description: 并发环境下是 AtomicInteger 性能的10倍
 * 实现原理为每个线程持有100个数字，线程内简单递增，数字整体上不是简单递增的，仅适用于特定场景
 * @Date: created in 2018/5/14.
 */
public class ConcurrentAtomicInteger {
    private static final int BATCH_COUNT = 100;
    private final AtomicInteger rootCounter;
    private final Supplier<CellCounter> supplier;
    private final int attachmentIndex = InternalThreadLocalMap.nextVariableIndex();
    public ConcurrentAtomicInteger() {
        this(0);
    }

    public ConcurrentAtomicInteger(int initialValue) {
        this(initialValue, false);
    }

    public ConcurrentAtomicInteger(int initialValue, boolean nonNegative) {
        this.rootCounter = new AtomicInteger(initialValue);
        this.supplier = () -> new CellCounter(rootCounter, nonNegative);
    }
    public int next() {// 性能受制于AttachmentThreadUtils.getOrUpdate
        CellCounter cellCounter = AttachThreadUtil.getOrUpdate(attachmentIndex, supplier);
        return cellCounter.next();
    }

    private static class CellCounter {
        private final AtomicInteger rootCounter;
        private final boolean nonNegative;

        private int base = 0;
        private int counter = 0;

        CellCounter(AtomicInteger rootCounter, boolean nonNegative) {
            this.rootCounter = rootCounter;
            this.base = rootCounter.getAndAdd(BATCH_COUNT);
            this.nonNegative = nonNegative;
        }
        int next() {
            int value = base + counter++;

            if (counter == BATCH_COUNT) {
                base = rootCounter.getAndAdd(BATCH_COUNT);
                counter = 0;

                if (nonNegative) {
                    int max = base + BATCH_COUNT;
                    if (base < 0 || max < 0) {
                        rootCounter.compareAndSet(max, 0);
                        base = rootCounter.getAndAdd(BATCH_COUNT);
                        counter = 0;
                    }
                }
            }

            return value;
        }
    }
}
