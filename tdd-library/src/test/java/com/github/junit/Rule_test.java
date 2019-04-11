package com.github.junit;

import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.ExternalResource;
import org.junit.runners.MethodSorters;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/11/18
 */
@FixMethodOrder(MethodSorters.JVM)
public class Rule_test {
    @Rule
    public ExpectedException exp = ExpectedException.none();

    @Test(expected = ArithmeticException.class)
    public void divide_test() {
        int _num1 = new Num().divide(1, 0);
        System.out.println("ok");
    }

    //等同上一种
    @Test
    public void divide_test2() {
        exp.expect(ArithmeticException.class);
        int _num1 = new Num().divide(1, 0);
        System.out.println("ok");
    }

    @Test
    public void expectException() {
        exp.expect(IndexOutOfBoundsException.class);
        throw new IndexOutOfBoundsException("Exception method.");
    }

    @ClassRule
    public static ExternalResource external = new ExternalResource() {
        protected void before() throws Throwable {
            System.out.println("Perparing test data.");
        }

        protected void after() {
            System.out.println("Cleaning test data.");
        }
    };

}
