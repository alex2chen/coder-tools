package com.github.art.行为型模式.模板方法;

/**
 * AbstractCalculator
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/23
 */
public abstract class AbstractCalculator {
    public abstract int calculate(int num1, int num2);

    public int cal(int num1, int num2) {
        calBefore();
        int result = calculate(num1, num2);
        calAfter();
        return result;
    }

    private void calBefore() {
        System.out.println("计算开始");
    }

    private void calAfter() {
        System.out.println("计算结束");
    }
}
