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
 * @Description:
 * @Date: created in 2018/4/11.
 */
public class MethodHandle_test {
    private Person person = new Person("chen", "alex");
    private Stopwatch stopwatch;

    @Before
    public void init() {
        stopwatch = Stopwatch.createStarted();
    }

    @Test
    public void invoke() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Person.class.getDeclaredMethod("getFirstName", new Class[]{});
        Object[] emptyParams = new Object[]{};
        Object result = method.invoke(person, emptyParams);
        System.out.printf("invoke %s %s",result,stopwatch);
    }

    @Test
    public void methodHandle() throws Throwable {
        MethodHandle methodHandle = MethodHandles.publicLookup().findVirtual(Person.class, "getFirstName", MethodType.methodType(String.class, new Class[]{}));
        Object result = methodHandle.invoke(person);
        System.out.printf("methodHandle %s %s",result,stopwatch);
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
    }
}
