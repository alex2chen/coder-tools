package com.github.art.行为型模式.观察者模式;

import java.util.List;

/**
 * AbstractSubject
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/25
 */
public abstract class AbstractSubject {
    protected List<Observer> observers;
    protected String name;
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        if (state != this.state) {
            notify2All();
        }
        this.state = state;
    }

    public void addObserver(Observer ob) {
        System.out.println("有人订阅");
        observers.add(ob);
    }

    public void removeObserver(Observer ob) {
        System.out.println("有人退订");
        observers.remove(ob);
    }

    public void notify2All() {
        if (!observers.isEmpty()) {
            for (Observer observer : observers) {
                observer.update();
            }
        }
    }
}
