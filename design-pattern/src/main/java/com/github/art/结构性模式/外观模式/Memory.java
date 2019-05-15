package com.github.art.结构性模式.外观模式;

/**
 * Memory
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/22
 */
public class Memory {
    protected void startup(){
        System.out.println("内存初始化");
    }
    protected void shutdown(){
        System.out.println("内存退出");
    }
}
