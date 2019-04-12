package com.github.spring.retry.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Created by fei.chen on 2018/8/22.
 */
@Configuration
@EnableRetry
public class RetryConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(RetryConfiguration.class);

    @Bean
    public RetryListener retryListener1() {
        return new CustomRetryListenerImpl();
    }

    public class CustomRetryListenerImpl implements RetryListener {

        @Override
        public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
            System.out.println("retry.open.");
            return true;
        }

        @Override
        public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback,
                                                   Throwable throwable) {
            System.out.println("retry.close.");
        }

        @Override
        public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
                                                     Throwable throwable) {
//            if (throwable instanceof RemoteAccessException){
//                logger.error("不重试的errror:" + throwable.getMessage());
//                context.setExhaustedOnly();
//            }
            logger.error("retry.error:" + context.getRetryCount());
        }
    }
}
