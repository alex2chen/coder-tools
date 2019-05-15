package com.github.art.行为型模式.观察者模式;

import java.util.ArrayList;

/**
 * ConcreteSubject
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/25
 */
public class ConcreteSubject extends AbstractSubject {
    public ConcreteSubject(String name) {
        this.name = name;
        this.observers = new ArrayList<>();
    }
}
