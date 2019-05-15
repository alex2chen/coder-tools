package com.github.art.string;

import org.junit.Assert;
import org.junit.Test;

/**
 * 压缩字符串,如果压缩后的字符串长度大于等于原字符串长度,则返回原字符串
 */
public class CompressStr {
    @Test
    public void go_cmpr() {
        Assert.assertEquals("aabcccaaaa", compress("a2b1c3a4"));
        Assert.assertEquals("aaaaaaaaaaaa", compress("a12"));
        Assert.assertEquals("abcdefg", compress("abcdefg"));
        Assert.assertEquals("aabbccdd", "aabbccdd");
    }

    private String compress(String s) {
        StringBuffer sb = new StringBuffer();
        int i, j;
        for (i = 0; i < s.length(); i++) {
            j = i + 1;
            while (j < s.length() && s.charAt(j) == s.charAt(i)) {
                j++;
            }
            sb.append(s.charAt(i));
            sb.append(j - i);
            i = j - 1;
        }
        String sbString = sb.toString();
        return sbString.length() <= s.length() ? sbString.toString() : s;
    }
}
