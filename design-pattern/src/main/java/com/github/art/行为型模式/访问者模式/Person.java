package com.github.art.行为型模式.访问者模式;

/**
 * Person
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public interface Person {
    public void accept(Visitor visitor);
}
