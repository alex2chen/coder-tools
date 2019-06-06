package com.github.common.concurrent;

import com.github.common.base.map.FastMap;
import com.github.common.base.map.KeyNamespace;
import com.github.common.base.map.MapKey;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * @Author: alex
 * @Description: 比concurrentMap慢出很多，慎用
 * @Date: created in 2018/6/6.
 */
public class ConcurrentFastMap<T> implements FastMap<T> {
    private final KeyNamespace namespace;
    private final int size;
    private final AtomicReferenceArray<T> values;
    private volatile ConcurrentHashMap<Integer, T> extMap;

    public ConcurrentFastMap(KeyNamespace namespace, int size) {
        this.namespace = namespace;
        this.size = size;
        this.values = new AtomicReferenceArray(size);
    }

    @Override
    public T put(String key, T value) {
        MapKey newKey = new MapKey(namespace, key);
        return put(newKey, value);
    }

    @Override
    public synchronized T put(MapKey key, T value) {
        int id = key.getId();
        return id >= this.size ? this.getExtMap().put(id, value) : this.values.getAndSet(id, value);
    }

    @Override
    public T get(MapKey key) {
        int id = key.getId();
        return id >= this.size ? this.getExtMap().get(id) : this.values.get(id);
    }

    @Override
    public synchronized T putIfAbsent(MapKey key, T value) {
        int id = key.getId();
        if (id >= this.size) {
            return this.getExtMap().putIfAbsent(id, value);
        } else {
            boolean absent = this.values.compareAndSet(id, null, value);
            return absent ? null : this.values.get(id);
        }
    }

    @Override
    public synchronized T remove(MapKey key) {
        int id = key.getId();
        return id >= this.size ? this.getExtMap().remove(id) : this.values.getAndSet(id, null);
    }

    private ConcurrentHashMap<Integer, T> getExtMap() {
        if (this.extMap == null) {
            synchronized (this) {
                if (this.extMap == null) {
                    this.extMap = new ConcurrentHashMap();
                }
            }
        }
        return this.extMap;
    }
}
