package com.github.art.行为型模式.职责链模式;

import java.math.BigDecimal;

/**
 * PurchaseRequest
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/25
 */
public class PurchaseRequest {
    private String productName;
    private BigDecimal amount;
    public PurchaseRequest(BigDecimal amount,String productName) {
        this.productName = productName;
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
