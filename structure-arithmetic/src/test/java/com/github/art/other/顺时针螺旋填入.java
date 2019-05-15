package com.github.art.other;

import org.junit.Test;

/**
 * 顺时针螺旋填入
 * 从键盘输入一个整数（1~20）
 * 则以该数字为矩阵的大小，把1,2,3…n*n 的数字按照顺时针螺旋的形式填入其中。例如：
 * 输入数字2，则程序输出：
 * 1 2
 * 4 3
 * 输入数字3，则程序输出：
 * 1 2 3
 * 8 9 4
 * 7 6 5
 * 输入数字4， 则程序输出：
 * 1  2   3   4
 * 12  13  14  5
 * 11  16  15  6
 * 10   9  8   7
 */
public class 顺时针螺旋填入 {
    @Test
    public void go_sortFill() {
        print(sortFill(2));
        System.out.println("----------");
        print(sortFill(5));
    }

    private int[][] sortFill(int size) {
        int count = 1;
        int[][] result = new int[size][size];
        int startIndex = 0, endIndex = size, level = 0;
        while (level <= size / 2) {
            for (int i = startIndex; i < endIndex; i++) {//给该轮的上层赋值,行数由变量level决定，依次为第0,1,2,...,size/2行
                result[level][i] = count++;
            }
            for (int i = startIndex + 1; i < endIndex; i++) {//给该轮的右侧赋值，列数由size-1-level决定，依次为第size-1,size-2,size/2列
                result[i][size - 1 - level] = count++;//去右上角
            }
            for (int i = endIndex - 2; i >= startIndex; i--) {//给该轮的下侧赋值，行数由size-1-level决定，依次为第size-1,size-2,size/2行
                result[size - 1 - level][i] = count++;//去左下角
            }
            for (int i = endIndex - 2; i >= startIndex+1; i--) {//给该轮的左侧赋值，列数由level决定，依次为第0，1,2，size/2列
                result[i][level] = count++;//去右上下角、左上角
            }
            startIndex++;
            endIndex--;
            level++;
        }
        return result;
    }

    private void print(int[][] input) {
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input.length; j++) {
                System.out.printf("%4d", input[i][j]);
            }
            System.out.println();
        }
    }
}
