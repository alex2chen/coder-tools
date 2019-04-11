package com.github.junit;

/**
 * Created by Administrator on 2015/7/3.
 */
public class Num {
    /**
     * @param x
     * @param y
     * @return
     */
    public int add(int x, int y) {
        return x + y;
    }

    /**
     * @param x
     * @param y
     * @return
     */
    public int divide(int x, int y) {
        return x / y;
    }

    /**
     * @param x
     * @param y
     * @return
     * @throws InterruptedException
     */
    public int plus(int x, int y) throws InterruptedException {
        Thread.sleep(500L);
        return x * y;
    }
}
