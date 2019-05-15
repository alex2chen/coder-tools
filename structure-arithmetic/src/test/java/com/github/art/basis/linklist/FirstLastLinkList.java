package com.github.art.basis.linklist;

import org.junit.Test;

/**
 * 双端链表
 * 链表中保存着对最后一个链结点的引用
 * Created by YT on 2018/3/27.
 */
public class FirstLastLinkList {
    @Test
    public void go_op(){
        FirstLastLinkListInner fl = new FirstLastLinkListInner();
//		fl.insertFirst(34);
//		fl.insertFirst(56);
//		fl.insertFirst(67);
//		fl.display();
//
//		fl.deleteFirst();
//		fl.deleteFirst();
//		fl.display();
        fl.insertLast(56);
        fl.insertLast(90);
        fl.insertLast(12);
        fl.display();
        fl.deleteFirst();
        fl.display();
    }

    public class FirstLastLinkListInner{
        //头结点
        private Node first;
        //尾结点
        private Node last;

        public FirstLastLinkListInner() {
            first = null;
            last = null;
        }

        /**
         * 插入一个结点，在头结点后进行插入
         */
        public void insertFirst(long value) {
            Node node = new Node(value);
            if(isEmpty()) {
                last = node;
            }
            node.next = first;
            first = node;
        }

        /**
         * 插入一个结点，从尾结点进行插入
         */
        public void insertLast(long value) {
            Node node = new Node(value);
            if(isEmpty()) {
                first = node;
            } else {
                last.next = node;
            }
            last = node;
        }

        /**
         * 删除一个结点，在头结点后进行删除
         */
        public Node deleteFirst() {
            Node tmp = first;
            if(first.next == null) {
                last = null;
            }
            first = tmp.next;
            return tmp;
        }

        /**
         * 显示方法
         */
        public void display() {
            Node current = first;
            while(current != null) {
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
            while(current.data != value) {
                if(current.next == null) {
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
            Node previous = first;
            while(current.data != value) {
                if(current.next == null) {
                    return null;
                }
                previous = current;
                current = current.next;
            }

            if(current == first) {
                first = first.next;
            } else {
                previous.next = current.next;
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
