package com.github.art.basis.array;

import org.junit.Test;

/**
 * 每一个数组元素的位置由数字编号，称为下标或者索引(index)。
 * 根据维度区分，有2种不同的数组：一维数组、多维数组
 * 数组的基本操作
     Insert - 在某个索引处插入元素
     Get - 读取某个索引处的元素
     Delete - 删除某个索引处的元素
     Size - 获取数组的长度
 */
public class MyArray {
    @Test
    public void go_change() {
        MyArrayInner arr = new MyArrayInner();
        arr.insert(13);
        arr.insert(34);
        arr.insert(90);
        arr.display();
        System.out.println(arr.search(190));
        System.out.println(arr.get(1));

        arr.change(0, 12);
        arr.display();
    }

    public class MyArrayInner {
        private long[] arr;
        //表示有效数据的长度
        private int elements;

        public MyArrayInner() {
            arr = new long[50];
        }

        public MyArrayInner(int maxsize) {
            arr = new long[maxsize];
        }

        /**
         * 添加数据
         */
        public void insert(long value) {
            arr[elements] = value;
            elements++;
        }

        /**
         * 显示数据
         */
        public void display() {
            System.out.print("[");
            for (int i = 0; i < elements; i++) {
                System.out.print(arr[i] + " ");
            }
            System.out.println("]");
        }

        /**
         * 查找数据
         */
        public int search(long value) {
            int i;
            for (i = 0; i < elements; i++) {
                if (value == arr[i]) {
                    break;
                }
            }

            if (i == elements) {
                return -1;
            } else {
                return i;
            }

        }

        /**
         * 查找数据，根据索引来查
         */
        public long get(int index) {
            if (index >= elements || index < 0) {
                throw new ArrayIndexOutOfBoundsException();
            } else {
                return arr[index];
            }
        }

        /**
         * 删除数据
         */
        public void delete(int index) {
            if (index >= elements || index < 0) {
                throw new ArrayIndexOutOfBoundsException();
            } else {
                for (int i = index; i < elements; i++) {
                    arr[index] = arr[index + 1];
                }
                elements--;
            }
        }

        /**
         * 更新数据
         */
        public void change(int index, int newvalue) {
            if (index >= elements || index < 0) {
                throw new ArrayIndexOutOfBoundsException();
            } else {
                arr[index] = newvalue;
            }
        }

    }
}

