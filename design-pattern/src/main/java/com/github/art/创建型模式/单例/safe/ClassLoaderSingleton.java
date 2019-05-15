package com.github.art.创建型模式.单例.safe;

/**
 * 静态内部类实现的线程安全的单例模式
 *
 * 由于内部静态类只会被加载一次，故是线程安全的
 */
public class ClassLoaderSingleton {
    private ClassLoaderSingleton() {
    }

    private static class SingleHolder {
        static ClassLoaderSingleton instance = new ClassLoaderSingleton();
    }

    public static ClassLoaderSingleton getInstance() {
        return SingleHolder.instance;
    }
}
