package com.github.art.行为型模式.策略模式;

/**
 * 环境(Context)角色
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/23
 */
public class InterestContext {
    private TaxStragetyable taxStragety;

    public InterestContext() {
    }

    public InterestContext(TaxStragetyable taxStragety) {
        this.taxStragety = taxStragety;
    }

    public double getTax(double income) {
        return this.taxStragety.calculateTax(income);
    }

    public void setTaxStragety(TaxStragetyable taxStragety) {
        this.taxStragety = taxStragety;
    }
}
