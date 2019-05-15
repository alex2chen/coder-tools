package com.github.art.行为型模式.职责链模式;

/**
 * Approver
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/25
 */
public abstract class ApproverHandle {
    private String name;
    private ApproverHandle nextApprover;

    public abstract void ProcessRequest(PurchaseRequest purchaseRequest);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ApproverHandle getNextApprover() {
        return nextApprover;
    }

    public void setNextApprover(ApproverHandle nextApprover) {
        this.nextApprover = nextApprover;
    }
}
