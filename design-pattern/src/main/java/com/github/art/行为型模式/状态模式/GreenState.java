package com.github.art.行为型模式.状态模式;

/**
 * GreenState
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class GreenState extends State {

    @Override
    public void last(Context context) {
        context.setState(new BlueState());
    }

    @Override
    public void next(Context context) {
        context.setState(new RedState());
    }

    @Override
    public String getState() {
        return "绿色";
    }
}
