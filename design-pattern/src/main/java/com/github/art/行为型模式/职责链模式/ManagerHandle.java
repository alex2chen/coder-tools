package com.github.art.行为型模式.职责链模式;

import java.math.BigDecimal;

/**
 * Manager
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/25
 */
public class ManagerHandle extends ApproverHandle {
    public ManagerHandle(String name) {
        this.setName(name);
    }

    @Override
    public void ProcessRequest(PurchaseRequest purchaseRequest) {
        if (purchaseRequest.getAmount().subtract(new BigDecimal(10000)).intValue() < 0) {
            System.out.println(String.format("%s-%s approved the request of purshing %s","Manager", this.getName(), purchaseRequest.getProductName()));
        }
        else if (this.getNextApprover() != null) {
            getNextApprover().ProcessRequest(purchaseRequest);
        }
    }
}
