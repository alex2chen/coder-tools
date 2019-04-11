package com.github.junit;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/11/11
 */
//按方法名字母顺序执行
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Num_TestCase {
    @Ignore
    public void test_a(){
        System.out.println("test_a");
    }
    @Test
    public void add_test(){
        int _total = new Num().add(8,7);
        assertEquals(_total,15);
        assertThat(_total,is(15));
        assertThat(_total,allOf(greaterThan(5),lessThan(6)));
    }
    @Test(timeout = 500)
    public void plus_test(){
        try {
            int _num1=new Num().plus(9, 2);
            assertThat(_num1,greaterThan(0));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
