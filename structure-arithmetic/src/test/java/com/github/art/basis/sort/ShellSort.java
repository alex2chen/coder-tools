package com.github.art.basis.sort;

import org.junit.Test;

import java.util.Arrays;

/**
 * 希尔排序
 * 希尔排序是插入排序的一种高效率的实现，也叫缩小增量排序。如果序列是基本有序的，使用直接插入排序效率就非常高。希尔排序就利用了这个特点。
 * 基本思想是：先将整个待排记录序列分割成为若干子序列分别进行直接插入排序，
 * 间隔的计算：间隔h的初始值为 1，通过 h = 3*h + 1 来循环计算，知道该间隔大于数组的大小时停止。最大间隔为不大于数组大小的最大值。
 * 间隔的减少：通过公式 h = (h - 1)/3 来计算。
 * https://img-blog.csdn.net/20170804062400495?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveXVzaGl5aTY0NTM=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast
 */
public class ShellSort {
    @Test
    public void go_sort() {
        int[] arr = new int[]{11, 2, 5, 4, 1, 4, 5, 30, 34};
        shellSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    private void shellSort(int[] arr) {
        int size = arr.length;
        int grep = size, j = 0, temp = 0;
        do {
            grep = grep / 3 + 1;
            for (int i = grep; i < size; i++) {//插入排序start....
                if (i - grep >= 0 && arr[i - grep] > arr[i]) {
                    temp = arr[i];
                    for (j = i - grep; j >= 0 && arr[j] > temp; j -= grep) {
                        arr[j + grep] = arr[j];
                    }
                    arr[j + grep] = temp;
                }
            }                               //插入排序end....
        } while (grep > 1);
    }
}
