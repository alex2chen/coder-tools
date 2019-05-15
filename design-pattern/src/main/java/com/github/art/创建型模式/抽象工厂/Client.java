package com.github.art.创建型模式.抽象工厂;

/**
 * Client
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/31
 */
public class Client {
    public static void main(String []args){
        AbstractFactory abstractFactory=new NanjiFactory();
        Yabo yabo=abstractFactory.createYabo();
        yabo.eat();
        Yazhou yazhou=abstractFactory.createYazhou();
        yazhou.print();

    }
}
