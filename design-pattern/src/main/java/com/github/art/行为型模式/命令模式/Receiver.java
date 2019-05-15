package com.github.art.行为型模式.命令模式;

/**
 * Receiver
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/25
 */
public class Receiver {
    public void action(String cmd) {
        System.out.println(cmd + "已收到，开始执行");
    }
}
