package com.github.common.script;

import groovy.util.Eval;
import org.junit.Test;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/5/13.
 */
public class Groovy_test {
    @Test
    public void eval() {
        // Eval.me("println 'hello world'");
        Object result = Eval.me("age", 22, "if(age < 18){'未成年'}else{'成年'}");
        System.out.println(result);
        //绑定一个名为 x 的参数的简单计算
        //System.out.println(Eval.x(4, "2*x"));
        //带有两个名为 x 与 y 的绑定参数的简单计算
        // System.out.println(Eval.xy(4, 5, "x*y"));
        //带有三个绑定参数（x、y 和 z）的简单计算
        // System.out.println(Eval.xyz(4, 5, 6, "x*y+z"));
    }
}
