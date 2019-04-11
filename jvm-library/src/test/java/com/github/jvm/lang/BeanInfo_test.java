package com.github.jvm.lang;

import com.sun.beans.editors.IntegerEditor;
import org.junit.Test;

import java.beans.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/12/21.
 */
public class BeanInfo_test {
    @Test
    public void getDescriptor() throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(User.class);
        System.out.println(beanInfo.getBeanDescriptor());
        System.out.println(Arrays.toString(beanInfo.getMethodDescriptors()));
        System.out.println(Arrays.toString(beanInfo.getPropertyDescriptors()));
    }

    @Test
    public void setAge() throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        User userInfo = new User();
        String age = "age";
        Object ageValue = 19;//如果是非int会报错，java.lang.IllegalArgumentException: argument type mismatch
        BeanInfo beanInfo = Introspector.getBeanInfo(User.class);
        PropertyDescriptor[] proDescrtptors = beanInfo.getPropertyDescriptors();
        if (proDescrtptors != null && proDescrtptors.length > 0) {
            for (PropertyDescriptor propDesc : proDescrtptors) {
                if (propDesc.getName().equals(age)) {
                    Method methodSetUserName = propDesc.getWriteMethod();//很重要的原则
                    methodSetUserName.invoke(userInfo, ageValue);
                    Method methodGetUserName = propDesc.getReadMethod();
                    System.out.println(methodGetUserName.invoke(userInfo));
                    break;
                }
            }
        }
    }

    @Test
    public void setAgeToListener() throws IntrospectionException {
        User userInfo = new User();
        String age = "age";
        String ageValue = "19";
        BeanInfo beanInfo = Introspector.getBeanInfo(User.class);
        PropertyDescriptor[] proDescrtptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propDesc : proDescrtptors) {
            if (propDesc.getName().equals(age)) {
                propDesc.setPropertyEditorClass(IntegerEditor.class);//也可以自定义
                PropertyEditor propertyEditor = propDesc.createPropertyEditor(userInfo);
                propertyEditor.addPropertyChangeListener(x -> {
                    PropertyEditor source = (PropertyEditor) x.getSource();
                    Method methodSetUserName = propDesc.getWriteMethod();
                    try {
                        System.out.println("newValue:" + source.getValue());
                        methodSetUserName.invoke(userInfo, source.getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                propertyEditor.setAsText(ageValue);
                break;
            }
        }
    }

    public class User {
        private String userName;
        private int age;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

}
