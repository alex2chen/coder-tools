package com.github.spring.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/8/22.
 */
public class SpringConfigReader {

    /**
     * @param xmlFile 如：classpath:spring-retry.xml
     * @return
     */
    public ApplicationContext loadXmlSpringContext(String xmlFile) {
        return new ClassPathXmlApplicationContext(xmlFile);
    }

    /**
     * spring boot
     *
     * @param basePackage
     * @return
     */
    public ApplicationContext loadAnnotationConfigSpringContext(String basePackage) {
        return new AnnotationConfigApplicationContext(basePackage);
    }

    /**
     * spring boot
     *
     * @param annotatedClasses
     * @return
     */
    public ApplicationContext loadAnnotationConfigSpringContext(Class... annotatedClasses) {
        return new AnnotationConfigApplicationContext(annotatedClasses);
    }
}
