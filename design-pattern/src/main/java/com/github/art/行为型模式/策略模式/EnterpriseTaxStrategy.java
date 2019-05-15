package com.github.art.行为型模式.策略模式;

/**
 * 具体策略
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/23
 */
public class EnterpriseTaxStrategy implements TaxStragetyable {
    @Override
    public double calculateTax(double income) {
        return (income - 3500) > 0 ? (income - 3500) * 0.045 : 0.0;
    }
}
