package com.github.spring;

import com.github.spring.util.AopUtils;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: alex.chen
 * @Description:
 * @Date: 2019/12/11
 */
@Configuration
public class DefaultAdvisorAutoProxyCreator_test {
    @Test
    public void print_test() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DefaultAdvisorAutoProxyCreator_test.class);
        UserService userService = applicationContext.getBean(UserService.class);
        System.out.println(userService.getClass());
        System.out.println(AopUtils.isProxy(userService.getClass()));
        System.out.println(AopUtils.isJdkProxy(userService.getClass()));
        userService.print();
    }

    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }

    @Bean
    public Advice myMethodInterceptor() {
        return new MyMethodInterceptor();
    }

    @Bean
    public NameMatchMethodPointcutAdvisor nameMatchMethodPointcutAdvisor() {
        NameMatchMethodPointcutAdvisor nameMatchMethodPointcutAdvisor = new NameMatchMethodPointcutAdvisor();
        nameMatchMethodPointcutAdvisor.setMappedName("pri*");
        nameMatchMethodPointcutAdvisor.setAdvice(myMethodInterceptor());
        return nameMatchMethodPointcutAdvisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }

    public interface UserService {
        void print();
    }

        public class UserServiceImpl implements UserService {
//    public class UserServiceImpl {
        public void print() {
            System.out.println(getClass() + "#print");
        }
    }

    public class MyMethodInterceptor implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            System.out.println(getClass() + "调用方法前");
            Object ret = invocation.proceed();
            System.out.println(getClass() + "调用方法后");
            return ret;
        }
    }
}
