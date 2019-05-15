package com.github.art.行为型模式.职责链模式;

import java.math.BigDecimal;

/**
 * VicePresident
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/25
 */
public class VicePresidentHandle extends ApproverHandle {
    public VicePresidentHandle(String name) {
        setName(name);
    }

    @Override
    public void ProcessRequest(PurchaseRequest purchaseRequest) {
        if (purchaseRequest.getAmount().subtract(new BigDecimal(25000)).intValue() < 0) {
            System.out.println(String.format("%s-%s approved the request of purshing %s", "VicePresident", this.getName(), purchaseRequest.getProductName()));
        }
        else if (getNextApprover() != null) {
            getNextApprover().ProcessRequest(purchaseRequest);
        }
    }
}
