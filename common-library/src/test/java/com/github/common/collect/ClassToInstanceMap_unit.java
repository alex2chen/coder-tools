package com.github.common.collect;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import org.junit.Test;

/**
 * @author alex.chen
 * @Description:
 * @date 2017/4/14
 */
public class ClassToInstanceMap_unit {
    @Test
    public void putInstance() {
        ClassToInstanceMap<Person> classToInstanceMap = MutableClassToInstanceMap.create();
        Person person = new Person(20, "abc");
        classToInstanceMap.putInstance(Person.class, person);
        Person person1 = classToInstanceMap.getInstance(Person.class);
        System.out.println(person1);
    }

    public static class Person {
        private String name;
        private int age;
        public Person(int age, String name) {
            this.age = age;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
