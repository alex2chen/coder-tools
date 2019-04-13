package com.github.jvm.util.function;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

/**
 * @author alex.chen
 * @Description:
 * @date 2016/1/8
 */
public class Stream_unit {
    @Test
    public void test() {
        List<String> list = Lists.newArrayList("a", "b", "ddd", "b");
        list.stream()
                .filter((x) -> "all".equals(x))
                .distinct()
                .limit(4)
                .skip(1).forEach(System.out::println);
    }

}
