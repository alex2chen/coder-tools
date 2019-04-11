package com.github.jvm.lang.ref;

import com.google.common.base.FinalizableReferenceQueue;
import com.google.common.base.FinalizableSoftReference;
import com.google.common.base.FinalizableWeakReference;
import org.junit.Test;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/12/21.
 */
public class GuavaReference_test {
    private User user = new User("alex", null);

    //由于直接使用referenceQueue,再加上开启线程去监控queue太过麻烦和复杂，参考由google guava的实现
    @Test
    public void softGc_callback() throws InterruptedException {
        FinalizableReferenceQueue queue = new FinalizableReferenceQueue();
        FinalizableSoftReference weakReference = new FinalizableSoftReference(user, queue) {
            public void finalizeReferent() {
                //这里是被gc之后的回调
                //do your thing
                System.out.println("user 对象被回收!");
            }
        };
        user = null;
        System.out.println(String.format("Get object of WeakReference: %s", weakReference.get()));
        System.gc();
        while (weakReference.get() != null) {
            System.out.println(String.format("Get object of WeakReference: %s", weakReference.get()));
            Thread.sleep(1000);
        }
    }

    @Test
    public void weakGc_callback() {
        FinalizableReferenceQueue queue = new FinalizableReferenceQueue();
        FinalizableWeakReference weakReference = new FinalizableWeakReference(user, queue) {
            public void finalizeReferent() {
                //这里是被gc之后的回调
                //do your thing
                System.out.println("user 对象被回收!");
            }
        };
        user = null;
        System.out.println(String.format("Get object of WeakReference: %s", weakReference.get()));
        System.gc();
        System.out.println(String.format("Get object of WeakReference: %s", weakReference.get()));
    }
}
