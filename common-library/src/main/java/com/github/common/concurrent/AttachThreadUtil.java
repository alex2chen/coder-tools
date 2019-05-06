package com.github.common.concurrent;

/**
 * @Author: alex
 * @Description: 当线程为AttachmentThread、FastThreadLocalThread时能达到ThreadLocal 1.5x的性能；
 * 优化策略：
 * 当线程不是上述两种，并且线程id小于16k时，可获得跟ThreadLocal相同的性能；
 * 当线程不满足上述两种情况，将只能达到ThreadLocal 0.7x的性能；
 * @Date: created in 2018/5/6.
 */
public class AttachThreadUtil {
//    private static ThreadLocal<IntToObjectArrayMap<Object>> SLOW_THREAD_LOCAL_HOLDER = ThreadLocal.withInitial(() -> new IntToObjectArrayMap<>());
//    private static final ConcurrentIntToObjectArrayMap threadAttachmentMap = new ConcurrentIntToObjectMap<IntToObjectArrayMap<Object>>(1024);

}
