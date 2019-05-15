package com.github.art.行为型模式.中介者模式;

/**
 * ParterB
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class ParterB extends AbstractCardPartner {
    public ParterB() {
    }

    public ParterB(int moneyCount) {
        this.setMoneyCount(moneyCount);
    }

    @Override
    public void changeCount(AbstractMediator mediator, int count) {
        mediator.bWin(count);
    }
}
