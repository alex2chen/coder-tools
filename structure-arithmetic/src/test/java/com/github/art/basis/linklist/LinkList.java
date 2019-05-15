package com.github.art.basis.linklist;

import org.junit.Assert;
import org.junit.Test;

/**
 * 单联表
 * Created by YT on 2018/3/27.
 */
public class LinkList {
    @Test
    public void go_op() {
        LinkListInner linkList = new LinkListInner();
        linkList.insertFirst(34);
        linkList.insertFirst(23);
        linkList.insertFirst(12);
        linkList.insertFirst(0);
        linkList.insertFirst(-1);
//		linkList.display();
//		linkList.deleteFirst();
//		linkList.display();
//		Node node = linkList.find(23);
//		node.display();
//        Node node1 = linkList.delete(0);
//        node1.display();
    }

    // * 1. 判断一个链表是否存在环儿<br>
    // * 2. 如果有环儿计算环儿的长度<br>
    // * 3. 找出环儿的连接点<br>
    @Test
    public void go_hashRing() {
        Node node1 = null;
        Node node2 = null;
        Node node3 = null;
        Node node4 = null;
        Assert.assertFalse(isExistLoop(node1));
        Assert.assertEquals(0, loopLength(node1));
        Assert.assertEquals(null, findLoopEntrance(node1));

        node1 = new Node(1);
        node2 = new Node(2);
        node3 = new Node(3);
        node1.next = node2;
        node2.next = node3;
        Assert.assertFalse(isExistLoop(node1));
        Assert.assertEquals(0, loopLength(node1));
        Assert.assertEquals(null, findLoopEntrance(node1));

        node1 = new Node(1);
        node1.next = node1;
        Assert.assertTrue(isExistLoop(node1));
        Assert.assertEquals(1, loopLength(node1));
        Assert.assertEquals(node1, findLoopEntrance(node1));

        node1 = new Node(1);
        node2 = new Node(2);
        node1.next = node2;
        node2.next = node1;
        Assert.assertTrue(isExistLoop(node1));
        Assert.assertEquals(2, loopLength(node1));
        Assert.assertEquals(node1, findLoopEntrance(node1));

        node1 = new Node(1);
        node2 = new Node(2);
        node3 = new Node(3);
        node1.next = node2;
        node2.next = node3;
        node3.next = node2;
        Assert.assertTrue(isExistLoop(node1));
        Assert.assertEquals(2, loopLength(node1));
        Assert.assertEquals(node2, findLoopEntrance(node1));

        node1 = new Node(1);
        node2 = new Node(2);
        node3 = new Node(3);
        node4 = new Node(4);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node2;
        Assert.assertTrue(isExistLoop(node1));
        Assert.assertEquals(3, loopLength(node1));
        Assert.assertEquals(node2, findLoopEntrance(node1));
    }

    /**
     * 判断一个链表是否存在环儿
     *
     * @param header
     * @return 是否存在环儿
     */
    public static boolean isExistLoop(Node header) {
        // 定义两个指针fast和slow,fast移动步长为2，slow移动步长为1
        Node fast = header;
        Node slow = header;

        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;

            //如果相遇则存在环儿，跳出
            if (fast == slow) {
                break;
            }
        }

        // 根据跳出循环的条件return
        if (fast == null || fast.next == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 计算有环儿链表的环儿长度<br>
     * fast, slow从碰撞点出发再次碰撞就是环儿的长度
     *
     * @param header
     * @return 返回环儿的长度
     */
    public static int loopLength(Node header) {
        // 如果不存在环儿，返回0
        if (!isExistLoop(header)) {
            return 0;
        }

        Node fast = header;
        Node slow = header;
        int length = 0;
        boolean begin = false;
        boolean again = false;

        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;

            // 超过两圈后停止计数，跳出循环
            if (fast == slow && again == true) {
                break;
            }

            // 超过一圈后开始计数
            if (fast == slow && again == false) {
                begin = true;
                again = true;
            }

            if (begin == true) {
                ++length;
            }
        }

        return length;
    }

    /**
     * 找出环儿的连接点<br>
     * 碰撞点到连接点的距离=头指针到连接点的距离<br>
     * 因此，分别从碰撞点、头指针开始走，相遇的那个点就是连接点<br>
     *
     * @param header
     * @return 环儿连接点
     */
    public static Node findLoopEntrance(Node header) {
        Node fast = header;
        Node slow = header;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                break;
            }
        }

        if (fast == null || fast.next == null) {
            return null;
        }

        slow = header;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }

        return slow;
    }

    /**
     * 链表，相当于火车
     */
    public class LinkListInner {
        //头结点
        private Node first;

        public LinkListInner() {
            first = null;
        }

        /**
         * 插入一个结点，在头结点后进行插入
         */
        public void insertFirst(long value) {
            Node node = new Node(value);
            node.next = first;
            first = node;
        }

        /**
         * 删除一个结点，在头结点后进行删除
         */
        public Node deleteFirst() {
            Node tmp = first;
            first = tmp.next;
            return tmp;
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
            Node previous = first;
            while (current.data != value) {
                if (current.next == null) {
                    return null;
                }
                previous = current;
                current = current.next;
            }

            if (current == first) {
                first = first.next;
            } else {
                previous.next = current.next;
            }
            return current;

        }
    }

    /**
     * 链结点，相当于是车厢
     */
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
