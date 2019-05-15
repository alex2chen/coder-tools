package com.github.art.行为型模式.迭代器模式;

/**
 * ConcreteIteraror
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/25
 */
public class ConcreteIteraror implements Iteratorable {
    private Aggregate aggregate;
    private int pos = -1;

    public ConcreteIteraror(Aggregate aggregate) {
        this.aggregate = aggregate;
    }

    @Override
    public Object previous() {
        if (pos > 0) {
            pos--;
        }
        return aggregate.get(pos);
    }

    @Override
    public Object next() {
        if (pos < aggregate.size() - 1) {
            pos++;
        }
        return aggregate.get(pos);
    }

    @Override
    public Boolean hasNext() {
        return pos < aggregate.size() - 1 ? true : false;
    }

    @Override
    public Object first() {
        pos = 0;
        return aggregate.get(pos);
    }
}
