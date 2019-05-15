package com.github.art.结构性模式.桥接模式;

/**
 * MysqlDriver
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/23
 */
public class MysqlDriver implements Driver {
    @Override
    public void open() {
        System.out.println("mysql open");
    }

    @Override
    public void close() {
        System.out.println("mysql close");
    }

    @Override
    public void exec() {
        System.out.println("mysql exec");
    }
}
