package com.github.jvm.lang;

import com.google.common.base.Stopwatch;
import org.junit.Before;
import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: alex
 * @Description: 性能比较
 * methodHandle Invoke 16.84 ms
 * invoke 57.33 ms
 * methodHandle invokeExact 60.57 ms
 * methodHandle invokeWithArguments 146.6 ms
 *
 * @Date: created in 2018/4/11.
 */
public class MethodHandle_test {
    private Person person = new Person("chen", "alex");
    private Method method;
    private MethodHandle methodHandle;
    private boolean param = true;

    @Before
    public void init() {
        try {
            method = Person.class.getDeclaredMethod("getFullName", boolean.class);
            methodHandle = MethodHandles.publicLookup().findVirtual(Person.class, "getFullName", MethodType.methodType(String.class, boolean.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void invoke() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 10000; i++) {
            Object result = method.invoke(person, param);
        }
        System.out.printf("invoke %s", stopwatch);
        System.out.println();
    }

    @Test
    public void methodHandleInvoke() throws Throwable {
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 10000; i++) {
            Object result = methodHandle.invoke(person, param);
        }
        System.out.printf("methodHandle Invoke %s", stopwatch);
        System.out.println();
    }

    @Test
    public void methodHandleWithArguments() throws Throwable {
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 10000; i++) {
            Object result = methodHandle.invokeWithArguments(new Object[]{person, param});
        }
        System.out.printf("methodHandle invokeWithArguments %s", stopwatch);
        System.out.println();
    }

    @Test
    public void methodHandleInvokeExact() throws Throwable {
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 10000; i++) {
            //Object result = methodHandle.invokeExact(person, param);//会报错：java.lang.invoke.WrongMethodTypeException: expected
            String result = (String) methodHandle.invokeExact(person, param);
        }
        System.out.printf("methodHandle invokeExact %s", stopwatch);
        System.out.println();
    }

    public class Person {
        private String firstName;
        private String lastName;

        public Person(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getFullName(boolean isFullName) {
            return String.format("%s.%s", firstName, lastName);
        }
    }
}
