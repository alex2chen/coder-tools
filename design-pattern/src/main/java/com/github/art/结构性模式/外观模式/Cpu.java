package com.github.art.结构性模式.外观模式;

/**
 * Cpu
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/22
 */
public class Cpu {
    protected void startup(){
        System.out.println("Cpu初始化");
    }
    protected void shutdown(){
        System.out.println("Cpu退出");
    }
}
