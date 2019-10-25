package com.github.common.reflect;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: alex.chen
 * @Description:
 * @Date: 2019/10/24
 */
public class FastMethodInvoker {
    private static ConcurrentMap<Class<?>, FastClass> fastClassMap = Maps.newConcurrentMap();
    private final FastMethod fastMethod;

    protected FastMethodInvoker(FastMethod fastMethod) {
        this.fastMethod = fastMethod;
    }

    /**
     * 获取cglib生成的FastMethod，创建方法的FastMethodInvoker实例.
     */
    public static FastMethodInvoker create(Class<?> clz, String methodName, Class... parameterTypes) {
        Method method = ClassUtil.getAccessibleMethod(clz, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + clz + ']');
        }
        return build(clz, method);
    }

    /**
     * 获取cglib生成的FastMethod，创建Getter方法的FastMethodInvoker实例.
     */
    public static FastMethodInvoker createGetter(final Class<?> clz, final String propertyName) {
        Method method = ClassUtil.getGetterMethod(clz, propertyName);
        if (method == null) {
            throw new IllegalArgumentException(
                    "Could not find getter method [" + propertyName + "] on target [" + clz + ']');
        }
        return build(clz, method);
    }

    /**
     * 获取cglib生成的FastMethod，创建Setter方法的FastMethodInvoker实例.
     */
    public static FastMethodInvoker createSetter(final Class<?> clz, final String propertyName,
                                                 Class<?> parameterType) {
        Method method = ClassUtil.getSetterMethod(clz, propertyName, parameterType);
        if (method == null) {
            throw new IllegalArgumentException(
                    "Could not find getter method [" + propertyName + "] on target [" + clz + ']');
        }
        return build(clz, method);
    }

    private static FastMethodInvoker build(final Class<?> clz, Method method) {
        FastClass fastClz = fastClassMap.get(clz);
        if (fastClz == null) {
            fastClz = FastClass.create(clz);
            fastClassMap.put(clz, fastClz);
        }
        return new FastMethodInvoker(fastClz.getMethod(method));
    }

    /**
     * 调用方法
     */
    @SuppressWarnings("unchecked")
    public <T> T invoke(Object obj, Object... args) {
        try {
            return (T) fastMethod.invoke(obj, args);
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
        }
        return null;
    }
}