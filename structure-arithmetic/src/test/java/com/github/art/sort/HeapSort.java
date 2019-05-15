package com.github.art.sort;

import org.junit.Test;

import java.util.Arrays;

/**
 * 堆排序是利用堆这种数据结构而设计的一种排序算法，堆排序是一种选择排序，它的最坏，最好，平均时间复杂度均为O(nlogn)，它也是不稳定排序。
 * 堆是具有以下性质的完全二叉树：每个结点的值都大于或等于其左右孩子结点的值，称为大顶堆；或者每个结点的值都小于或等于其左右孩子结点的值，称为小顶堆。
 * 大顶堆：arr[i] >= arr[2i+1] && arr[i] >= arr[2i+2]
 * 小顶堆：arr[i] <= arr[2i+1] && arr[i] <= arr[2i+2]
 * 注意：如果想升序排序就使用大顶堆，反之使用小顶堆。
 * <p>
 * Created by YT on 2018/3/28.
 */
public class HeapSort {
    @Test
    public void go_sort() {
        int[] arr = {10, 7, 43, 5, 4, 52, 2, 1};
        heapSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public void heapSort(int[] arr) {
        if (arr == null || arr.length == 0) return;
        // 1.建堆>大顶堆
        for (int i = arr.length / 2; i >= 0; i--) {
            heapAdjust(arr, i, arr.length);
        }
        // 3.堆排序>小顶堆
        for (int j = arr.length - 1; j >= 0; j--) {
            swap(arr, 0, j);//将堆顶元素与末尾元素进行交换
            heapAdjust(arr, 0, j);//重新对堆进行调整
        }
    }
    //2.调整堆：从第一个非叶子结点从下至上，从右至左调整结构
    private void heapAdjust(int[] arr, int start, int length) {
        int temp = arr[start];//先取出当前元素i
        for (int k = start * 2 + 1; k < length; k = k * 2 + 1) {//从i结点的左子结点开始，也就是2i+1处开始
            if (k + 1 < length && arr[k] < arr[k + 1]) {//如果左子结点小于右子结点，k指向右子结点
                k++;
            }
            if (arr[k] > temp) {//如果子节点大于父节点，将子节点值赋给父节点（不用进行交换）
                arr[start] = arr[k];
                start = k;
            } else {
                break;
            }
        }
        arr[start] = temp; //插入正确的位置
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
