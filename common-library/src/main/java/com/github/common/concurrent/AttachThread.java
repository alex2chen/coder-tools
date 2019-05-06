package com.github.common.concurrent;

import com.github.jvm.util.concurrent.ArraySizeUtil;
import io.netty.util.concurrent.FastThreadLocalThread;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/5/6.
 */
public class AttachThread extends FastThreadLocalThread {
    public static final Object NOT_FOUND = new Object();
    public static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * 线程本地变量
     */
    private Object[] objs;

    public AttachThread() {
        super();
    }

    public AttachThread(Runnable runnable, String name) {
        super(runnable, name);
    }

    public AttachThread(Runnable runnable) {
        super(runnable);
    }

    public AttachThread(String name) {
        super(name);
    }

    public AttachThread(ThreadGroup threadGroup, Runnable runnable, String name) {
        super(threadGroup, runnable, name);
    }

    /**
     * 存储线程变量
     *
     * @param index 需要从AttachmentThreadUtils.nextVarIndex()获取到
     * @param value
     */
    public void put(int index, Object value) {
        if (index < 0) {
            throw new IllegalArgumentException("Illegal index: " + index);
        }

        if (index >= MAXIMUM_CAPACITY) {
            throw new IndexOutOfBoundsException("Illegal index: " + index);
        }

        ensureCapacity(index + 1);

        objs[index] = value;
    }

    /**
     * 获取线程变量
     *
     * @param index 需要从 AttachmentThreadUtils.nextVarIndex()获取到
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Illegal index: " + index);
        }

        if (objs == null || index >= objs.length) {
            return null;
        }

        Object value = objs[index];

        if (value != NOT_FOUND) {
            return (T) value;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrUpdate(int index, Supplier<T> producer) {
        if (index < 0) {
            throw new IllegalArgumentException("Illegal index: " + index);
        }

        Object value;
        if (objs == null || index >= objs.length) {
            value = NOT_FOUND;
        } else {
            value = objs[index];
        }

        if (value != NOT_FOUND) {
            return (T) value;
        }

        value = producer.get();
        put(index, (T) value);

        return (T) value;
    }

    private void ensureCapacity(int capacity) {
        if (objs != null && objs.length >= capacity) {
            return;
        }

        int newCapacity = ArraySizeUtil.tableSizeFor(capacity);

        if (newCapacity > MAXIMUM_CAPACITY) {
            throw new IndexOutOfBoundsException("" + newCapacity);
        }

        Object[] newArray = new Object[newCapacity];
        Arrays.fill(newArray, NOT_FOUND);

        if (objs != null) {
            System.arraycopy(objs, 0, newArray, 0, objs.length);
        }
        this.objs = newArray;
    }
}
