package com.github.spring.core;

import com.github.spring.SpringConfigReader;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ObjectUtils;

import java.io.IOException;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/4/11.
 */
public class ObjectUtils_test {
    @Test
    public void identityToString() {
        System.out.println(ObjectUtils.identityToString(new User()));
    }

    @Test
    public void filepath() throws IOException {
        System.out.println(SpringConfigReader.class.getClassLoader().getResource("static/logo.jpg"));
        System.out.println(new ClassPathResource("static/logo.jpg").getFile());
    }

    public class User {
    }
}
