package com.github.art.行为型模式.访问者模式;

/**
 * Love
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class Love implements Visitor {
    @Override
    public void think(Man man) {
        System.out.println("恋爱中的男人，不懂装懂");
    }

    @Override
    public void think(Woman girl) {
        System.out.println("恋爱中的女人，智商为零");
    }
}
