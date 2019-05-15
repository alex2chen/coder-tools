package com.github.art.basis.linklist;

import org.junit.Test;

/**
 * 双向链表
 * 双向链表既允许向后遍历，也允许向前遍历整个链表。
 * Created by YT on 2018/3/27.
 */
public class DoubleLinkList {
    @Test
    public void go_op() {
        DoubleLinkListInner dl = new DoubleLinkListInner();
        dl.insertLast(45);
        dl.insertLast(56);
        dl.insertLast(90);
        dl.display();
        while (!dl.isEmpty()) {
            dl.deleteFirst();
            dl.display();
        }
    }

    public class DoubleLinkListInner {
        //头结点
        private Node first;
        //尾结点
        private Node last;

        public DoubleLinkListInner() {
            first = null;
            last = null;
        }

        /**
         * 插入一个结点，在头结点后进行插入
         */
        public void insertFirst(long value) {
            Node node = new Node(value);
            if (isEmpty()) {
                last = node;
            } else {
                first.previous = node;
            }
            node.next = first;
            first = node;
        }

        /**
         * 插入一个结点，从尾结点进行插入
         */
        public void insertLast(long value) {
            Node node = new Node(value);
            if (isEmpty()) {
                first = node;
            } else {
                last.next = node;
                node.previous = last;
            }
            last = node;
        }

        /**
         * 删除一个结点，在头结点后进行删除
         */
        public Node deleteFirst() {
            Node tmp = first;
            if (first.next == null) {
                last = null;
            } else {
                first.next.previous = null;
            }
            first = tmp.next;
            return tmp;
        }

        /**
         * 删除结点，从尾部进行删除
         */
        public Node deleteLast() {
            Node tmp = last;
            if (first.next == null) {
                first = null;
            } else {
                last.previous.next = null;
            }
            last = last.previous;
            return last;
        }

        /**
         * 显示方法
         */
        public void display() {
            Node current = first;
            while (current != null) {
                current.display();
                current = current.next;
            }
            System.out.println();
        }

        /**
         * 查找方法
         */
        public Node find(long value) {
            Node current = first;
            while (current.data != value) {
                if (current.next == null) {
                    return null;
                }
                current = current.next;
            }
            return current;
        }

        /**
         * 删除方法，根据数据域来进行删除
         */
        public Node delete(long value) {
            Node current = first;
            while (current.data != value) {
                if (current.next == null) {
                    return null;
                }
                current = current.next;
            }

            if (current == first) {
                first = first.next;
            } else {
                current.previous.next = current.next;
            }
            return current;

        }

        /**
         * 判断是否为空
         */
        public boolean isEmpty() {
            return (first == null);
        }
    }

    public class Node {
        //数据域
        public long data;
        //指针域
        public Node next;
        public Node previous;

        public Node(long value) {
            this.data = value;
        }

        /**
         * 显示方法
         */
        public void display() {
            System.out.print(data + " ");
        }
    }
}
