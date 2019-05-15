package com.github.art.行为型模式.职责链模式;

import java.math.BigDecimal;

/**
 * President
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/25
 */
public class PresidentHandel extends ApproverHandle {
    public PresidentHandel(String name) {
        this.setName(name);
    }

    @Override
    public void ProcessRequest(PurchaseRequest purchaseRequest) {
        if (purchaseRequest.getAmount().subtract(new BigDecimal(100000)).intValue() < 0) {
            System.out.println(String.format("%s-%s approved the request of purshing %s", "President", this.getName(), purchaseRequest.getProductName()));
        } else {
            System.out.println("Request需要组织一个会议讨论");
        }
    }
}
