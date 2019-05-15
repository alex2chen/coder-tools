package com.github.art.search;

/**
 * Created by YT on 2018/4/3.
 */
public class BinarySearch {

    /**
     * 使用循环的方式实现二分查找
     *
     * @param array
     * @param value
     * @return
     */
    public int searchCirculation(int[] array, int value) {
        int low = 0;
        int high = array.length - 1;
        int middle;

        while (low <= high) {
            middle = (low + high) / 2;
            if (value < array[middle]) {
                high = middle - 1;
            } else if (value > array[middle]) {
                low = middle + 1;
            } else {
                return array[middle];
            }
        }
        return 0;
    }

    /**
     * 使用递归的方式实现二分查找
     *
     * @param array
     * @param value
     * @return
     */
    public int searchRecursive(int[] array, int value) {
        return searchRecursive(array, value, 0, array.length - 1);
    }

    private int searchRecursive(int[] array, int value, int low, int high) {
        if (high < low) {
            return 0;
        }
        int middle = (low + high) / 2;

        if (value < array[middle]) {
            return searchRecursive(array, value, low, middle - 1);
        } else if (value > array[middle]) {
            return searchRecursive(array, value, middle + 1, high);
        } else {
            return array[middle];
        }
    }

}
