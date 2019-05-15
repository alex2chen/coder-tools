package com.github.art.结构性模式.外观模式;

/**
 * ComputerFacade
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/22
 */
public class ComputerFacade {
    private Cpu cpu;
    private Memory memory;

    public void initSubSystem() {
        this.cpu = new Cpu();
        this.memory = new Memory();
    }

    public void run() {
        this.cpu.startup();
        this.memory.startup();

    }

    public void exit() {
        this.cpu.shutdown();
        this.memory.shutdown();
    }


}
