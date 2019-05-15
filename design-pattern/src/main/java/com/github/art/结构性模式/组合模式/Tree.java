package com.github.art.结构性模式.组合模式;

/**
 * Tree
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/23
 */
public class Tree {
    protected TreeNode  root;
    public Tree(String name){
        root=new TreeNode(name);
    }

}
