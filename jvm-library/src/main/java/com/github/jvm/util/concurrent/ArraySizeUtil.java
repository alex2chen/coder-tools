package com.github.jvm.util.concurrent;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/5/6.
 */
public class ArraySizeUtil {
    public static final int MAX_CAPACITY = 1 << 30;

    /**
     * 成倍扩容
     *
     * @param cap
     * @return
     */
    public static int tableSizeFor(final int cap) {
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
    public static long offset(final long arrayBase, final int arrayShift, final int index) {
        return ((long) index << arrayShift) + arrayBase;
    }
}
