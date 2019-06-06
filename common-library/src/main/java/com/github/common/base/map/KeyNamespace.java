package com.github.common.base.map;

import com.google.common.base.Preconditions;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/6/6.
 */
public class KeyNamespace {
    private final String namespace;
    private final AtomicInteger idNext = new AtomicInteger();
    //多实例缓存
    private static final ConcurrentHashMap<String, KeyNamespace> KEY_NAMESPACE = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, MapKey> name2MapKey = new ConcurrentHashMap();
    private final ConcurrentHashMap<Integer, MapKey> id2MapKey = new ConcurrentHashMap();

    private KeyNamespace(String namespace) {
        this.namespace = namespace;
    }

    public static KeyNamespace getOrCreateNamespace(String namespace) {
        KeyNamespace instance = KEY_NAMESPACE.get(namespace);
        if (instance == null) {
            instance = new KeyNamespace(namespace);
            KeyNamespace old = KEY_NAMESPACE.putIfAbsent(namespace, instance);
            if (old != null) {
                instance = old;
            }
        }

        return instance;
    }

    public static KeyNamespace createNamespace(String namespace) {
        KeyNamespace key = new KeyNamespace(namespace);
        KeyNamespace old = KEY_NAMESPACE.putIfAbsent(namespace, key);
        Preconditions.checkArgument(old == null, "namespace '%s' is already exists", namespace);
        return key;
    }

    public MapKey getOrCreate(String name) {
        MapKey key = this.name2MapKey.get(name);
        if (key == null) {
            key = new MapKey(this, name);
            MapKey old = this.name2MapKey.putIfAbsent(name, key);
            if (old == null) {
                this.id2MapKey.put(key.getId(), key);
            } else {
                key = old;
            }
        }

        return key;
    }

    public MapKey create(String name) {
        MapKey key = new MapKey(this, name);
        MapKey old = this.name2MapKey.putIfAbsent(name, key);
        Preconditions.checkArgument(old == null, "name '%s' in namespace '%s' is already exists", name, namespace);
        this.id2MapKey.put(key.getId(), key);
        return key;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public AtomicInteger getIdNext() {
        return this.idNext;
    }

    public MapKey get(int id) {
        return this.id2MapKey.get(id);
    }
}
