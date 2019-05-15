package com.github.art.结构性模式.组合模式;

import java.util.Enumeration;
import java.util.Vector;

/**
 * TreeNode
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/23
 */
public class TreeNode {
    private String name;
    protected TreeNode parent;
    private Vector<TreeNode> children;

    public TreeNode(String name) {
        this.name = name;
        children = new Vector<>();
    }

    public void add(TreeNode node) {
        children.add(node);
    }

    public void remove(TreeNode node) {
        children.remove(node);
    }

    public Enumeration<TreeNode> getChildren() {
        return children.elements();
    }
}
