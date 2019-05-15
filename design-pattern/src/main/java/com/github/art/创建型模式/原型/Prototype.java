package com.github.art.创建型模式.原型;

/**
 * 原型
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/19
 */
public class Prototype implements Cloneable {
    public String id;

    public Prototype(String id) {
        this.id = id;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Prototype{" +
                "id='" + id + '\'' +
                '}';
    }
}
