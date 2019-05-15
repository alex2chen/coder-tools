package com.github.art.创建型模式.单例.safe;

/**
 * 静态变量初始化实现线程安全的单例模式
 *
 * static变量 instance 是在类被加载时初始化并仅被初始化一次，这样就可以保证只有一个instance被初始化
 */
public class StaticSingleton {
    private static StaticSingleton instance = new StaticSingleton();

    private StaticSingleton() {
    }

    public static StaticSingleton getInstance() {
        return instance;
    }
}
