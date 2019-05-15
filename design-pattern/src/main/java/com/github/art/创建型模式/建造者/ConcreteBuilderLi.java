package com.github.art.创建型模式.建造者;

/**
 * ConcreteBuilderLi
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/19
 */
public class ConcreteBuilderLi extends Builder {
    public Computer pc = new Computer();

    public void buildPartCpu() {
        System.out.println("组装cpu");
        pc.add("cpu");
    }

    public void buildPartNetwork() {
        System.out.println("组装网络");
        pc.add("network");
    }

    public Computer getComputer() {
        return pc;
    }
}
