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

    @Test
    public void arraycopy() {
        String[] s = {"Mircosoft", "IBM", "Sun", "Oracle", "Apple"};
        String[] sBak = new String[6];
        System.arraycopy(s, 0, sBak, 0, s.length);
        for (int i = 0; i < sBak.length; i++) {
            System.out.print(sBak[i] + " ,");
        }
        System.out.println(System.getenv());
    }
}
