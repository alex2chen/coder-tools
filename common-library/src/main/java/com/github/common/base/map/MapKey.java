package com.github.common.base.map;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/6/6.
 */
public final class MapKey {
    private final KeyNamespace namespace;
    private final String name;
    private final int id;

    public MapKey(KeyNamespace namespace, String name) {
        this.namespace = namespace;
        this.name = name;
        this.id = namespace.getIdNext().getAndIncrement();
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public KeyNamespace getNamespace() {
        return this.namespace;
    }
}
