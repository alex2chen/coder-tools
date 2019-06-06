package com.github.common.base.map;

/**
 * @Author: alex
 * @Description: MapKey会在初始化阶段就去申请一个固定id,存放的id作为index访问数组即可，避免了hash计算等一系列操作
 * @Date: created in 2018/6/6.
 */
public interface FastMap<T> {
    T put(String key, T value);

    T put(MapKey key, T value);

    T get(MapKey key);

    T putIfAbsent(MapKey key, T value);

    T remove(MapKey key);

}
