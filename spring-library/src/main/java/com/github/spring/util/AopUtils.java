package com.github.spring.util;

import com.google.common.base.Throwables;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: alex.chen
 * @Description:
 * @Date: 2019/10/26
 */
public class AopUtils {
    /**
     * 代理 class 的名称
     */
    private static final List<String> PROXY_CLASS_NAMES = Arrays.asList("net.sf.cglib.proxy.Factory"
            // cglib
            , "org.springframework.cglib.proxy.Factory"
            , "javassist.util.proxy.ProxyObject"
            // javassist
            , "org.apache.ibatis.javassist.util.proxy.ProxyObject");

    public static boolean isProxy(Class<?> clazz) {
        if (clazz != null) {
            for (Class<?> cls : clazz.getInterfaces()) {
                if (PROXY_CLASS_NAMES.contains(cls.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取源目标对象
     *
     * @param proxy ignore
     * @param <T>   ignore
     * @return ignore
     */
    public static <T> T getTargetObject(T proxy) {
        if (!isProxy(proxy.getClass())) {
            return proxy;
        }
        try {
            if (org.springframework.aop.support.AopUtils.isJdkDynamicProxy(proxy)) {
                return getJdkDynamicProxyTargetObject(proxy);
            } else if (org.springframework.aop.support.AopUtils.isCglibProxy(proxy)) {
                return getCglibProxyTargetObject(proxy);
            } else {
                return proxy;
            }
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
            return null;
        }
    }

    /**
     * 获取Cglib源目标对象
     *
     * @param proxy ignore
     * @param <T>   ignore
     * @return ignore
     */
    private static <T> T getCglibProxyTargetObject(T proxy) throws Exception {
        Field cglibField = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        cglibField.setAccessible(true);
        Object dynamicAdvisedInterceptor = cglibField.get(proxy);
        Field advised = ReflectionUtils.findField(dynamicAdvisedInterceptor.getClass(), "advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
        return (T) target;
    }

    /**
     * 获取JdkDynamic源目标对象
     *
     * @param proxy ignore
     * @param <T>   ignore
     * @return ignore
     */
    private static <T> T getJdkDynamicProxyTargetObject(T proxy) throws Exception {
        Field jdkDynamicField = proxy.getClass().getSuperclass().getDeclaredField("jdkDynamicField");
        jdkDynamicField.setAccessible(true);
        AopProxy aopProxy = (AopProxy) jdkDynamicField.get(proxy);
        Field advised = ReflectionUtils.findField(aopProxy.getClass(), "advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
        return (T) target;
    }
}
