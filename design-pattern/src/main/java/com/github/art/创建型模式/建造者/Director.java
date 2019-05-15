package com.github.art.创建型模式.建造者;

/**
 * Director
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/19
 */
public class Director {
    public Computer buildAll(Builder builder) {
        builder.buildPartCpu();
        builder.buildPartNetwork();
        return builder.getComputer();
    }
}
