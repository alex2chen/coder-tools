package com.github.art.创建型模式.单例;

/**
 * Singleton
 * 非线程安全单例模式实现类
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/19
 */
public class Singleton {
    private static Singleton instance;

    private Singleton() {
    }

    public static Singleton GetInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
