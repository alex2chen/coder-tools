package com.github.jvm.util.concurrent;

import com.github.jvm.util.UnsafeUtils;

import java.lang.reflect.Field;
import java.util.RandomAccess;

/**
 * @Author: alex
 * @Description: 仅支持get/set，不支持remove
 * @Date: created in 2018/4/29.
 */
public class ConcurrentArrayList<T> implements RandomAccess {
    public static final int MAX_CAPACITY = 1 << 30;
    private static final long SIZE_OFFSET;
    private static final int ABASE;
    private static final int ASHIFT;
    private volatile Object[] values;
    //unsafe atomic operate
    private volatile int size;

    static {
        try {
            Field field = ConcurrentArrayList.class.getDeclaredField("size");
            SIZE_OFFSET = UnsafeUtils.unsafe().objectFieldOffset(field);
            ABASE = UnsafeUtils.unsafe().arrayBaseOffset(Object[].class);
            int scale = UnsafeUtils.unsafe().arrayIndexScale(Object[].class);
            if ((scale & (scale - 1)) != 0) {
                throw new Error("array index scale not a power of two");
            }
            ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
        } catch (Throwable e) {
            throw new Error(e);
        }
    }

    public T get(int index) {
        return (T) UnsafeUtils.unsafe().getObjectVolatile(values, offset(ABASE, ASHIFT, index));
    }

    public void add(T value) {
        int index = insertIndex();
        set(index, value);
    }

    private int insertIndex() {
        int index = UnsafeUtils.unsafe().getAndAddInt(this, SIZE_OFFSET, 1);
        ensureCapacity(index + 1);
        return index;
    }

    public void set(int index, T value) {
        final long offset = offset(ABASE, ASHIFT, index);
        for (; ; ) {// like cas
            final Object[] before = values;
            UnsafeUtils.unsafe().putOrderedObject(before, offset, value);
            final Object[] after = values;

            if (before == after) {
                return;
            }
        }
    }

    public ConcurrentArrayList() {
        this(16);
    }

    public ConcurrentArrayList(int initialCapacity) {
        if (initialCapacity > MAX_CAPACITY) {
            throw new IndexOutOfBoundsException("Illegal initial capacity: " + initialCapacity);
        }
        ensureCapacity(initialCapacity);
    }

    private void ensureCapacity(int capacity) {
        Object[] theArray = values;
        if (theArray != null && theArray.length >= capacity) {
            return;
        }
        synchronized (this) {
            Object[] finalArray = values;
            if (finalArray != null && finalArray.length >= capacity) {
                return;
            }
            int newCapacity = tableSizeFor(capacity);
            if (newCapacity > MAX_CAPACITY) {
                throw new IndexOutOfBoundsException("" + newCapacity);
            }

            Object[] objs = new Object[newCapacity];

            if (finalArray != null) {
                System.arraycopy(finalArray, 0, objs, 0, finalArray.length);
            }
            values = objs;
        }
    }

    /**
     * 成倍扩容
     *
     * @param cap
     * @return
     */
    public int tableSizeFor(final int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAX_CAPACITY) ? MAX_CAPACITY : n + 1;
    }

    /**
     * 获取offset
     *
     * @param arrayBase
     * @param arrayShift
     * @param index
     * @return
     */
    public long offset(final long arrayBase, final int arrayShift, final int index) {
        return ((long) index << arrayShift) + arrayBase;
    }

    public int size() {
        return size;
    }

    public void clear() {
        size = 0;
    }

}
