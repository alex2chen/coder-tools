package com.github.jvm.lang;

import org.junit.Test;

import java.util.Map;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/12/21.
 */
public class System_test {
    @Test
    public void env() {
        Map<String, String> envs = System.getenv();
        envs.entrySet().forEach(x -> System.out.println(x.getKey() + " > " + x.getValue()));
    }
}
