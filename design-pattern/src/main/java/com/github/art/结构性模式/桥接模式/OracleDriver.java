package com.github.art.结构性模式.桥接模式;

/**
 * OracleDriver
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/23
 */
public class OracleDriver implements Driver {
    @Override
    public void open() {
        System.out.println("oracle is open");
    }

    @Override
    public void close() {
        System.out.println("oracle is close");
    }

    @Override
    public void exec() {
        System.out.println("oracle is exec");
    }
}
