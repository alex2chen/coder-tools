package com.github.art.other;

import org.junit.Test;

/**
 * 杨辉三角
 */
public class 杨辉三角 {
    @Test
    public void go_drwap() {
        drawp(5);
    }

    private void drawp(int level) {
        int[][] arr = new int[level][level];
        int i, j;
        for (i = 0; i < level; i++) {
            for (j = 0; j <= i; j++) {
                if (j == 0 || i == j) {
                    arr[i][j] = 1;
                }
            }
        }
        for (i = 2; i < level; i++) {
            for (j = 1; j < i; j++) {
                arr[i][j] = arr[i - 1][j - 1] + arr[i - 1][j];
            }
        }
        for (i = 0; i < level; i++) {
            for (j = 0; j <= i; j++) {
                if (j == 0) {
                    int count = j + 1;
                    for (; count < level - i; count++) {
                        System.out.print(" ");
                    }
                }
                System.out.print(arr[i][j] + " ");
            }
            System.out.println("");
        }

    }

}
