package com.github.art.tree;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.Optional;

/**
 * 二叉树类
 * 有序数组插入数据项和删除数据项太慢
 * 链表查找数据太慢
 * 树中能快速地查找、插入和删除数据项
 * 中序遍历(可以实现排序)
 * 二叉数判断:对所有节点来说:所有左子树节点都小于等于其根节点;所有右子树节点都大于其根节点
 * 平衡树检查：此处平衡树的定义是两棵子树的高度差不超过1
 * 有序的数组转为树
 */
public class BinaryTree {
    @Test
    public void go_op() {
        BinaryTreeInner tree = new BinaryTreeInner();
        tree.insert(10, "James");
        tree.insert(20, "YAO");
        tree.insert(15, "Kobi");
        tree.insert(3, "Mac");
        System.out.println(String.format("       %s", getName(tree.root)));
        System.out.println(String.format("   %s       %s", getName(tree.root.leftChild), getName(tree.root.rightChild)));
        System.out.println(String.format("%s    %s  %s   %s",
                getName(tree.root.leftChild == null || tree.root.leftChild.leftChild == null ? null : tree.root.leftChild.leftChild),
                getName(tree.root.leftChild == null || tree.root.leftChild.rightChild == null ? null : tree.root.leftChild.rightChild),
                getName(tree.root.rightChild == null || tree.root.rightChild.leftChild == null ? null : tree.root.rightChild.leftChild),
                getName(tree.root.rightChild == null || tree.root.rightChild.rightChild == null ? null : tree.root.rightChild.rightChild)));

        //  System.out.println(tree.root.data);
        //	System.out.println(tree.root.rightChild.data);
        //	System.out.println(tree.root.rightChild.leftChild.data);
        //	System.out.println(tree.root.leftChild.data);
        Node node = tree.find(3);
        System.out.println("查找key=3, " + node.sData);
        System.out.println("前序遍历:先访问根节点，再前序遍历左子树，最后前序遍历右子树");
        tree.frontOrder(tree.root);
        System.out.println("\r\n中序遍历:中序遍历：先中序遍历左子树，再访问根节点，最后中序遍历右子树");
        tree.inOrder(tree.root);
        System.out.println("\r\n后序遍历：先后序遍历左子树，再后序遍历右子树，最后访问根节点");
        tree.afterOrder(tree.root);

        tree.delete(90);
    }

    @Test
    public void go_checkBST() {
        Node root = new Node(10);
        root.leftChild = new Node(8);// new Node(11);
        root.rightChild = new Node(20);
        System.out.println(checkBSTMinMax(root, Integer.MIN_VALUE, Integer.MAX_VALUE));
    }
    @Test
    public void go_checkBalanceTree(){
        Node root = new Node(10);
        root.leftChild = new Node(8);
        //root.rightChild = new Node(20);
        System.out.println(isBalanced(root));
    }
    @Test
    public void go_arrayConvertTree() {
        //将一个有序的数组变成一个最小高度的二叉查找树
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Node node = insertNodeFromArray(array, 0, array.length - 1);
        System.out.println(JSON.toJSONString(node));
    }

    private boolean checkBSTMinMax(Node root, int min, int max) {
        if (root == null) {
            return true;
        }
        if (root.key > max || root.key <= min) {
            return false;
        }
        if (!checkBSTMinMax(root.leftChild, min, root.key) || !checkBSTMinMax(root.rightChild, root.key, max)) {
            return false;
        }

        return true;
    }
    public boolean isBalanced(Node root){
        if(root == null){
            return true;
        }
        int heightDiff = getHeight(root.leftChild) - getHeight(root.rightChild);
        if(Math.abs(heightDiff) > 1){
            return false;
        }else{
            return isBalanced(root.leftChild) && isBalanced(root.rightChild);
        }
    }
    private int getHeight(Node root) {
        if (root == null) {
            return 0;
        }
        return Math.max(getHeight(root.leftChild), getHeight(root.rightChild)) + 1;
    }

    public Node insertNodeFromArray(int[] array, int start, int end) {
        if (end < start) {
            return null;
        }
        int middle = (start + end) / 2;
        Node treeNode = new Node(array[middle]);
        treeNode.leftChild = insertNodeFromArray(array, start, middle - 1);
        treeNode.rightChild = insertNodeFromArray(array, middle + 1, end);
        return treeNode;
    }

    private String getName(Node node) {
        return Optional.ofNullable(node).map(x -> x.key + "(" + x.sData + ")").orElse("xx");
    }

    public class BinaryTreeInner {
        //根节点
        private Node root;

        /**
         * 插入节点
         */
        public void insert(int key, String sValue) {
            //封装节点
            Node newNode = new Node(key, sValue);
            //引用当前节点
            Node current = root;
            //引用父节点
            Node parent;
            //如果root为null，也就是第一插入的时候
            if (root == null) {
                root = newNode;
                return;
            } else {
                while (true) {
                    //父节点指向当前节点
                    parent = current;
                    //如果当前指向的节点数据比插入的要大,则向左走
                    if (current.key > key) {
                        current = current.leftChild;
                        if (current == null) {
                            parent.leftChild = newNode;
                            return;
                        }
                    } else {
                        current = current.rightChild;
                        if (current == null) {
                            parent.rightChild = newNode;
                            return;
                        }
                    }
                }
            }
        }

        /**
         * 查找节点
         */
        public Node find(long key) {
            //引用当前节点，从根节点开始
            Node current = root;
            //循环，只要查找值不等于当前节点的数据项
            while (current.key != key) {
                //进行比较，比较查找值和当前节点的大小
                if (current.key > key) {
                    current = current.leftChild;
                } else {
                    current = current.rightChild;
                }
                //如果查找不到
                if (current == null) {
                    return null;
                }
            }
            return current;
        }

        /**
         * 前序遍历
         */
        public void frontOrder(Node localNode) {
            if (localNode != null) {
                //访问根节点
                System.out.print(">>>" + getName(localNode));
                //前序遍历左子树
                frontOrder(localNode.leftChild);
                //前序遍历右子树
                frontOrder(localNode.rightChild);
            }
        }

        /**
         * 中序遍历
         */
        public void inOrder(Node localNode) {
            if (localNode != null) {
                //中序遍历左子树
                inOrder(localNode.leftChild);
                //访问根节点
                System.out.print(">>>" + getName(localNode));
                //中旬遍历右子树
                inOrder(localNode.rightChild);
            }
        }

        /**
         * 后序遍历
         */
        public void afterOrder(Node localNode) {
            if (localNode != null) {
                //后序遍历左子树
                afterOrder(localNode.leftChild);
                //后序遍历右子树
                afterOrder(localNode.rightChild);
                //访问根节点
                System.out.print(">>>" + getName(localNode));
            }
        }

        //删除节点
        public boolean delete(long value) {
            //引用当前节点，从根节点开始
            Node current = root;

            //应用当前节点的父节点
            Node parent = root;
            //是否为左节点
            boolean isLeftChild = true;

            while (current.key != value) {
                parent = current;
                //进行比较，比较查找值和当前节点的大小
                if (current.key > value) {
                    current = current.leftChild;
                    isLeftChild = true;
                } else {
                    current = current.rightChild;
                    isLeftChild = false;
                }
                //如果查找不到
                if (current == null) {
                    return false;
                }
            }

            //删除叶子节点，也就是该节点没有子节点
            if (current.leftChild == null && current.rightChild == null) {
                if (current == root) {
                    root = null;
                } else if (isLeftChild) {
                    parent.leftChild = null;
                } else {
                    parent.rightChild = null;
                }
            } else if (current.rightChild == null) {
                if (current == root) {
                    root = current.leftChild;
                } else if (isLeftChild) {
                    parent.leftChild = current.leftChild;
                } else {
                    parent.rightChild = current.leftChild;
                }
            } else if (current.leftChild == null) {
                if (current == root) {
                    root = current.rightChild;
                } else if (isLeftChild) {
                    parent.leftChild = current.rightChild;
                } else {
                    parent.rightChild = current.rightChild;
                }
            } else {
                Node successor = getSuccessor(current);
                if (current == root) {
                    root = successor;
                } else if (isLeftChild) {
                    parent.leftChild = successor;
                } else {
                    parent.rightChild = successor;
                }
                successor.leftChild = current.leftChild;
            }

            return true;


        }

        public Node getSuccessor(Node delNode) {
            Node successor = delNode;
            Node successorParent = delNode;
            Node current = delNode.rightChild;

            while (current != null) {
                successorParent = successor;
                successor = current;
                current = current.leftChild;
            }

            if (successor != delNode.rightChild) {
                successorParent.leftChild = successor.rightChild;
                successor.rightChild = delNode.rightChild;
            }
            return successor;
        }
    }

    /**
     * 二叉树节点
     */
    public class Node {
        //数据项
        public int key;
        //数据项
        public String sData;
        //左子节点
        public Node leftChild;
        //右子节点
        public Node rightChild;

        public Node(int data) {
            this.key = data;
        }

        public Node(int data, String sData) {
            this.key = data;
            this.sData = sData;
        }

    }
}
