package com.github.art.行为型模式.迭代器模式;

/**
 * Iteratorable
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/25
 */
public interface Iteratorable {
    public Object previous();

    public Object next();

    public Boolean hasNext();

    public Object first();
}
