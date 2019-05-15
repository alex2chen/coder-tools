package com.github.art.行为型模式.状态模式;

/**
 * State
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public abstract class State {
    public abstract void last(Context context);

    public abstract void next(Context context);

    public abstract String getState();
}
