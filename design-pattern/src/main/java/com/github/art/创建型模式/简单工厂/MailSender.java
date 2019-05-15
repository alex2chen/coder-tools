package com.github.art.创建型模式.简单工厂;

/**
 * MailSender
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/22
 */
public class MailSender implements Sender {
    @Override
    public void send() {
        System.out.println("邮件已发送");
    }
}
