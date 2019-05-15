package com.github.art.basis.sort;

import org.junit.Test;

import java.util.Arrays;

/**
 * 冒泡
 * 冒泡排序从小到大排序：一开始交换的区间为0~N-1，将第1个数和第2个数进行比较，前面大于后面，交换两个数，否则不交换。
 * 再比较第2个数和第三个数，前面大于后面，交换两个数否则不交换。依次进行，最大的数会放在数组最后的位置。
 * 然后将范围变为0~N-2，数组第二大的数会放在数组倒数第二的位置。依次进行整个交换过程，最后范围只剩一个数时数组即为有序。
 *
 * https://img-blog.csdn.net/20161009190728886
 */
public class BubbleSort {
    @Test
    public void go_sort() {
        int[] arr = new int[]{11, 2, 5, 4};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    private void sort(int[] arr) {
        int size = arr.length;
        int tmp = 0;
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            }
        }
    }

}
