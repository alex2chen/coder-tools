package com.github.jvm.javax;

import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * @author alex.chen
 * @Description
 * @date 2016/2/1
 */
public class JNDI_unit {
    @Test
    public void initialContext() throws NamingException {
        Properties env = new Properties();
        env.setProperty("name", "alex");
        //通过JNDI api 初始化上下文
        InitialContext initialContext = new InitialContext(env);
        initialContext.createSubcontext("/sys");
        initialContext.createSubcontext("/sys/service");
        initialContext.rebind("/sys/service/getname", "I am getname");
        initialContext.rebind("/sys/service/getage", "I am getage");

        Context context = (Context) initialContext.lookup("sys");
        Object str_Getname = context.lookup("service/getname");
        System.out.println("/sys/service/getname:" + str_Getname);
        Context context1 = (Context) initialContext.lookup("sys/service");
        initialContext.bind("/sys/service2", "this is service2");
        context1.rename("/sys/service2", "this service2 is changed");
        Object str_server2 = context1.lookup("service2");
        System.out.println("/sys/service2:" + str_server2);
    }
}
