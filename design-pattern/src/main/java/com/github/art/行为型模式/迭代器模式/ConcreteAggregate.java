package com.github.art.行为型模式.迭代器模式;

/**
 * ConcreteAggregate
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/25
 */
public class ConcreteAggregate implements Aggregate {
    private String[] strs;

    public ConcreteAggregate(String[] str) {
        this.strs = str;
    }

    @Override
    public Iteratorable iterator() {
        return new ConcreteIteraror(this);
    }

    @Override
    public Object get(int i) {
        return strs[i];
    }

    @Override
    public int size() {
        return strs.length;
    }
}
