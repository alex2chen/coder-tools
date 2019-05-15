package com.github.art.结构性模式.桥接模式;

/**
 * DriverManagerBridge
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/23
 */
public class DriverManagerBridge extends DriverManager {
    @Override
    public void exec() {
        System.out.println("s-----------");
        super.exec();
        System.out.println("e-----------");
    }

    @Override
    public void queryTest() {
        System.out.println("s------");
        this.openConection();
        this.exec();
        this.closeConection();
        System.out.println("e---------");
    }
}
