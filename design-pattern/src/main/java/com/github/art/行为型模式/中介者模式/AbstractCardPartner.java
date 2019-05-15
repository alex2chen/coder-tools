package com.github.art.行为型模式.中介者模式;

/**
 * AbstractCardPartner
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public abstract class AbstractCardPartner {
    private int moneyCount;

    public int getMoneyCount() {
        return moneyCount;
    }

    public void setMoneyCount(int moneyCount) {
        this.moneyCount = moneyCount;
    }

    public abstract void changeCount(AbstractMediator mediator, int count);
}
