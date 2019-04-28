package com.github.jvm.lang;

import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @Author: alex
 * @Description: Java 8 Reflection Access to Parameter Names问题 https://www.concretepage.com/java/jdk-8/java-8-reflection-access-to-parameter-names-of-method-and-constructor-with-maven-gradle-and-eclipse-using-parameters-compiler-argument
 * 要解决该问题：
 * 1.指定compilerArgs -parameters
 * 2.采用Spring的思路通过注解，public String info(@RequestParam(value="key") String key)
 * @Date: created in 2019/4/28.
 */
public class Method_test {

    @Test
    public void isNamePresent() {
        Method[] methods = BookService.class.getDeclaredMethods();
        for (Method method : methods) {
            Parameter[] parameters = method.getParameters();
            for (Parameter p : parameters) {
                System.out.println(p.getName());//只能拿到arg0、arg1
                if (p.isNamePresent()) {//false
                    System.out.println(p.getName());
                }
            }
        }

    }

    public class BookService {
        public BookService(Integer bookId, String bookDesc) {
        }

        public void evaluateBook(String bookName, Integer bookPrice) {
        }
    }
}
