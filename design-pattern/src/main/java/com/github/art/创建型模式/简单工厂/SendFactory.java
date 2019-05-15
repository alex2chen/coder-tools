package com.github.art.创建型模式.简单工厂;

/**
 * SendFactory
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/22
 */
public class SendFactory {
    public Sender produce(String type) {
        switch (type) {
            case "mail":
                return new MailSender();
            case "sms":
                return new SmsSender();
            default:
                throw new RuntimeException("no such");
        }
    }
}
