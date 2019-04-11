package com.github.spring;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactory;

import java.util.Arrays;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/8/22.
 */
public class ManualAop_test {

    @Test
    public void aopRun() {
        AopHi proxy = (AopHi) createProxy(AopHi.class);
        proxy.hi("world");
    }

    private Object createProxy(Class target) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(target);
        proxyFactory.addAdvice(new MyHiMethodInterceptor(target));
        return proxyFactory.getProxy();
    }

    public interface AopHi {
        String hi(String msg);
    }

    private class MyHiMethodInterceptor implements MethodInterceptor {
        private Class target;

        public MyHiMethodInterceptor(Class target) {
            this.target = target;
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            System.out.println("method:" + invocation.getMethod().getName());
            System.out.println("args:" + Arrays.toString(invocation.getArguments()));
            return null;
        }
    }
}
