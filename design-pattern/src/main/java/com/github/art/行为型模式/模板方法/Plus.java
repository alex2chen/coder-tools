package com.github.art.行为型模式.模板方法;

/**
 * Plus
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/23
 */
public class Plus extends AbstractCalculator {
    @Override
    public int calculate(int num1, int num2) {
        return num1 + num2;
    }
}
