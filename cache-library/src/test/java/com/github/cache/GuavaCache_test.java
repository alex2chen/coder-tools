package com.github.cache;

import com.github.cache.guava.GuavaCacheImpl;
import com.github.cache.guava.GuavaCacheTxManager;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.springframework.cache.Cache;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/12/7.
 */
public class GuavaCache_test {
    private CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder().recordStats().maximumSize(2).expireAfterWrite(3, TimeUnit.SECONDS);

    @Test
    public void maxSize() {
        Cache cache = new GuavaCacheImpl("batch", cacheBuilder.build());
        cache.put("user_id:1", "20");
        cache.put("user_id:2", "20");
        cache.put("user_id:3", "20");

        System.out.println(cache.get("user_id:1", String.class));
        System.out.println(cache.get("user_id:2", String.class));
        System.out.println(cache.get("user_id:3", String.class));

        Cache cache2 = new GuavaCacheImpl("batch2", cacheBuilder.build());
        cache2.put("user_id:1", "20");

        System.out.println(cache2.get("user_id:1", String.class));
        System.out.println(cache2.get("user_id:2", String.class));
        System.out.println(cache2.get("user_id:3", String.class));

        System.out.println(cache.get("user_id:3", String.class));
    }

    @Test
    public void putVO() throws InterruptedException {
        Cache cache = new GuavaCacheImpl("exec", cacheBuilder.build());
        cache.put("id-1", new ExecResult(20, 2));
        System.out.println(cache.get("id-1", ExecResult.class));
    }

    @Test
    public void evict() {
        Cache cache = new GuavaCacheImpl("exec", cacheBuilder.build());
        cache.put("id-1", new ExecResult(20, 2));
        //cache.evict("id-1");//TODO:懒加载啦
        cache.put("id-1", null);
        System.out.println(cache.get("id-1", ExecResult.class));
    }

    @Test
    public void exprire() throws InterruptedException {
        Cache cache = new GuavaCacheImpl("batch", cacheBuilder.build());
        cache.put("user_id:1", "20");
        System.out.println(cache.get("user_id:1", String.class));
        Thread.sleep(5000L);
        System.out.println(cache.get("user_id:1", String.class));
        cache.put("user_id:2", "20");
        System.out.println(cache.get("user_id:2", String.class));
        System.out.println(cache);
    }

    @Test
    public void monitor() {
        cacheBuilder.recordStats();
        Cache cache = new GuavaCacheImpl("batch", cacheBuilder.build());
        cache.put("user_id:1", "20");
        //System.out.println(com.google.jvm.cache.Cache);
    }

    @Test
    public void cacheManage() {
        GuavaCacheTxManager cacheManager = new GuavaCacheTxManager();
        Map<String, CacheBuilder> builderMap = Maps.newHashMap();
        builderMap.put("reptileKeyValue", cacheBuilder);
        cacheManager.setConfigMap(builderMap);
        cacheManager.getCache("batch").put("name", "alex");

    }

    public static class ExecResult {
        private int commit;
        private int count;

        public ExecResult(int commit, int count) {
            this.commit = commit;
            this.count = count;
        }

        public int getCommit() {
            return commit;
        }

        public void setCommit(int commit) {
            this.commit = commit;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "ExecResult{" +
                    "commit=" + commit +
                    ", count=" + count +
                    '}';
        }
    }
}
