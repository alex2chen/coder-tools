package com.github.jvm.lang.ref;

import com.google.common.base.FinalizableReferenceQueue;
import com.google.common.base.FinalizableWeakReference;
import org.junit.Test;

import java.lang.ref.WeakReference;

/**
 * @Author: alex
 * @Description: 弱引用什么情况下回收？设置运行参数：-verbose:gc
 * <p>
 * 弱引用在GC(包括MinitorGC和Full GC)时，被扫描到就会被回收，但是有一个前提，该弱引用没有外部引用（强引用）
 * @Date: created in 2017/12/21.
 * @see SoftReference_test
 */
public class WeakReference_test {
    private User user = new User("alex", null);
    @Test
    public void weakGc() {
        WeakReference<User> softUser = new WeakReference<User>(user);
        user = null;//此次很重要
        int i = 0;
        while (softUser.get() != null) {
            System.out.println(String.format("Get object of WeakReference: %s, count: %d", softUser.get(), ++i));
            if (i % 10 == 0) {
                System.out.println("gc start!");
                System.gc();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }
        }
        System.out.println("Get object is null!");
    }
}
