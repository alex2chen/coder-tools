package com.github.art.行为型模式.访问者模式;

/**
 * Woman
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class Woman implements Person {
    @Override
    public void accept(Visitor visitor) {
        visitor.think(this);
    }
}
