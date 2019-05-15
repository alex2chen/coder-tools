package com.github.art.行为型模式.状态模式;

/**
 * RedState
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class RedState extends State {
    @Override
    public void last(Context context) {
        context.setState(new GreenState());
    }

    @Override
    public void next(Context context) {
        context.setState(new BlueState());
    }

    @Override
    public String getState() {
        return "红色";
    }
}
