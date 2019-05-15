package com.github.art.hashtable;

import org.junit.Test;

import java.math.BigInteger;

/**
 * 哈希表是一种数据结构，它可以提供快速的插入和查找操作。哈希表是基于数组来实现的。
 * 通常设计索引很关键，见MyHashTableInner.hashCode
 * 为了解决索引冲突问题，引入开放地址法，链地址法（MyHashTableInner2）
 * Created by YT on 2018/3/28.
 */
public class MyHashTable {
    @Test
    public void go_simpleHashTable() {
        MyHashTableInner ht = new MyHashTableInner();
        //	System.out.println(ht.hashCode("a"));
        //	System.out.println(ht.hashCode("ct"));
        ht.insert(new Info("a", "张三"));
        ht.insert(new Info("ct", "李四"));
        ht.insert(new Info("b", "王五"));

        System.out.println(ht.find("a").getName());
        System.out.println(ht.find("ct").getName());
        System.out.println(ht.find("b").getName());

        ht.delete("b");
        System.out.println(ht.find("b").getName());
    }

    @Test
    public void go_indexfxied() {
        MyHashTableInner2 ht = new MyHashTableInner2();
        ht.insert(new Info("a", "张三"));
        ht.insert(new Info("ct", "李四"));
        ht.insert(new Info("b", "王五"));
        ht.insert(new Info("dt", "赵柳"));

        System.out.println(ht.find("a").getName());
        System.out.println(ht.find("ct").getName());
        System.out.println(ht.find("b").getName());
        System.out.println(ht.find("dt").getName());

        System.out.println(ht.delete("a").getName());
        System.out.println(ht.find("a").getName());
    }

    public class MyHashTableInner {
        private Info[] arr;

        public MyHashTableInner() {
            arr = new Info[100];
        }

        public MyHashTableInner(int maxSize) {
            arr = new Info[maxSize];
        }

        public void insert(Info info) {
            //获得关键字
            String key = info.getKey();
            //关键字所自定的哈希数
            int hashVal = hashCode(key);
            //如果这个索引已经被占用，而且里面是一个未被删除的数据
            while (arr[hashVal] != null && arr[hashVal].getName() != null) {
                //进行递加
                ++hashVal;
                //循环
                hashVal %= arr.length;
            }
            arr[hashVal] = info;
        }

        public Info find(String key) {
            int hashVal = hashCode(key);
            while (arr[hashVal] != null) {
                if (arr[hashVal].getKey().equals(key)) {
                    return arr[hashVal];
                }
                ++hashVal;
                hashVal %= arr.length;
            }
            return null;
        }

        public Info delete(String key) {
            int hashVal = hashCode(key);
            while (arr[hashVal] != null) {
                if (arr[hashVal].getKey().equals(key)) {
                    Info tmp = arr[hashVal];
                    tmp.setName(null);
                    return tmp;
                }
                ++hashVal;
                hashVal %= arr.length;
            }
            return null;
        }

        public int hashCode(String key) {
            //1.每个字符的asii码相加，但会有漏洞,重复率高
            //		int hashVal = 0;
            //		for(int i = key.length() - 1; i >= 0; i--) {
            //			int letter = key.charAt(i) - 96;
            //			hashVal += letter;
            //		}
            //		return hashVal;
            //2.幂的连乘，但数据会过大
            //            for(int i=key.length()-1; i>=0; i--) {
            //                int value = key.charAt(i) - 96;
            //                code += value * power27;
            //                power27 *= 27;
            //            }
            //3.压缩可选值(为了解决编码后的数据过大，对其进行取模运算)
            BigInteger hashVal = new BigInteger("0");
            BigInteger pow27 = new BigInteger("1");
            for (int i = key.length() - 1; i >= 0; i--) {
                int letter = key.charAt(i) - 96;
                BigInteger letterB = new BigInteger(String.valueOf(letter));
                hashVal = hashVal.add(letterB.multiply(pow27));
                pow27 = pow27.multiply(new BigInteger(String.valueOf(27)));
            }
            return hashVal.mod(new BigInteger(String.valueOf(arr.length))).intValue();
        }
    }

    public class Info {
        private String key;
        private String name;

        public Info(String key, String name) {
            this.key = key;
            this.name = name;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class MyHashTableInner2 {
        private LinkList[] arr;

        public MyHashTableInner2() {
            arr = new LinkList[100];
        }

        public void insert(Info info) {
            //获得关键字
            String key = info.getKey();
            //关键字所自定的哈希数
            int hashVal = hashCode(key);
            if (arr[hashVal] == null) {
                arr[hashVal] = new LinkList();
            }
            arr[hashVal].insertFirst(info);
        }

        public Info find(String key) {
            int hashVal = hashCode(key);
            return arr[hashVal].find(key).info;
        }

        public Info delete(String key) {
            int hashVal = hashCode(key);
            return arr[hashVal].delete(key).info;
        }

        public int hashCode(String key) {
            BigInteger hashVal = new BigInteger("0");
            BigInteger pow27 = new BigInteger("1");
            for (int i = key.length() - 1; i >= 0; i--) {
                int letter = key.charAt(i) - 96;
                BigInteger letterB = new BigInteger(String.valueOf(letter));
                hashVal = hashVal.add(letterB.multiply(pow27));
                pow27 = pow27.multiply(new BigInteger(String.valueOf(27)));
            }
            return hashVal.mod(new BigInteger(String.valueOf(arr.length))).intValue();
        }

        public class LinkList {
            //头结点
            private Node first;

            public LinkList() {
                first = null;
            }

            /**
             * 插入一个结点，在头结点后进行插入
             */
            public void insertFirst(Info info) {
                Node node = new Node(info);
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
             * 查找方法
             */
            public Node find(String key) {
                Node current = first;
                while (!key.equals(current.info.getKey())) {
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
            public Node delete(String key) {
                Node current = first;
                Node previous = first;
                while (!key.equals(current.info.getKey())) {
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

        public class Node {
            //数据域
            public Info info;
            //指针域
            public Node next;

            public Node(Info info) {
                this.info = info;
            }

        }
    }
}
