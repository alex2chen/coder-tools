package com.github.spring.retry.config;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/4/12.
 */
@Component
public class CustomIntecepter implements MethodInterceptor {
    private RetryTemplate retryTemplate;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        RetryTemplate template = new RetryTemplate();
        RetryOperationsInterceptor interceptor = RetryInterceptorBuilder.stateless()
                .retryOperations(template)
                //.label(retryable.label())
                //.recoverer(getRecoverer(target, method))
                .build();
        interceptor.invoke(invocation);
        System.out.println("CustomIntecepter:" + invocation.getMethod().getName());
        //retryTemplate.execute(()->invocation.proceed());
        return null;
    }

}
