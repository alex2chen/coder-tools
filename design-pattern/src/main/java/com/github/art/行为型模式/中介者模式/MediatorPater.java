package com.github.art.行为型模式.中介者模式;

/**
 * MediatorPater
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class MediatorPater extends AbstractMediator {
    public MediatorPater() {
    }

    public MediatorPater(AbstractCardPartner a, AbstractCardPartner b) {
        this.setA(a);
        this.setB(b);
    }

    @Override
    public void aWin(int count) {
        System.out.println("cardPartnerA win:"+count);
        this.getA().setMoneyCount(getA().getMoneyCount() + count);
        this.getB().setMoneyCount(getB().getMoneyCount() - count);
    }

    @Override
    public void bWin(int count) {
        System.out.println("cardPartnerB win:"+count);
        this.getB().setMoneyCount(getB().getMoneyCount() + count);
        this.getA().setMoneyCount(getA().getMoneyCount() - count);
    }
}
