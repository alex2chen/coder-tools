package com.github.art.行为型模式.访问者模式;

/**
 * Client
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class Client {
    public static void main(String[] args) {
        ObjectStructure objectStructure = new ObjectStructure();
        objectStructure.attach(new Woman());
        objectStructure.attach(new Man());

        Visitor visitor = new Success();
        objectStructure.display(visitor);

        visitor = new Love();
        objectStructure.display(visitor);

    }
}
