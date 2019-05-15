package com.github.art.创建型模式.原型;

/**
 * Client
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/19
 */
public class Client {
    public static void main(String[] args) throws CloneNotSupportedException {
        Prototype prototypeOld = new Prototype("12px");
        //变一个
        Prototype cloneOne = (Prototype) prototypeOld.clone();
        System.out.println(cloneOne);
        //变两个
        Prototype cloneTwo = (Prototype) prototypeOld.clone();
        System.out.println(cloneTwo);

    }
}
