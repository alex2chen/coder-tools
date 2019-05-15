package com.github.art.行为型模式.状态模式;

/**
 * Client
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class Client {
    public static void main(String[] args) throws InterruptedException {
        Context context = new Context(new RedState());
        while (true) {
            context.push();
            Thread.currentThread().sleep(2000);
        }
    }
}
