package com.github.spring;

import com.github.spring.util.AopUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.cglib.core.DebuggingClassWriter;

import java.util.Arrays;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/8/22.
 */
public class Aop_test {
    @BeforeClass
    public static void init(){
        //cglib
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "target\\gen-class");
        /**
         * jdk
         * 指定 -Dsun.misc.ProxyGenerator.saveGeneratedFiles=true，AccessController.doPrivileged只会访问命令行参数
         * @see sun.nio.fs.WindowsPath output path 默认为当前项目的根目录（D:\coder-tools\spring-library\com\sun\proxy\$Proxy1.class）
         * @see sun.misc.ProxyGenerator
         */
    }
    @Test
    public void manualCreateJdkAop() {

        AopHi proxy = (AopHi) createProxy(AopHi.class);
        proxy.hi("world");
        System.out.println(AopUtils.isProxy(proxy.getClass()));
    }

    @Test
    public void manualCreateCglibAop() {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(new AopHiImpl());
//        proxyFactory.setProxyTargetClass(true);
//        proxyFactory.setOptimize(true);
        proxyFactory.addAdvice(new MyHiMethodInterceptor());
        AopHi proxy = (AopHi) proxyFactory.getProxy();
        proxy.hi("world3");
        System.out.println(AopUtils.isProxy(proxy.getClass()));
    }

    private Object createProxy(Class target) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(target);
        proxyFactory.addAdvice(new MyHiMethodInterceptor());
        return proxyFactory.getProxy();
    }

    public interface AopHi {
        String hi(String msg);
    }

    public class AopHiImpl implements AopHi {
        @Override
        public String hi(String msg) {
            return msg;
        }
    }

    private class MyHiMethodInterceptor implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            System.out.println("target:" + invocation.getThis());
            System.out.println("method:" + invocation.getMethod().getName());
            System.out.println("args:" + Arrays.toString(invocation.getArguments()));
            return null;
        }
    }
}
