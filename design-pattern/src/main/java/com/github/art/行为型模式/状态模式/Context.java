package com.github.art.行为型模式.状态模式;

/**
 * Context
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class Context {
    private State state;

    public Context(State state) {
        this.setState(state);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        notifyAction(state);
        this.state = state;
    }

    public void notifyAction(State state) {
        System.out.println("当前灯的状态为：" + state.getState());
    }

    /**
     *
     */
    public void pull() {
        state.next(this);
//        System.out.println(state.getState());
    }

    /**
     *
     */
    public void push() {
        state.last(this);
//        System.out.println(state.getState());
    }
}
