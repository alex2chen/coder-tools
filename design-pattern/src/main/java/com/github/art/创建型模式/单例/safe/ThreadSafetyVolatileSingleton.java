package com.github.art.创建型模式.单例.safe;

/**
 * 高效的线程安全的单例模式实现类
 */
public class ThreadSafetyVolatileSingleton {
    private static volatile ThreadSafetyVolatileSingleton instance;

    private ThreadSafetyVolatileSingleton() {
    }

    public static ThreadSafetyVolatileSingleton getInstance() {
        if (instance == null) {
            synchronized (ThreadSafetyVolatileSingleton.class) {
                if (instance == null) instance = new ThreadSafetyVolatileSingleton();
            }
        }
        return instance;
    }

}
