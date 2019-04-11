package com.github.spring;

import org.junit.Test;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.validation.DataBinder;

/**
 * @Author: alex
 * @Description: 源码相关 https://blog.csdn.net/alex_xfboy/article/details/88076245
 * @Date: created in 2018/4/11.
 */
public class DataBinder_test {
    @Test
    public void testConversionWithInappropriateStringEditor() {
        DataBinder dataBinder = new DataBinder(null);
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        dataBinder.setConversionService(conversionService);
        dataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

        NameBean bean = new NameBean("Fred");
        System.out.println(dataBinder.convertIfNecessary(bean, String.class));
        conversionService.addConverter(new NameBeanConverter());
        System.out.println(dataBinder.convertIfNecessary(bean, String.class));
    }

    public static class NameBean {
        private String name;

        public NameBean(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class NameBeanConverter implements Converter<NameBean, String> {
        @Override
        public String convert(NameBean source) {
            return "[" + source.getName() + "]";
        }
    }
}
