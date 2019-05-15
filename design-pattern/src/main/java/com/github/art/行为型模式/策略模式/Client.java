package com.github.art.行为型模式.策略模式;

/**
 *
 */
public class Client {
    public static void main(String[] args) {
        InterestContext context = new InterestContext();
        context.setTaxStragety(new EnterpriseTaxStrategy());
        System.out.println(context.getTax(8000));

        context.setTaxStragety(new PersonalTaxStrategy());
        System.out.println(context.getTax(8000));
    }
}
