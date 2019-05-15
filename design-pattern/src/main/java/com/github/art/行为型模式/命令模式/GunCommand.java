package com.github.art.行为型模式.命令模式;

/**
 * GunCommand
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/25
 */
public class GunCommand implements Command {
    private Receiver receiver;

    @Override
    public void exec() {
        receiver.action("滚");
    }

    public GunCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }
}
