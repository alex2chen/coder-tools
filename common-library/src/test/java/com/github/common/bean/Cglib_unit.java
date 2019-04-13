package com.github.common.bean;


import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @author alex.chen
 * @Description
 * @date 2016/4/30
 * @See InvocationHandler AbstractInvocationHandler
 */
public class Cglib_unit {
    @Test
    public void proxy() {
        //CGLIB debugging enabled
        //System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "target\\class");

        CglibProxy cglibProxy = new CglibProxy();
        SmsSubject smsSubject = (SmsSubject) cglibProxy.getInstance(new SmsSubject());
        smsSubject.sent("abc");
        smsSubject.accept();
    }

    public static class SmsSubject {
        public void sent(String msg) {
            System.out.println("sent msg:" + msg);
        }

        public void accept() {
            System.out.println("accept msg:123456");
        }
    }

    public static class CglibProxy implements MethodInterceptor {
        private Object target;

        public Object getInstance(Object target) {
            this.target = target;
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(this.target.getClass());
            //回调方法
            enhancer.setCallback(this);
            //创建代理对象
            return enhancer.create();
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            System.out.println("invoke start");
            Object object = methodProxy.invokeSuper(o, objects);
            System.out.println("invoke end");
            return object;
        }
    }
}
