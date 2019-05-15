package com.github.art.basis.sort;

import org.junit.Test;

import java.util.Arrays;

/**
 * 插入排序
 * <p>
 * 插入排序从小到大排序：首先位置1上的数和位置0上的数进行比较，如果位置1上的数大于位置0上的数，将位置0上的数向后移一位，将1插入到0位置，否则不处理。位置k上的数和之前的数依次进行比较，如果位置K上的数更大，将之前的数向后移位，最后将位置k上的数插入不满足条件点，反之不处理。。
 * 相信大家都有过打扑克牌的经历，特别是牌数较大的。就是拿到一张牌，找到一个合适的位置插入。这个原理其实和插入排序是一样的
 * 注意在插入一个数的时候要保证这个数前面的数已经有序
 * <p>
 * https://img-blog.csdn.net/20161009190855230
 */
public class InsertSort {
    @Test
    public void go_sort() {
        int[] arr = new int[]{11, 2, 5, 4};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    private void sort(int[] arr) {
        int temp = 0, j = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1] > arr[i]) {
                temp = arr[i];
                for (j = i - 1; j >= 0 && arr[j] > temp; j--) {
                    arr[j + 1] = arr[j];
                }
                arr[j + 1] = temp;
            }
        }

    }

}
