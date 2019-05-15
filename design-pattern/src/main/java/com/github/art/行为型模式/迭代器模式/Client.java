package com.github.art.行为型模式.迭代器模式;

/**
 * Client
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/25
 */
public class Client {
    public static void main(String[] args) {
        Aggregate collection = new ConcreteAggregate(new String[]{"a", "bc", "e", "12"});
        Iteratorable iterator = collection.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        System.out.println(iterator.first());
        System.out.println(iterator.next());
        System.out.println(iterator.previous());
    }
}
