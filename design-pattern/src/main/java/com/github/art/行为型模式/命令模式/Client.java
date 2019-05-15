package com.github.art.行为型模式.命令模式;

/**
 * Client
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/25
 */
public class Client {
    public static void main(String[] args) {
        Receiver receiver = new Receiver();
        Command runCmd = new RunCommand(receiver);
        Command gunCmd = new GunCommand(receiver);

        Invoker invoker = new Invoker();
        invoker.excute(runCmd);

        invoker.excute(gunCmd);
    }
}
