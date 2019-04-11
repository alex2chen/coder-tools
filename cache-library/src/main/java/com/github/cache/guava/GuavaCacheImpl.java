package com.github.cache.guava;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/10/25.
 */
public class GuavaCacheImpl implements Cache {
    private static final Object NULL_HOLDER = new NullHolder();
    private static final String DEFAULT_NAME = "system_cache";
    private static GuavaCacheImpl guavaCache = new GuavaCacheImpl();
    private final String name;
    private final com.google.common.cache.Cache<Object, Object> cache;
    private final boolean allowNullValues;

    private GuavaCacheImpl() {
        this(DEFAULT_NAME, CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(1, TimeUnit.HOURS).build());
    }

    public GuavaCacheImpl(String name, com.google.common.cache.Cache<Object, Object> cache) {
        this(name, cache, true);
    }

    public GuavaCacheImpl(String name, com.google.common.cache.Cache<Object, Object> cache, boolean allowNullValues) {
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(cache, "Cache must not be null");
        this.name = name;
        this.cache = cache;
        this.allowNullValues = allowNullValues;
    }

    public static GuavaCacheImpl builder() {
        return guavaCache;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.cache;
    }

    @Override
    public ValueWrapper get(Object key) {
        key = getKey(key.toString());
        Object value = this.cache.getIfPresent(key);
        return toWrapper(value);
    }

    @Override
    public <T> T get(Object key, Class<T> aClass) {
        key = getKey(key.toString());
        T value = fromStoreValue(this.cache.getIfPresent(key), aClass);
        System.out.println("get...Key:" + key);
        return value;
    }

    @Override
    public <T> T get(Object o, Callable<T> callable) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        key = getKey(key.toString());
        this.cache.put(key, toStoreValue(value));
        System.out.println("put...Key:" + key);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        PutIfAbsentCallable callable = new PutIfAbsentCallable(value);
        try {
            Object result = this.cache.get(key, callable);
            return (callable.called ? null : toWrapper(result));
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void evict(Object key) {
        this.cache.invalidate(key);
    }

    @Override
    public void clear() {
        this.cache.invalidateAll();
    }

    private Object toStoreValue(Object userValue) {
        if (this.allowNullValues && userValue == null) {
            return NULL_HOLDER;
        }
        return userValue;
    }

    private <T> T fromStoreValue(Object storeValue, Class<T> aClass) {
        if (this.allowNullValues && storeValue == NULL_HOLDER) {
            return null;
        }
        if (storeValue != null && aClass != null && !aClass.isInstance(storeValue)) {
            throw new IllegalStateException("Cached value is not of required type [" + aClass.getName() + "]: " + storeValue);
        }
        return (T) storeValue;
    }

    private String getKey(String key) {
        return name + ":" + key;
    }

    private ValueWrapper toWrapper(Object value) {
        return (value != null ? new SimpleValueWrapper(fromStoreValue(value, null)) : null);
    }

    private static class NullHolder implements Serializable {
    }

    private class PutIfAbsentCallable implements Callable<Object> {
        private final Object value;
        private boolean called;

        public PutIfAbsentCallable(Object value) {
            this.value = value;
        }

        @Override
        public Object call() throws Exception {
            this.called = true;
            return toStoreValue(this.value);
        }
    }
}
