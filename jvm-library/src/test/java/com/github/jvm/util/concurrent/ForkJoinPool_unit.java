package com.github.jvm.util.concurrent;

import org.junit.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * @author alex.chen
 * @Description ForkJoinPool是在java7中增加的
 * @date 2017/4/13
 */
public class ForkJoinPool_unit {

    @Test
    public void mapreduce() throws InterruptedException {
        ForkJoinPool pool = new ForkJoinPool(4);
        PrintTask task = new PrintTask(0, 300);
        pool.submit(task);
        //线程阻塞，等待所有任务完成
        pool.awaitTermination(2, TimeUnit.SECONDS);
        pool.shutdown();
    }

    class PrintTask extends RecursiveAction {
        private static final int THRESHOLD = 50; //最多只能打印50个数
        private int start;
        private int end;

        public PrintTask(int start, int end) {
            super();
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - start < THRESHOLD) {
                for (int i = start; i < end; i++) {
                    System.out.println(Thread.currentThread().getName() + "的i值：" + i);
                }
            } else {
                int middle = (start + end) / 2;
                PrintTask left = new PrintTask(start, middle);
                PrintTask right = new PrintTask(middle, end);
                //并行执行两个“小任务”
                left.fork();
                right.fork();
            }
        }
    }
}
