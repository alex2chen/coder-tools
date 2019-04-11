package com.github.spring;

import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

import java.time.LocalTime;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/8/22.
 */
public class SpringRetry_test {


    public static class RemoteService {
        @Retryable(interceptor = "customIntecepter",value = {RemoteAccessException.class}, maxAttempts = 3, backoff = @Backoff(delay = 5000l, multiplier = 1))
        public void call() throws Exception {
            System.out.println(LocalTime.now() + " do something...");
            throw new RuntimeException("需重试的异常");
        }

        @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 10000l, multiplier = 1))
        public void callError(boolean isRetry) throws Exception {
            System.out.println(LocalTime.now() + " do something...");
            if (isRetry) {
                throw new RemoteAccessException("RPC调用异常");
            } else {
                throw new RuntimeException("需重试的异常");
            }
        }

        @Recover
        public void recover(Exception e) {
            System.out.println("回退0：" + e.getMessage());
        }
//    @Recover
//    public void recover(RuntimeException e) {
//       System.out.println("回退1：" + e.getMessage());
//    }
//    @Recover
//    public void recover(RemoteAccessException e) {
//       System.out.println("回退2：" + e.getMessage());
//    }
    }
}
