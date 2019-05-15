package com.github.art.basis.array;

import org.junit.Test;

/**
 * 查找第一个没有重复的数组元素
 * Input : -1 2 -1 3 2
 * Output : 3
 * repeat is : 3
 * <p>
 * Input : 9 4 9 6 7 4
 * Output : 6
 * Output : 6
 */
public class NonRepeateNumber {
    @Test
    public void go_print() {
        int arr[] = {9, 4, 9, 6, 7, 4};
        System.out.println(firstNonRepeate(arr));
    }

    public int firstNonRepeate(int[] arr) {
        int size = arr.length;
        for (int i = 0; i < size; i++) {
            int j;
            for (j = 0; j < size; j++) {
                if (i != j && arr[i] == arr[j]) {
                    break;
                }
            }
            if (j == size) return arr[i];
        }
        return -1;
    }
}
