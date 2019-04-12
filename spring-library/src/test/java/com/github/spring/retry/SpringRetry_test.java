package com.github.spring.retry;

import com.github.spring.util.SpringConfigReader;
import com.github.spring.retry.config.RemoteService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/8/22.
 */
public class SpringRetry_test extends SpringConfigReader {
    private ApplicationContext applicationContext;
    private RemoteService remoteService;

    @Before
    public void loadApplicationContext() {
        applicationContext = loadAnnotationConfigSpringContext("com.github.spring.retry.config");
        remoteService = applicationContext.getBean(RemoteService.class);
    }

    @Test
    public void call() throws Exception {
        remoteService.call();
    }

    @Test
    public void callError() throws Exception {
        remoteService.callError(true);
        //remoteService.callError(false);
    }
}
