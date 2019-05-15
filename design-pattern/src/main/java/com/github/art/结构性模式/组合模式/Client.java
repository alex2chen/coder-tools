package com.github.art.结构性模式.组合模式;

/**
 * Client
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/23
 */
public class Client {
    public static void main(String[] args) {
        ComplexGraphics complexGraphics = new ComplexGraphics();
        complexGraphics.add(new Circle("一个圈"));
        complexGraphics.add(new Line("一条直线"));
        complexGraphics.draw();

        Tree tree=new Tree("root");
        TreeNode parent=new TreeNode("树干");
        TreeNode leaf=new TreeNode("树叶");
        parent.add(leaf);
        tree.root.add(parent);
    }
}
