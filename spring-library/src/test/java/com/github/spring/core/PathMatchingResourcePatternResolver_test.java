package com.github.spring.core;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/2/11.
 */
public class PathMatchingResourcePatternResolver_test {
    private ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();

    @Test
    public void readCsvFile() throws IOException {
        Resource[] resources = patternResolver.getResources("file:E:/doTry/spring/spring-batch/spring-batch-example/src/main/resources/ch11/data/*.csv");
        //System.out.println(Arrays.toString(resources));
        Arrays.asList(resources).forEach(System.out::println);
    }

    @Test
    public void doClassScan() throws IOException, ClassNotFoundException {
        List<Class<?>> classes = doScan("com.github.spring", c -> true);
        classes.forEach(System.out::println);
    }

    private List<Class<?>> doScan(String basePackage, Predicate<Class<?>> predicate) throws IOException, ClassNotFoundException {
        Pattern PACKAGE_TO_DIRECTORY = Pattern.compile("\\.");
        String CLASS_PATTERN = "*.class";
        String baseUrl = PACKAGE_TO_DIRECTORY.matcher(basePackage).replaceAll("/");
        baseUrl = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + baseUrl + "/" + CLASS_PATTERN;
        Resource[] resources = patternResolver.getResources(baseUrl);
        List<Class<?>> classes = Lists.newArrayList();
        for (Resource resource : resources) {
            if (!resource.isReadable()) {
                continue;
            }
            int index = resource.getFilename().lastIndexOf('.');
            String simpleClassName = resource.getFilename().substring(0, index);
            String className = basePackage + "." + simpleClassName;
            Class<?> markedClass = Thread.currentThread().getContextClassLoader().loadClass(className);
            if (predicate.apply(markedClass)) {
                classes.add(markedClass);
            }
        }
        return classes;
    }
}
