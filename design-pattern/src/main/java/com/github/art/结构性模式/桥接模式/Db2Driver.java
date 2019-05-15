package com.github.art.结构性模式.桥接模式;

/**
 * Db2Driver
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/23
 */
public class Db2Driver implements Driver {
    @Override
    public void open() {
        System.out.println("db2 is open");
    }

    @Override
    public void close() {
        System.out.println("db2 is close");
    }

    @Override
    public void exec() {
        System.out.println("db2 is exec");
    }
}
