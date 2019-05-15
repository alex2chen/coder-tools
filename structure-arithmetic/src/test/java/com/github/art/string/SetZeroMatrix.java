package com.github.art.string;

import org.junit.Test;

/**
 * 在矩阵中，如果某个位置为0，怎将其所在的行和列都置为0
 */
public class SetZeroMatrix {
    @Test
    public void go_padding() {
        int[][] matrix = {{1, 2, 3, 4}, {2, 3, 4, 5}, {6, 5, 3, 4},
                {8, 6, 9, 0}, {4, 7, 0, 1}};

        for (int i = 0; i < matrix.length; i++) {
            String matrixStr = "";
            for (int j = 0; j < matrix[i].length; j++) {
                matrixStr += matrix[i][j] + "  ";
            }
            System.out.println(matrixStr);
        }

        System.out.println("Set Zero");
        zeroGood(matrix);

        for (int i = 0; i < matrix.length; i++) {
            String matrixStr = "";
            for (int j = 0; j < matrix[i].length; j++) {
                matrixStr += matrix[i][j] + "  ";
            }
            System.out.println(matrixStr);
        }
    }

    private void zeroGood(int[][] matrix) {
        boolean row[] = new boolean[matrix.length];
        boolean column[] = new boolean[matrix[0].length];

        int i, j;
        for (i = 0; i < matrix.length; i++) {
            for (j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0) {
                    row[i] = true;
                    column[j] = true;
                }
            }
        }

        for (i = 0; i < matrix.length; i++) {
            for (j = 0; j < matrix[i].length; j++) {
                if (row[i] || column[j]) {
                    matrix[i][j] = 0;
                }
            }
        }
    }
}
