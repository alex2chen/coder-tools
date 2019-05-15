package com.github.art.行为型模式.观察者模式;

/**
 * ConcreteObserver2
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/25
 */
public class ConcreteObserver2 implements Observer {
    @Override
    public void update() {
        System.out.println("观察者2被触发");
    }
}
