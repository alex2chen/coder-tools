package com.github.art.行为型模式.中介者模式;

/**
 * Client
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class Client {
    public static void main(String[] args) {
        AbstractCardPartner partnerA = new ParterA(200);
        AbstractCardPartner partnerB = new ParterB(800);

        AbstractMediator mediatorPater = new MediatorPater(partnerA, partnerB);
        //第一局
        mediatorPater.aWin(280);
        //第二局
        mediatorPater.bWin(30);

        System.out.println("partnerA:" + partnerA.getMoneyCount());
        System.out.println("partnerB:" + partnerB.getMoneyCount());
    }
}
