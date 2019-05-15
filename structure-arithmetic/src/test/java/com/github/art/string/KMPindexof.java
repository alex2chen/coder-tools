package com.github.art.string;

import org.junit.Test;

/**
 * 它是由D.E.Knuth、J.H.Morris和V.R.Pratt同时发现的。KMP算法可以在O(m+n)的时间里完成串的模式匹配。它的主要思想是：每当一趟匹配过程中出现字符不匹配时，不需回退i指针，而是利用已经得到的“部分匹配”的结果将模式向右“滑动”尽可能远的一段距离后，继续匹配过程。
 * https://blog.csdn.net/jevonscsdn/article/details/60874054
 */
public class KMPindexof {
    @Test
    public void go_search() {
        String source = "abchhabchabchabchcaaaabceabddh";
        String target = "abceab";
        System.out.println("匹配成功，下标为：" + kmpSearch(source, target));
    }

    private int kmpSearch(String source, String target) {
        int count = 0;
        // 转为字符型数组
        char[] s = source.toCharArray();
        char[] t = target.toCharArray();
        // 获取next数组
        int[] next = next(target);
        int i = 0;// 主串下标
        int j = 0;// 模式串下标
        while (i < s.length && j < t.length) {
            //若j!=-1,则必然会发生字符比较
            if (j != -1) {
                count++;
            }
            if (j == -1 || s[i] == t[j]) {
                i++;
                j++;
            } else {
                j = next[j];
            }

        }
        System.out.println("KMP匹配法比较次数为：" + count);
        if (j == t.length) {
            return i - t.length;
        }
        return -1;
    }

    //next数组优化版
    private int[] next(String target) {
        char[] t = target.toCharArray();
        int[] next = new int[t.length];
        next[0] = -1;
        int k = -1;
        int j = 0;
        while (j < next.length - 1) {
            if (k == -1 || t[j] == t[k]) {
                k++;
                j++;
                // ===============
                // 较优化前的next数组求法，改变在以下四行代码。
                if (t[j] != t[k]) {
                    next[j] = k;// 优化前只有这一行。
                } else {
                    // 优化后因为不能出现p[j] = p[ next[j ]]，所以当出现时需要继续递归。
                    next[j] = next[k];
                }
                // ===============
            } else {
                k = next[k];
            }
        }
        return next;
    }
}
