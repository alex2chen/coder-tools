package com.github.art.结构性模式.装饰模式;

/**
 * Client
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/22
 */
public class Client {
    public static void main(String []args){
        Phone phone=new ApplePhone();
        //贴膜
        Decorator decorator=new Sticker(phone);
        decorator.print();

        //挂件
        Decorator decorator1=new Accessories(phone);
        decorator1.print();

    }
}
