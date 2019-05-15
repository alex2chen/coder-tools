package com.github.art.basis.array;

import org.junit.Test;

/**
 * 查找数组中第二小的元素
 */
public class SecondMinNumber {
    @Test
    public void go_print() {
        int arr[] = {12, 13, 1, 10, 34, 1};
        System.out.println(search(arr));
    }

    public int search(int[] arr) {
        int first, second, size = arr.length;
        if (size < 2) {
            return 0;
        }
        first = second = Integer.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            if (arr[i] < first) {//找最小的
                second = first;
                first = arr[i];
            } else if (arr[i] < second && arr[i] != first) {
                second = arr[i];//找第二小的
            }
        }
        return second;
    }
}
