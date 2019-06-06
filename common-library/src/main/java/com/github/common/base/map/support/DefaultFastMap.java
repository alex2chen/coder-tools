package com.github.common.base.map.support;

import com.github.common.base.map.FastMap;
import com.github.common.base.map.KeyNamespace;
import com.github.common.base.map.MapKey;

import java.util.Arrays;

/**
 * @Author: alex
 * @Description: 非线程安全，性能比HashMap提升约30%，但需要预热（key处理）
 * @Date: created in 2018/6/6.
 */
public class DefaultFastMap<T> implements FastMap<T> {
    private final KeyNamespace namespace;
    private volatile Object[] values;

    public DefaultFastMap(KeyNamespace namespace, int size) {
        this.namespace = namespace;
        this.values = new Object[size];
    }

    @Override
    public T put(String key, T value) {
        MapKey newKey = new MapKey(namespace, key);
        return put(newKey, value);
    }

    @Override
    public T put(MapKey key, T value) {
        Object[] elements = this.values;
        int id = key.getId();
        if (id >= elements.length) {
            elements = Arrays.copyOf(elements, id + 1);
            this.values = elements;
        }
        Object old = elements[id];
        elements[id] = value;
        return (T) old;
    }

    @Override
    public T get(MapKey key) {
        Object[] elements = this.values;
        int id = key.getId();
        return id >= elements.length ? null : (T) elements[id];
    }

    @Override
    public T putIfAbsent(MapKey key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(MapKey key) {
        Object[] elements = this.values;
        int id = key.getId();
        if (id >= elements.length) {
            return null;
        } else {
            Object old = elements[id];
            elements[id] = null;
            return (T) old;
        }
    }
}
