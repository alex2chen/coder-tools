package com.github.art.行为型模式.中介者模式;

/**
 * AbstractMediator
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public abstract class AbstractMediator {
    private AbstractCardPartner a;
    private AbstractCardPartner b;

    public AbstractCardPartner getA() {
        return a;
    }

    public void setA(AbstractCardPartner a) {
        this.a = a;
    }

    public AbstractCardPartner getB() {
        return b;
    }

    public void setB(AbstractCardPartner b) {
        this.b = b;
    }

    public abstract void aWin(int count);

    public abstract void bWin(int count);
}
