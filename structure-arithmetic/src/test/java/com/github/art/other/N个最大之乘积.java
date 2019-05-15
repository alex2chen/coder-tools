package com.github.art.other;

import org.junit.Test;

import java.util.Scanner;

/**
 *
 * 给定一个无序数组，包含正数、负数和0，要求从中找出3个数的乘积，使得乘积最大，要求时间复杂度：O(n)，空间复杂度：O(1)
 */
public class N个最大之乘积 {
    @Test
    public void go_thirdSum() {
        System.out.println(getMaxSum(new int[]{3, 4, 6, -1, 4, 5}));
    }

    private int getMaxSum(int[] input) {
        int max1 = 0;
        int max2 = 0;
        int max3 = 0;
        int min1 = 0;
        int min2 = 0;
        for (int i = 0; i < input.length; i++) {
            if (input[i] > max1) {
                max3 = max2;
                max2 = max1;
                max1 = input[i];
            } else if (input[i] > max2) {
                max3 = max2;
                max2 = input[i];
            } else if (input[i] > max3) {
                max3 = input[i];
            }
            if (input[i] < min1) {
                min2 = min1;
                min1 = input[i];
            } else if (input[i] < min2) {
                min2 = input[i];
            }
        }
        System.out.println(max1);
        System.out.println(max2);
        System.out.println(max3);
        System.out.println(min1);
        System.out.println(min2);
        return (max1 * max2 * max3) > (max1 * min1 * min2) ? (max1 * max2 * max3) : (max1 * min1 * min2);
    }
}
