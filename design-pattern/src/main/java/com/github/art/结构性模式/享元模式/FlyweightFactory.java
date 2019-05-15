package com.github.art.结构性模式.享元模式;

import java.util.HashMap;
import java.util.Map;

/**
 * FlyweightFactory
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/23
 */
public class FlyweightFactory {
    private Map<String, Flyweight> flyweights;

    public FlyweightFactory() {
        initFlyweights();
    }

    private void initFlyweights() {
        flyweights = new HashMap<>();
        flyweights.put("key1", new ConcreteFlyweight("key1"));
        flyweights.put("key2", new ConcreteFlyweight("key2"));
        flyweights.put("key3", new ConcreteFlyweight("key3"));
    }

    public Flyweight getFlyweight(String key) {
        return flyweights.get(key);
    }
}
