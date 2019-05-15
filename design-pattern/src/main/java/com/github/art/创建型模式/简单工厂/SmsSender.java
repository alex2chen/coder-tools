package com.github.art.创建型模式.简单工厂;

/**
 * SmsSender
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/22
 */
public class SmsSender implements Sender {
    @Override
    public void send() {
        System.out.println("短信已发送");
    }
}
