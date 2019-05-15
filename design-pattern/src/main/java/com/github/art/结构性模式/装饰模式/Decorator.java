package com.github.art.结构性模式.装饰模式;

/**
 * Decorator
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/22
 */
public abstract class Decorator extends Phone {
    private Phone phone;

    public Decorator(Phone phone) {
        this.phone = phone;
    }

    @Override
    public void print() {
        phone.print();
    }
}
