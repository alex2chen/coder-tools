package com.github.common.bean;

import com.google.common.base.Stopwatch;
import net.sf.cglib.core.DebuggingClassWriter;
import org.junit.Test;
import org.springside.modules.utils.reflect.FastMethodInvoker;

/**
 * @Author: alex
 * @Description: 基于cglib，通过代码生成实现最快速的反射调用 https://github.com/springside/springside4/wiki
 * @Date: created in 2018/4/15.
 */
public class FastMethodInvoker_unit {
    @Test
    public void invoke(){
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "target\\class");
        FastMethodInvoker fastMethodInvoker=FastMethodInvoker.create(FastMethodInvoker_unit.class,"display",String.class);
        Stopwatch stopwatch=Stopwatch.createStarted();
        fastMethodInvoker.invoke(this,"alex");
        System.out.println(stopwatch);
    }
    public void display(String name){
        System.out.println(name);
    }
}
