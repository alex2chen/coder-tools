package com.github.common.script;

import org.junit.Test;

import javax.script.*;

/**
 * @Author: alex
 * @Description: 对应的包是javax.script。默认情况下，Java 6只支持javascript脚本，它底层的实现是Mozilla Rhino，它是个纯Java的javascript实现。
 * Java 8之后是Nashorn，SPI实现类NashornScriptEngineFactory
 * @Date: created in 2018/5/13.
 */
public class Javascript_test {

    /**
     * JSR 223 javax.script API
     * JSR-223 是 Java 中调用脚本语言的标准 API。从 Java 6 开始引入进来，主要目的是用来提供一种统一的框架，
     * 以便在 Java 中调用多种脚本语言。JSR-223 支持大部分流行的脚本语言，比如JavaScript、Scala、JRuby、Jython和Groovy等。
     */
    @Test
    public void jsr() throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("groovy");
        Bindings bindings = new SimpleBindings();
        bindings.put("age", 22);
        Object value = engine.eval("if(age < 18){'未成年'}else{'成年'}", bindings);
        System.out.println(value);
    }

    @Test
    public void jsr2() throws ScriptException, NoSuchMethodException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        // 建立上下文变量
        Bindings bind = engine.createBindings();
        // 加入变量champion值为GSP
        bind.put("champion", "GSP");
        // 绑定上下文，作用域为当前引擎范围
        engine.setBindings(bind, ScriptContext.ENGINE_SCOPE);
        // 执行脚本(另外还有文件读取方式
        //如:engine.eval(new FileReader("xx.js")));
        engine.eval("function sayName(){return champion;}");
        // 可有调用方法
        Invocable invoke = (Invocable) engine;
        // 调用方法，此处无参数列表
        String result = invoke.invokeFunction("sayName", null).toString();
        System.out.println(result);
    }
}
