package com.github.art.other;

import org.junit.Test;

/**
 * 规则替换：
 * rule:
 * C -> CD
 * AB -> B
 * sample: ABC -> BCD, CC -> CDCD
 * String 的实现可以有o（n）的空间，char[] 不允许有额外的空间。
 */
public class 规则替换 {
    @Test
    public void go_ruleRepace() {
        System.out.println(ruleRepace(new char[]{'A', 'B', 'C'}));
        System.out.println(ruleRepace(new char[]{'C', 'C'}));
    }
    private String ruleRepace(char[] input) {
        StringBuilder result = new StringBuilder();
        int size = input.length;
        for (int i = 0; i < size; i++) {
            if (input[i] == 'C') {
                result.append("CD");
            } else if (i < size - 1 && input[i] == 'A' && input[i + 1] == 'B') {
                result.append("B");
                i++;
            } else {
                result.append(input[i]);
            }
        }
        return result.toString();
    }

}
