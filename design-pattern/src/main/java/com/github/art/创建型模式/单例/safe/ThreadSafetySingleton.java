package com.github.art.创建型模式.单例.safe;

/**
 * 线程安全的单例模式实现类
 */
public class ThreadSafetySingleton {
    private static ThreadSafetySingleton instance;

    private ThreadSafetySingleton() {
    }

    public static synchronized ThreadSafetySingleton GetInstance() {
        if (instance == null) {
            instance = new ThreadSafetySingleton();
        }
        return instance;
    }
}
