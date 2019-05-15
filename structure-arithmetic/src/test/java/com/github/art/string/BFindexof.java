package com.github.art.string;

import org.junit.Test;

/**
 * BF（Brute Force)算法也就是传说中的“笨办法”，是一个暴力/蛮力算法。设串S和P的长度分别为m,n，则它在最坏情况下的时间复杂度是O(m*n)。BF算法的最坏时间复杂度虽然不好，但它易于理解和编程，在实际应用中，一般还能达到近似于O(m+n)的时间度（最坏情况不是那么容易出现的，RP问题），因此，还在被大量使用。
 * http://www.cnblogs.com/zzqcn/p/3508442.html
 */
public class BFindexof {
    @Test
    public void go_search() {
        String source = "abchhabchabchabchcaaaabceabddh";
        String target = "abceab";
        //System.out.println(source.indexOf(target));
        System.out.println("匹配成功，下标为：" + indexOf(source, target));
    }

    private int indexOf(String source, String target) {
        // TODO Auto-generated method stub
        return indexOf(source.toCharArray(), 0, source.toCharArray().length,
                target.toCharArray(), 0, target.toCharArray().length, 0);
    }

    private static int indexOf(char[] source, int sourceOffset, int sourceCount,
                               char[] target, int targetOffset, int targetCount, int fromIndex) {
        int count = 0;
        if (fromIndex >= sourceCount) return (targetCount == 0 ? sourceCount : -1);

        if (fromIndex < 0) fromIndex = 0;

        if (targetCount == 0) return fromIndex;

        char first = target[targetOffset];
        int max = sourceOffset + (sourceCount - targetCount);

        for (int i = sourceOffset + fromIndex; i <= max; i++) {
            /* Look for first character. */
            count++;
            if (source[i] != first) {

                while (++i <= max && source[i] != first) {
                    count++;
                }
                ;
            }
            /* Found first character, now look at the rest of v2 */
            if (i <= max) {
                int j = i + 1;
                int end = j + targetCount - 1;
                //为方便统计次数，对String源码稍作调整
                for (int k = targetOffset + 1; j < end; ) {
                    count++;
                    if (source[j] == target[k]) {
                        j++;
                        k++;
                    } else {
                        break;
                    }
                }
                ;
                //=================

                if (j == end) {
                    System.out.println("暴力匹配法比较次数为：" + count);
                    /* Found whole string. */
                    return i - sourceOffset;
                }
            }
        }
        System.out.println("暴力匹配法比较次数为：" + count);
        return -1;
    }
}
