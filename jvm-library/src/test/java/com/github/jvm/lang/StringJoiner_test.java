package com.github.jvm.lang;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/4/11.
 */
public class StringJoiner_test {
    private List<String> names = Lists.newArrayList("alex", "top");

    @Test
    public void stringjoin() {
        //guava
        System.out.println(Joiner.on(";").join(names));

        //jdk
        StringJoiner joiner = new StringJoiner(";");
        names.forEach(s -> joiner.add(s));
        System.out.println(joiner.toString());
        //lambda
        System.out.println(Lists.newArrayList("alex", "top").stream().collect(Collectors.joining(";")));
    }
}
