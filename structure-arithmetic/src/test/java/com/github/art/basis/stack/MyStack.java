package com.github.art.basis.stack;

import org.junit.Test;

/**
 * 撤回，即Ctrl+Z，是我们最常见的操作之一，大多数应用都会支持这个功能。你知道它是怎么实现的吗？
 * 答案是这样的：把之前的应用状态(限制个数)保存到内存中，最近的状态放到第一个。
 *
 * 栈中的元素采用LIFO (Last In First Out)，即后进先出
 * 栈的基本操作
     Push — 在栈的最上方插入元素
     Pop — 返回栈最上方的元素，并将其删除
     isEmpty — 查询栈是否为空
     Top — 返回栈最上方的元素，并不删除
 */
public class MyStack {
    @Test
    public void go_op() {
        MyStackInner ms = new MyStackInner(4);
        ms.push(23);
        ms.push(12);
        ms.push(1);
        ms.push(90);
        System.out.println(ms.isEmpty());
        System.out.println(ms.isFull());

        System.out.println(ms.peek());
        System.out.println(ms.peek());

        while (!ms.isEmpty()) {
            System.out.print(ms.pop() + ",");
        }

        System.out.println(ms.isEmpty());
        System.out.println(ms.isFull());
    }

    public static class MyStackInner {
        //底层实现是一个数组
        private long[] arr;
        private int top;

        /**
         * 默认的构造方法
         */
        public MyStackInner() {
            arr = new long[10];
            top = -1;
        }

        /**
         * 带参数构造方法，参数为数组初始化大小
         */
        public MyStackInner(int maxsize) {
            arr = new long[maxsize];
            top = -1;
        }

        /**
         * 添加数据
         */
        public void push(int value) {
            arr[++top] = value;
        }

        /**
         * 移除数据
         */
        public long pop() {
            return arr[top--];
        }

        /**
         * 查看数据
         */
        public long peek() {
            return arr[top];
        }

        /**
         * 判断是否为空
         */
        public boolean isEmpty() {
            return top == -1;
        }

        /**
         * 判断是否满了
         */
        public boolean isFull() {
            return top == arr.length - 1;
        }
    }

}
