package com.github.art.行为型模式.访问者模式;

/**
 * Success
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class Success implements Visitor {
    @Override
    public void think(Man man) {
        System.out.println("一个成功的男人背后一定有一个支持他的女人");
    }

    @Override
    public void think(Woman girl) {
        System.out.println("一个成功的女人背后一定有一个不成功的男人");
    }
}
