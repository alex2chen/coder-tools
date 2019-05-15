package com.github.art.行为型模式.中介者模式;

/**
 * ParterA
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class ParterA extends AbstractCardPartner {
    public ParterA() {

    }

    public ParterA(int moneyCount) {
        setMoneyCount(moneyCount);
    }

    @Override
    public void changeCount(AbstractMediator mediator, int count) {
        mediator.aWin(count);
    }
}
