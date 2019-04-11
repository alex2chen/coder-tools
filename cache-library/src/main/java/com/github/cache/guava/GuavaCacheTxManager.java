package com.github.cache.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: alex
 * @Description: 支持事务回滚
 * @Date: created in 2017/10/25.
 */
public class GuavaCacheTxManager extends AbstractTransactionSupportingCacheManager {
    private final ConcurrentMap<String, Cache> cacheMap = Maps.newConcurrentMap();
    private Map<String, CacheBuilder> builderMap = Maps.newHashMap();
    private boolean dynamic = true;
    private boolean allowNullValues = true;

    @Override
    protected Collection<? extends Cache> loadCaches() {
        Collection<Cache> values = cacheMap.values();
        return values;
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = this.cacheMap.get(name);
        if (cache == null && this.dynamic) {
            synchronized (this.cacheMap) {
                cache = this.cacheMap.get(name);
                if (cache == null && this.builderMap.containsKey(name)) {
                    CacheBuilder builder = this.builderMap.get(name);
                    cache = createGuavaCache(name, builder);
                    this.cacheMap.put(name, cache);
                }
            }
        }
        return cache;
    }

    protected Cache createGuavaCache(String name, CacheBuilder builder) {
        com.google.common.cache.Cache<Object, Object> cache = null;
        if (builder == null) {
            cache = CacheBuilder.newBuilder().build();
        } else {
            cache = builder.build();
        }
        return new GuavaCacheImpl(name, cache, isAllowNullValues());
    }


    public boolean isAllowNullValues() {
        return this.allowNullValues;
    }

    public void setConfigMap(Map<String, CacheBuilder> configMap) {
        this.builderMap = configMap;
    }
}
