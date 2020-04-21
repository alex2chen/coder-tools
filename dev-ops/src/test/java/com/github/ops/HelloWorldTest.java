package com.github.ops;


import com.github.ops.jacoco.HelloWorld;
import com.github.ops.cobertura.Person;
import com.github.ops.utils.ListHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;

/**
 * Unit test for simple App.
 */
public class HelloWorldTest {
    @Test
    public void testsayHi() {
        HelloWorld helloWorld = new HelloWorld();
        Assert.assertNotNull(helloWorld.sayHi());
    }

    @Test
    public void testMethod2() {
        HelloWorld helloWorld = new HelloWorld();
        int sum = helloWorld.add(3, 4);
        Assert.assertThat(7, is(sum));
    }

    @Test
    public void testListHelper() {
        System.out.println(ListHelper.byte2Int((byte) 3));
        Person p = new Person("chen", 22);
        Person p1 = new Person("johne", 33);
        Person p3 = new Person("mashao", 33);
        List<Person> list = ListHelper.getList(p, p1, p3);
        System.out.println(list);

        List<Person> list1 = ListHelper.where(list, s -> s.getName().startsWith("z"));
        System.out.println(list1);

        System.out.println(ListHelper.first(list, null));

        Optional last = ListHelper.last(list, s -> s.getName().equals("mashao"));
        System.out.println(last.isPresent());
    }
}
