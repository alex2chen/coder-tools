package com.github.common.concurrent;

import com.github.jvm.util.UnsafeUtils;

import java.util.Arrays;

/**
 * @Author: alex
 * @Description: 原子性操作多个int，利用空间换时间，使用Cache Line
 * 比如：count=4，array.length=96,刚好4个cache Line（24个int为一个Cache Line），get/set都会命中cache
 * @Date: created in 2018/5/14.
 */
public class AtomicMuiltInteger {
    private final int[] array;
    private final int count;
    private static final int ABASE;
    private static final int ASHIFT;

    static {
        try {
            ABASE = UnsafeUtils.unsafe().arrayBaseOffset(int[].class);
            int scale = UnsafeUtils.unsafe().arrayIndexScale(int[].class);
            if ((scale & (scale - 1)) != 0) {
                throw new Error("array index scale not a power of two");
            }
            ASHIFT = 31 - Integer.numberOfLeadingZeros(scale) + 4;
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /**
     * 原子性操作多个int，初始值为0
     *
     * @param count 数量
     */
    public AtomicMuiltInteger(int count) {
        this(count, 0);
    }

    /**
     * 原子性操作多个int
     *
     * @param count        数量
     * @param initialValue 初始值
     */
    public AtomicMuiltInteger(int count, int initialValue) {
        if (count < 1) {// 必须大于0
            throw new IllegalArgumentException("Illegal count: " + count);
        }
        this.count = count;
        array = new int[(count + 2) << 4];
        Arrays.fill(array, initialValue);
    }

    /**
     * 递增并获取该位置的值
     *
     * @param index
     * @return
     */
    public int incrementAndGet(int index) {
        return UnsafeUtils.unsafe().getAndAddInt(array, offset(index), 1) + 1;
    }

    /**
     * 增加并获取该位置的值
     *
     * @param index
     * @param delta
     * @return
     */
    public int addAndGet(int index, int delta) {
        return UnsafeUtils.unsafe().getAndAddInt(array, offset(index), delta) + delta;
    }

    /**
     * 某个位置的值
     *
     * @param index
     * @return
     */
    public int get(int index) {
        return UnsafeUtils.unsafe().getIntVolatile(array, offset(index));
    }

    /**
     * 所有位置的和
     *
     * @return
     */
    public int sum() {
        int sum = 0;

        for (int i = 0; i < count; i++) {
            sum += UnsafeUtils.unsafe().getIntVolatile(array, offset(i));
        }

        return sum;
    }

    /**
     * 重置某位置的值为0
     *
     * @param index
     */
    public void reset(int index) {
        set(index, 0);
    }

    /**
     * 重置所有位置的值为0
     */
    public void resetAll() {
        for (int i = 0; i < count; i++) {
            set(i, 0);
        }
    }

    /**
     * 设置某位置的值
     *
     * @param index
     * @param value
     */
    public void set(int index, int value) {
        UnsafeUtils.unsafe().putOrderedInt(array, offset(index), value);
    }

    private static final long offset(int index) {
        return (((long) index + 1L) << ASHIFT) + ABASE;
    }
}