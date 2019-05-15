package com.github.art.结构性模式.组合模式;

/**
 * AbstractGraphics
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/23
 */
public abstract class AbstractGraphics {
    protected String name;
    protected abstract void add(AbstractGraphics image);
    protected abstract void remove(AbstractGraphics image);
    protected abstract void draw();
}
