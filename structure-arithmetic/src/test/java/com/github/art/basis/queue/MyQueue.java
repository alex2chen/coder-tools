package com.github.art.basis.queue;

import org.junit.Test;

/**
 * 列队类，FIFO(First in First Out)
 * 队列的基本操作
     Enqueue — 在队列末尾插入元素
     Dequeue — 将队列第一个元素删除
     isEmpty — 查询队列是否为空
     Top — 返回队列的第一个元素
 */
public class MyQueue {
    @Test
    public void go_op() {
        MyQueueInner mq = new MyQueueInner(4);
        mq.insert(23);
        mq.insert(45);
        mq.insert(13);
        mq.insert(1);
        System.out.println(mq.isFull());
        System.out.println(mq.isEmpty());
        System.out.println(mq.peek());
        while (!mq.isEmpty()) {
            System.out.print(mq.remove() + " ");
        }
        System.out.println();
        mq.insert(23);
        mq.insert(45);
        mq.insert(13);
        mq.insert(1);
        while (!mq.isEmpty()) {
            System.out.print(mq.remove() + " ");
        }
    }

    public class MyQueueInner {
        //底层使用数组
        private long[] arr;
        //有效数据的大小
        private int elements;
        //队头
        private int front;
        //队尾
        private int end;

        /**
         * 默认构造方法
         */
        public MyQueueInner() {
            arr = new long[10];
            elements = 0;
            front = 0;
            end = -1;
        }

        /**
         * 带参数的构造方法，参数为数组的大小
         */
        public MyQueueInner(int maxsize) {
            arr = new long[maxsize];
            elements = 0;
            front = 0;
            end = -1;
        }

        /**
         * 添加数据,从队尾插入
         */
        public void insert(long value) {
            arr[++end] = value;
            elements++;
        }

        /**
         * 删除数据，从队头删除
         */
        public long remove() {
            elements--;
            return arr[front++];
        }

        /**
         * 查看数据，从队头查看
         */
        public long peek() {
            return arr[front];
        }

        /**
         * 判断是否为空
         */
        public boolean isEmpty() {
            return elements == 0;
        }

        /**
         * 判断是否满了
         */
        public boolean isFull() {
            return elements == arr.length;
        }
    }

}
