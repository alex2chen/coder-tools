package com.github.art.basis.array;

import org.junit.Test;

/**
 * 有序数组
 * 有序数组提高了查询的效率（方便使用二分查找），但并没有提高删除和插入元素的效率。
 *
 */
public class MyOrderArray {
    @Test
    public void go_insert() {
        MyOrderArrayInner arr = new MyOrderArrayInner();
        arr.insert(90);
        arr.insert(30);
        arr.insert(80);
        arr.insert(10);
        arr.display();

        System.out.println(arr.binarySearch(30));
    }

    public class MyOrderArrayInner {
        private long[] arr;
        //表示有效数据的长度
        private int elements;

        public MyOrderArrayInner() {
            arr = new long[50];
        }

        public MyOrderArrayInner(int maxsize) {
            arr = new long[maxsize];
        }

        /**
         * 添加数据
         */
        public void insert(long value) {
            int i;
            for (i = 0; i < elements; i++) {
                if (arr[i] > value) {
                    break;
                }
            }

            for (int j = elements; j > i; j--) {
                arr[j] = arr[j - 1];
            }
            arr[i] = value;
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
         * 二分法查找数据
         */
        public int binarySearch(long value) {
            int middle = 0;
            int low = 0;
            int pow = elements;

            while (true) {
                middle = (pow + low) / 2;
                if (arr[middle] == value) {
                    return middle;
                } else if (low > pow) {
                    return -1;
                } else {
                    if (arr[middle] > value) {
                        pow = middle - 1;
                    } else {
                        low = middle + 1;
                    }
                }
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

