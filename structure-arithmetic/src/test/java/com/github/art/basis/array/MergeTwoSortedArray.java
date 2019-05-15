package com.github.art.basis.array;

import org.junit.Test;

import java.util.Arrays;

/**
 * 合并2个排序好的数组
 * Input :  arr1[] = { 1, 3, 4, 5}
 * arr2[] = {2, 4, 6, 8}
 * Output : arr3[] = {1, 2, 3, 4, 5, 6, 7, 8}
 * <p>
 * Input  : arr1[] = { 5, 8, 9}
 * arr2[] = {4, 7, 8}
 * Output : arr3[] = {4, 5, 7, 8, 8, 9}
 */
public class MergeTwoSortedArray {
    @Test
    public void go_print() {
        int[] arr1 = {1, 3, 5, 7};
        int[] arr2 = {2, 4, 6, 8};
        int[] result = marge(arr1, arr2);
        System.out.println(Arrays.toString(result));
    }

    public int[] marge(int[] arr1, int[] arr2) {
        int n1 = arr1.length, i = 0, n2 = arr2.length, j = 0, k = 0;
        int[] result = new int[n1 + n2];
        while (i < n1 && j < n2) {
            if (arr1[i] < arr2[j]) {
                result[k++] = arr1[i++];
            } else {
                result[k++] = arr2[j++];
            }
        }
        while (i < n1) {
            result[k++] = arr1[i++];
        }
        while (j < n2) {
            result[k++] = arr2[j++];
        }
        return result;
    }
}
