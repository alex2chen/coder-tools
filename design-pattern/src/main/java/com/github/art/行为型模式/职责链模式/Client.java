package com.github.art.行为型模式.职责链模式;

import java.math.BigDecimal;

/**
 * Client
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/25
 */
public class Client {
    private static ApproverHandle buildChain() {
        ApproverHandle manager = new ManagerHandle("李经理");
        ApproverHandle vp = new VicePresidentHandle("张副总");
        ApproverHandle pre = new PresidentHandel("陈总经理");
        manager.setNextApprover(vp);
        vp.setNextApprover(pre);
        return manager;
    }

    public static void main(String[] args) {
        ApproverHandle manager = buildChain();
        PurchaseRequest requestTelphone = new PurchaseRequest(new BigDecimal(4000), "Telphone");
        manager.ProcessRequest(requestTelphone);//处理请求

        PurchaseRequest requestSoftware = new PurchaseRequest(new BigDecimal(10001), "Visual Studio");
        manager.ProcessRequest(requestSoftware);

        PurchaseRequest requestComputers = new PurchaseRequest(new BigDecimal(40000), "Computers");
        manager.ProcessRequest(requestComputers);
    }
}
