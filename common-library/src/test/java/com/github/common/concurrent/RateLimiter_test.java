package com.github.common.concurrent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/6/13.
 */
public class RateLimiter_test {
    @Test
    public void rateLimiter() {
        RateLimiter limiter = RateLimiter.create(2);
        System.out.println(limiter.acquire(3));
        System.out.println(JSON.toJSONString(limiter, SerializerFeature.PrettyFormat, SerializerFeature.QuoteFieldNames));
        System.out.println(limiter.acquire(2));
        System.out.println(JSON.toJSON(limiter));
        System.out.println(limiter.acquire(1));
        System.out.println(JSON.toJSON(limiter));
    }
}
