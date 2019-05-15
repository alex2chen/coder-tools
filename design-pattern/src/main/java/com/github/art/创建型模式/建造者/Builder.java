package com.github.art.创建型模式.建造者;

/**
 * Builder
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/19
 */
public abstract class Builder {
    public abstract void buildPartCpu();

    public abstract void buildPartNetwork();

    public abstract Computer getComputer();
}
