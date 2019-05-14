package com.github.common.concurrent;

import io.netty.util.concurrent.FastThreadLocalThread;
import io.netty.util.internal.InternalThreadLocalMap;

import java.util.function.Supplier;

/**
 * @Author: alex
 * @Description: 当线程为AttachmentThread、FastThreadLocalThread时能达到ThreadLocal 1.5x的性能；
 * 优化策略：
 * 当线程不是上述两种，并且线程id小于16k时，可获得跟ThreadLocal相同的性能；
 * 当线程不满足上述两种情况，将只能达到ThreadLocal 0.7x的性能；
 * @Date: created in 2018/5/6.
 */
public class AttachThreadUtil {
    private static ThreadLocal<IntToObjectMap<Object>> SLOW_THREAD_LOCAL_HOLDER = ThreadLocal.withInitial(() -> new IntToObjectMap<>());
    private static final ConcurrentIntToObjectMap<IntToObjectMap<Object>> threadAttachmentMap = new ConcurrentIntToObjectMap<>(1024);

    public static <T> T getOrUpdate(int index, Supplier<T> producer) {
        Thread currentThread = Thread.currentThread();
        if (currentThread instanceof FastThreadLocalThread) {// 很快，1.5x ThreadLocal性能
            FastThreadLocalThread fastThread = (FastThreadLocalThread) currentThread;
            InternalThreadLocalMap threadLocalMap = fastThread.threadLocalMap();
            if (threadLocalMap == null) {
                // 会自动赋值的
                threadLocalMap = InternalThreadLocalMap.get();
            }
            Object obj = threadLocalMap.indexedVariable(index);
            if (obj != InternalThreadLocalMap.UNSET) {
                return (T) obj;
            }
            obj = producer.get();
            threadLocalMap.setIndexedVariable(index, obj);
            return (T) obj;
        }
        if (currentThread instanceof AttachThread) {// 很快，1.5x ThreadLocal性能
            AttachThread attachmentThread = (AttachThread) currentThread;
            T result = attachmentThread.get(index);

            if (result != null) {
                return result;
            } else {
                return attachmentThread.getOrUpdate(index, producer);
            }
        }
        long currentThreadId = currentThread.getId();
        if (currentThreadId < 1024L * 16L) {// 跟直接使用ThreadLocal相同的性能
            IntToObjectMap<Object> varMap = threadAttachmentMap.get((int) currentThreadId);
            if (varMap == null) {
                varMap = threadAttachmentMap.getOrUpdate((int) currentThreadId, () -> new IntToObjectMap<>());
            }
            Object obj = varMap.get(index);
            if (obj != null) {
                return (T) obj;
            } else {
                return (T) varMap.getOrUpdate(index, (Supplier<Object>) producer);
            }
        }
        // 很慢，0.7x ThreadLocal性能
        IntToObjectMap<Object> varMap = SLOW_THREAD_LOCAL_HOLDER.get();
        Object obj = varMap.get(index);
        if (obj != null) {
            return (T) obj;
        } else {
            return (T) varMap.getOrUpdate(index, (Supplier<Object>) producer);
        }
    }

    public static <T> void put(int index, T value) {
        Thread currentThread = Thread.currentThread();
        if (currentThread instanceof FastThreadLocalThread) {// 很快，1.5x ThreadLocal性能
            FastThreadLocalThread fastThread = (FastThreadLocalThread) currentThread;
            InternalThreadLocalMap threadLocalMap = fastThread.threadLocalMap();
            if (threadLocalMap == null) {
                // 会自动赋值的
                threadLocalMap = InternalThreadLocalMap.get();
            }
            threadLocalMap.setIndexedVariable(index, value);
            return;
        }
        if (currentThread instanceof AttachThread) {// 很快，1.5x ThreadLocal性能
            AttachThread attachmentThread = (AttachThread) currentThread;
            attachmentThread.put(index, value);
            return;

        }
        long currentThreadId = currentThread.getId();
        if (currentThreadId < 1024L * 16L) {// 跟直接使用ThreadLocal相同的性能
            IntToObjectMap<Object> varMap = threadAttachmentMap.get((int) currentThreadId);
            if (varMap == null) {
                varMap = threadAttachmentMap.getOrUpdate((int) currentThreadId, () -> new IntToObjectMap<>());
            }
            varMap.put(index, value);
            return;
        }
        // 很慢，0.7x ThreadLocal性能
        IntToObjectMap<Object> varMap = SLOW_THREAD_LOCAL_HOLDER.get();
        varMap.put(index, value);
    }
}
