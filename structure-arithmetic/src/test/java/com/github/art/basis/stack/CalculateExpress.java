package com.github.art.basis.stack;

import org.junit.Test;

import java.util.Stack;

/**
 * 使用栈计算后缀表达式
 */
public class CalculateExpress {
    @Test
    public void go_print() {
        String exp="231*+9-";
        System.out.println(evaluateExpress(exp));
    }

    private int evaluateExpress(String input) {
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isDigit(c))
                stack.push(c - '0');
            else {
                int val1 = stack.pop();
                int val2 = stack.pop();
                switch (c) {
                    case '+':
                        stack.push(val2 + val1);
                        break;
                    case '-':
                        stack.push(val2 - val1);
                        break;
                    case '/':
                        stack.push(val2 / val1);
                        break;
                    case '*':
                        stack.push(val2 * val1);
                        break;
                }
            }
        }
        return stack.pop();
    }
}
