package com.github.art.结构性模式.组合模式;

/**
 * Line
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/23
 */
public class Line extends AbstractGraphics {
    public Line(String name) {
        this.name = name;
    }

    @Override
    protected void add(AbstractGraphics image) {
        throw new RuntimeException("不能向Line添加其他图形");
    }

    @Override
    protected void remove(AbstractGraphics image) {
        throw new RuntimeException("不能向Line移除其他图形");
    }

    @Override
    protected void draw() {
        System.out.println("开始绘制直线");
    }
}
