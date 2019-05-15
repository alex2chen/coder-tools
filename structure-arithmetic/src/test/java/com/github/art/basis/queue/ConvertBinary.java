package com.github.art.basis.queue;

import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 使用队列将1到n转换为二进制
 * Input: n = 2
 * Output: 1, 10
 * <p>
 * Input: n = 5
 * Output: 1, 10, 11, 100, 101
 */
public class ConvertBinary {
    private void generatePrintBinary(int n) {
        Queue<String> q = new LinkedList<String>();
        q.add("1");
        while (n-- > 0) {
            String s1 = q.peek();
            q.remove();
            System.out.println(s1);

            // Store s1 before changing it
            String s2 = s1;
            // Append "0" to s1 and enqueue it
            q.add(s1 + "0");
            // Append "1" to s2 and enqueue it. Note that s2 contains
            // the previous front
            q.add(s2 + "1");
        }
    }

    @Test
    public void go_print() {
        int n = 10;
        generatePrintBinary(n);

    }
}
