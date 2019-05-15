package com.github.art.结构性模式.享元模式;

/**
 * ConcreteFlyweight
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/23
 */
public class ConcreteFlyweight extends Flyweight {
    private String intrinsicstate;

    public ConcreteFlyweight(String intrinsicstate) {
        this.intrinsicstate = intrinsicstate;
    }

    @Override
    public void operation(int extrinsicstate) {
        System.out.println(intrinsicstate + ":" + extrinsicstate);
    }
}
