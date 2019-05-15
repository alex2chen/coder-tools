package com.github.art.basis.sort;

import org.junit.Test;

import java.util.Arrays;

/**
 * 选择排序
 * 一开始从0~n-1区间上选择一个最小值，将其放在位置0上，然后在1~n-1范围上选取最小值放在位置1上。重复过程直到剩下最后一个元素，数组即为有序。
 * 与冒泡排序相比，选择排序将必要的交换次数从 O(N*N)减少到 O(N)，但比较次数仍然保持为 O(N*N)。
 * http://img.mp.itc.cn/upload/20160923/c705c53489b8455090ccb67199465387_th.jpg
 */
public class SelectionSort {
    @Test
    public void go_sort() {
        int[] arr = new int[]{11, 2, 5, 4};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    private void sort(int[] arr) {
        int min = 0;
        int tmp = 0, size = arr.length;
        for (int i = 0; i < size - 1; i++) {
            min = i;//每趟排序最小值先等于第一个数，遍历剩下的数
            for (int j = i; j < size; j++) {
                if (arr[j] < arr[min]) {
                    min = j;
                }
            }
            tmp = arr[i];
            arr[i] = arr[min];
            arr[min] = tmp;
        }
    }
}
