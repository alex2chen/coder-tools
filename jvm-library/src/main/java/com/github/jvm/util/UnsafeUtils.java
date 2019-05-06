package com.github.jvm.util;

import com.github.jvm.util.concurrent.ArraySizeUtil;
import sun.misc.Unsafe;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/4/29.
 */

public class UnsafeUtils {
    final static private Unsafe _unsafe;

    static {
        Unsafe tmpUnsafe = null;

        try {
            java.lang.reflect.Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            tmpUnsafe = (sun.misc.Unsafe) field.get(null);
        } catch (java.lang.Exception e) {
            throw new Error(e);
        }

        _unsafe = tmpUnsafe;
    }

    /**
     * 自旋
     *
     * @param values
     * @param ABASE
     * @param ASHIFT
     * @param value
     */
    public static boolean spinSet(Object[] values, int ABASE, int ASHIFT, int index, Object value) {
        final long offset = ArraySizeUtil.offset(ABASE, ASHIFT, index);
        for (; ; ) {
            final Object[] before = values;
            UnsafeUtils.unsafe().putOrderedObject(before, offset, value);
            final Object[] after = values;
            if (before == after) {
                return true;
            }
        }
    }

    public static final Unsafe unsafe() {
        return _unsafe;
    }
}
