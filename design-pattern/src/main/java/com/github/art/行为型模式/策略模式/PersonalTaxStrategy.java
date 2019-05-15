package com.github.art.行为型模式.策略模式;

/**
 * 具体策略
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/23
 */
public class PersonalTaxStrategy implements TaxStragetyable {
    @Override
    public double calculateTax(double income) {
        return income * 0.12;
    }
}
