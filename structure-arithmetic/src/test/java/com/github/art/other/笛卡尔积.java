package com.github.art.other;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 笛卡尔积
 * 又称直积，表示为X × Y
 * Created by YT on 2018/3/27.
 */
public class 笛卡尔积 {

    @Test
    public void go_calc() {
        List<String> colorList = Lists.newArrayList("red", "blue");
        List<String> heighList = Lists.newArrayList("170", "175");
        List<String> sizeList = Lists.newArrayList("88A", "92A");
        List<List<String>> list = Lists.newArrayList(colorList, heighList, sizeList);

        List<List<String>> result = Lists.newArrayList();
//        descartes(list, result, 0, new ArrayList<String>());
        descartes(list, result, 0, Lists.newArrayList("X"));
        System.out.println(result);

    }

    /**
     * @param dimvalue 原List
     * @param result   通过乘积转化后的数组
     * @param layer    中间参数(乘机index)
     * @param curList  中间参数
     */
    private static void descartes(List<List<String>> dimvalue,
                                  List<List<String>> result, int layer, List<String> curList) {
        if (layer < dimvalue.size() - 1) {
            if (dimvalue.get(layer).size() == 0) {
                descartes(dimvalue, result, layer + 1, curList);
            } else {
                for (int i = 0; i < dimvalue.get(layer).size(); i++) {
                    List<String> list = new ArrayList<String>(curList);
                    list.add(dimvalue.get(layer).get(i));
                    descartes(dimvalue, result, layer + 1, list);
                }
            }
        } else if (layer == dimvalue.size() - 1) {
            if (dimvalue.get(layer).size() == 0) {
                result.add(curList);
            } else {
                for (int i = 0; i < dimvalue.get(layer).size(); i++) {
                    List<String> list = new ArrayList<String>(curList);
                    list.add(dimvalue.get(layer).get(i));
                    result.add(list);
                }
            }
        }
    }

}
