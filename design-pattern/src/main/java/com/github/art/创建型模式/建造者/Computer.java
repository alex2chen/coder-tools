package com.github.art.创建型模式.建造者;

import java.util.ArrayList;
import java.util.List;

/**
 * Computer
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/19
 */
public class Computer {
    private List<String> parts;

    public Computer() {
        parts = new ArrayList<>();
    }

    public void add(String part) {
        parts.add(part);
    }

    public void showParts() {
        System.out.println("显示组装详情：");
        if (!parts.isEmpty()) {
            parts.forEach(System.out::println);
        }
    }
}
