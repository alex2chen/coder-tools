package com.github.spring.core;

import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.Arrays;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/2/11.
 */
public class PathMatchingResourcePatternResolver_test {
    /**
     * 读取某个目录下*.csv文件
     *
     * @throws IOException
     */
    @Test
    public void readPatternPath() throws IOException {
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = patternResolver.getResources("file:E:/doTry/spring/spring-batch/spring-batch-example/src/main/resources/ch11/data/*.csv");
        //System.out.println(Arrays.toString(resources));
        Arrays.asList(resources).forEach(System.out::println);
    }
}
