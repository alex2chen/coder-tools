package com.github.art.行为型模式.访问者模式;

/**
 * Visitor
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public interface Visitor {
    public void think(Man man);

    public void think(Woman girl);
}
