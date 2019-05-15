package com.github.art.创建型模式.建造者;

/**
 * Client
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/19
 */
public class Client {
    public static void main(String[] args) {
        Director director = new Director();
        Builder builderLi = new ConcreteBuilderLi();
        Builder builderWang = new ConcreteBuilderWang();

        Computer computer1=director.buildAll(builderLi);
        computer1.showParts();

        Computer computer2=director.buildAll(builderWang);
        computer2.showParts();
        System.out.println("end");

    }


}
