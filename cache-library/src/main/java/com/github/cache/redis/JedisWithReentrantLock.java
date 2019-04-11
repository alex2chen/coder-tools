package com.github.cache.redis;

import com.google.common.collect.Maps;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/4/11.
 */
public class JedisWithReentrantLock {
    private Jedis jedis;
    /**
     * 当前线程的锁及计数
     */
    private ThreadLocal<Map<String, Integer>> lockers = new ThreadLocal<>();

    public JedisWithReentrantLock(Jedis jedis) {
        this.jedis = jedis;
    }

    private boolean set(String key) {
        return jedis.set(key, "", "nx", "ex", 5L) != null;
    }

    private void del(String key) {
        jedis.del(key);
    }

    private Map<String, Integer> getLockers() {
        Map<String, Integer> refs = lockers.get();
        if (refs != null) {
            return refs;
        }
        lockers.set(Maps.newHashMap());
        return lockers.get();
    }

    public boolean lock(String key) {
        Map<String, Integer> refs = getLockers();
        Integer refCount = refs.get(key);
        if (refCount != null) {
            refs.put(key, refCount + 1);
            return true;
        }
        if (!this.set(key)) {
            return false;
        }
        refs.put(key, 1);
        return true;
    }

    public boolean unlock(String key) {
        Map<String, Integer> refs = getLockers();
        Integer refCount = refs.get(key);
        if (refCount == null) {
            return false;
        }
        refCount -= 1;
        if (refCount > 0) {
            refs.put(key, refCount);
        } else {
            refs.remove(key);
            this.del(key);
        }
        return true;
    }
}
