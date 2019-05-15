package com.github.art.basis.sort;

import com.google.common.base.Stopwatch;
import org.junit.Test;

import java.util.Arrays;

/**
 * 快速排序
 * 选择一个关键字，通常选择第一个元素或者最后一个元素，通过一趟扫描，将待排序列分成两部分，一部分比关键字小，另一部分大于等于关键字，此时关键字在其排
 * 好序后的正确位置，然后再用同样的方法递归地排序划分出来的两部分。
 * 关键字的选择：采用左端点做pivot(《算法导论》
 * <p>
 * 总结快速排序的思想：冒泡+二分+递归分治，慢慢体会。。。
 * <p>
 * <p>
 * <p>
 * <p>
 * 该如果优化呢？：特别是对随机数组的优化非常必要，http://jm.taobao.org/2010/09/08/252/
 * 1.pivot可以随机
 * 2.对于数组大小（较小）可以选择插入排序
 * 3.与pivot相同的数字交互到两端
 * <p>
 * https://img-blog.csdn.net/20161009191035090
 */
public class QuickSort {
    @Test
    public void go_sort() {
        //随机数组
        int[] arr = new int[]{11, 2, 5, 4, 12};
        Stopwatch stopwatch = Stopwatch.createStarted();
        quickSort(arr, 0, arr.length - 1);
        System.out.println(stopwatch);
        System.out.println(Arrays.toString(arr));
    }

    //划分数组
    private int partition(int[] arr, int left, int right) {
        //设置关键字
        int pivotKey = arr[left];//11

        while (left < right) {
            while (left < right && arr[right] >= pivotKey)
                right--;//3
            arr[left] = arr[right]; //把小的移动到左边,4, 2, 5, 11,12
            while (left < right && arr[left] <= pivotKey)
                left++;//3
            arr[right] = arr[left]; //把大的移动到右边,4, 2, 5, 11,12
        }
        arr[left] = pivotKey; //最后把pivot赋值到中间
        return left;
    }

    /**
     * 递归划分子序列
     *
     * @param arr
     * @param left
     * @param right
     */
    private void quickSort(int[] arr, int left, int right) {
        if (left >= right)
            return;
        int pivotPos = 0;
        //获得关键字
        int pivotKey = arr[left];
        while (left < right) {
            while (left < right && arr[right] >= pivotKey) {
                right--;
            }
            arr[left] = arr[right];//把小的移动到左边
            while (left < right && arr[left] <= pivotKey) {
                left++;
            }
            arr[right] = arr[left];
        }
        arr[left] = pivotKey; //最后把pivot赋值到中间
        pivotPos = left;
        //左边排序
        quickSort(arr, left, pivotPos - 1);
        //右边排序
        quickSort(arr, pivotPos + 1, right);
    }
}
