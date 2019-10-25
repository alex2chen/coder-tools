package com.github.jvm.util.concurrent;

import com.github.jvm.util.UnsafeUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.RandomAccess;

/**
 * @Author: alex
 * @Description: 无锁并发 List 实现，比CopyOnWriteArrayList 的写入开销低，仅支持get/set，O(1) vs O(n)，不支持remove
 * @Date: created in 2018/4/29.
 */
public class ConcurrentArrayList<T> implements RandomAccess {

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
        return (T) UnsafeUtils.unsafe().getObjectVolatile(values, ArraySizeUtil.offset(ABASE, ASHIFT, index));
    }

    public void add(T value) {
        int index = insertIndex();
        set(index, value);
    }
    public void addAll(Collection<T> collection) {
        if (collection == null) {
            return;
        }
        ensureCapacity(size + collection.size());
        for (T t : collection) {
            add(t);
        }
    }
    private int insertIndex() {
        int index = UnsafeUtils.unsafe().getAndAddInt(this, SIZE_OFFSET, 1);
        ensureCapacity(index + 1);
        return index;
    }

    public void set(int index, T value) {
        UnsafeUtils.spinSet(values,ABASE, ASHIFT, index,value);
    }

    public ConcurrentArrayList() {
        this(16);
    }

    public ConcurrentArrayList(int initialCapacity) {
        if (initialCapacity > ArraySizeUtil.MAX_CAPACITY) {
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
            int newCapacity = ArraySizeUtil.tableSizeFor(capacity);
            if (newCapacity > ArraySizeUtil.MAX_CAPACITY) {
                throw new IndexOutOfBoundsException("" + newCapacity);
            }

            Object[] objs = new Object[newCapacity];

            if (finalArray != null) {
                System.arraycopy(finalArray, 0, objs, 0, finalArray.length);
            }
            values = objs;
        }
    }



    public int size() {
        return size;
    }

    public void clear() {
        size = 0;
    }

}
