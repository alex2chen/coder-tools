package com.github.art.bit;

import com.google.common.base.Stopwatch;
import org.junit.Test;

import java.util.BitSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 位图
 */
public class Bitmap {
    @Test
    public void go_set() {
        int[] array = new int[80000];
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(100000);
            //System.out.print(array[i] + " ");
        }
        //取出最大值，找出不连续的数字
        //simple_sort(array);
        useBitmap(array);
    }

    /**
     * 一般的做法(耗时)
     * 复杂度：T（n）=O(n*n)
     *
     * @param array
     */
    private static void simple_sort(int[] array) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        int max = array[0];
        for (int item : array) {
            if (item > max) {
                max = item;
            }
        }
        System.out.println("最大值为：" + max);
        System.out.println("不连续的数字：");
        int flag;
        for (int i = 1; i <= max; i++) {
            flag = 1;
            for (int j = 0; j < array.length; j++) {
                if (i == array[j]) {
                    flag = 0;
                    break;
                }
            }
            if (flag == 1) {
                System.out.print(i + " ");
            }
        }
        stopwatch.stop();
        System.out.println("");
        System.out.println("long time:" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    /**
     * 使用位图思想
     *
     * @param array
     */
    private static void useBitmap(int[] array) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        int max = array[0];
        for (int item : array) {
            if (item > max) {
                max = item;
            }
        }
        System.out.println("最大值为：" + max);
        System.out.println("不连续的数字：");
        BitSet bitSet = new BitSet(max + 1);
        for (int item : array) {
            bitSet.set(item);
        }
        //bitSet.stream().filter(value -> bitSet.get(value)).forEach(System.out::println);
        System.out.println("");
        System.out.println("long time:" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

}
