package com.github.art.结构性模式.桥接模式;

/**
 * DriverManager
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/23
 */
public abstract class DriverManager {
    protected Driver driver;

    public void openConection() {
        driver.open();
    }

    public void closeConection() {
        driver.close();
    }

    public void exec() {
        driver.exec();
    }

    public abstract void queryTest();
}
