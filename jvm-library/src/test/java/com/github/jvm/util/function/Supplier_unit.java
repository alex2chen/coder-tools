package com.github.jvm.util.function;

import org.junit.Test;

import java.util.EventObject;
import java.util.function.Supplier;

/**
 * @author alex.chen
 * @Description: Supplier 接口返回一个任意范型的值，和Function接口不同的是该接口没有任何参数
 * @date 2018/4/13
 */
public class Supplier_unit {
    @Test
    public void lasyGet() {
        Supplier<Person> supplier = Person::new;
        System.out.println(supplier.get());//new Person
    }

    public static class Person {
    }
}
