package com.github.art.行为型模式.迭代器模式;

/**
 * Aggregate
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/25
 */
public interface Aggregate {
    public Iteratorable iterator();

    public Object get(int i);

    public int size();
}
