package com.github.jvm.util;

import com.google.common.base.Strings;

import java.util.BitSet;

/**
 * @Author: alex
 * @Description: 位图计算
 * @Date: created in 2015/7/18.
 */
public class BitSet_unit {
    public static void main(String[] args) {
        BitSet bitset = new BitSet(100);
        bitset.set(99, true);
        bitset.set(2, 6);
        System.out.println(bitset.get(3));
        System.out.println(bitset.get(7));

        System.out.println(bitset.size());
        System.out.println(bitset.length());
        System.out.println(bitset.cardinality());
        printBitSet(bitset);
        for (int item : bitset.stream().toArray()) {
            System.out.print(item + ",");
        }
        System.out.println();
        BitSet result = stringToBitset("00010001001001111000");
        System.out.println(result.get(3));
        System.out.println(result.get(5));
        System.out.println(result.toString());


    }

    /**
     * @param input
     * @return
     */
    public static BitSet stringToBitset(String input) {
        if (Strings.isNullOrEmpty(input)) {
            throw new NullPointerException("bit is not null");
        }
        BitSet result = new BitSet(input.length());
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '1') {
                result.set(i);
            }
        }
        return result;
    }

    /**
     * 打印
     *
     * @param bs
     */
    public static void printBitSet(BitSet bs) {
        StringBuffer buf = new StringBuffer();
        buf.append("[\n");
        for (int i = 0; i < bs.size(); i++) {
            if (i < bs.size() - 1) {
//                buf.append(getBitTo10(bs.get(i)) + ",");
                buf.append(getBitTo10(bs.get(i)));
            } else {
                buf.append(getBitTo10(bs.get(i)));
            }
//            if ((i + 1) % 8 == 0 && i != 0) {
//                buf.append("\n");
//            }
        }
        buf.append("\n]");
        System.out.println(buf.toString());
    }

    //true,false换成1,0为了好看
    public static String getBitTo10(boolean flag) {
        String a = "";
        if (flag == true) {
            return "1";
        } else {
            return "0";
        }
    }
}
