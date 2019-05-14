package com.github.common.concurrent;

import com.github.jvm.util.concurrent.ArraySizeUtil;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * @Author: alex
 * @Description: 不保证并发，仅适用于key值比较少的情况，不能超过512k个，实现原理ConcurrentIntToObjectMap类似
 * @Date: created in 2019/5/14.
 */
public class IntToObjectMap<T> {
    public static final Object NOT_FOUND = new Object();
    public static final int MAXIMUM_CAPACITY = 1024 * 512;

    private Object[] array;

    public IntToObjectMap() {
        this(16);
    }

    public IntToObjectMap(int initialCapacity) {
        if (initialCapacity < 2) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (initialCapacity > MAXIMUM_CAPACITY) {
            throw new IndexOutOfBoundsException("Illegal initial capacity: " + initialCapacity);
        }
        ensureCapacity(initialCapacity);
    }

    public void clear() {
        if (array == null) {
            return;
        }
        Arrays.fill(array, NOT_FOUND);
    }

    public void put(int key, T value) {
        if (key < 0) {
            throw new IllegalArgumentException("Illegal key: " + key);
        }
        if (key >= MAXIMUM_CAPACITY) {
            throw new IndexOutOfBoundsException("Illegal key: " + key);
        }
        ensureCapacity(key + 1);
        array[key] = value;
    }

    public boolean contains(int key) {
        if (key < 0) {
            return false;
        }
        Object[] finalArray = array;
        if (key >= finalArray.length) {
            return false;
        }
        Object value = finalArray[key];
        if (value == NOT_FOUND) {
            return false;
        } else {
            return true;
        }
    }

    public T get(int key) {
        if (key < 0) {
            throw new IllegalArgumentException("Illegal key: " + key);
        }
        Object[] finalArray = array;
        if (key >= finalArray.length) {
            return null;
        }
        Object value = finalArray[key];
        if (value != NOT_FOUND) {
            return (T) value;
        } else {
            return null;
        }
    }

    public T getOrUpdate(int key, Supplier<T> producer) {
        if (key < 0) {
            throw new IllegalArgumentException("Illegal key: " + key);
        }
        final Object[] finalArray = array;
        Object value;
        if (key >= finalArray.length) {
            value = NOT_FOUND;
        } else {
            value = finalArray[key];
        }
        if (value != NOT_FOUND) {
            return (T) value;
        }
        value = producer.get();
        put(key, (T) value);

        return (T) value;
    }

    private void ensureCapacity(int capacity) {
        Object[] finalArray = array;
        if (finalArray != null && finalArray.length >= capacity) {
            return;
        }
        int newCapacity = ArraySizeUtil.tableSizeFor(capacity);
        if (newCapacity > MAXIMUM_CAPACITY) {
            throw new IndexOutOfBoundsException("" + newCapacity);
        }
        Object[] objs = new Object[newCapacity];
        Arrays.fill(objs, NOT_FOUND);

        if (finalArray != null) {
            System.arraycopy(finalArray, 0, objs, 0, finalArray.length);
        }
        array = objs;
    }
}
