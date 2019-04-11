package com.github.testng;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/30
 */
public class ParallelMethodTest {
//    @BeforeTest
//    public void beforetest() {
//        System.out.printf("beforetest");
//    }
//
//    @BeforeClass
//    public void beforeclass() {
//        System.out.printf("beforeclass");
//    }

    @BeforeMethod
    public void beforeMethod() {
        long id = Thread.currentThread().getId();
        System.out.println("Before test-method. Thread id is: " + id);
    }

    @Test
    public void hello_test() {
        long id = Thread.currentThread().getId();
        System.out.printf("hello.Thread id is:"+id);
    }
    @Test
    public void testMethodsTwo() {
        long id = Thread.currentThread().getId();
        System.out.println("testMethodsTwo. Thread id is: " + id);
    }
    @Test(invocationCount = 6,threadPoolSize = 3)
    public void testMethod(){
        long id = Thread.currentThread().getId();
        System.out.println("testMethod. Thread id is: " + id);
    }

}
