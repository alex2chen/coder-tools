package com.github.art.行为型模式.命令模式;

/**
 * Invoker
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/25
 */
public class Invoker {
    private Command command;

    public void excute(Command command) {
        this.command=command;
        this.command.exec();
    }
}
