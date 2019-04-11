package com.github.jvm.lang.ref;

import com.google.common.base.FinalizableReferenceQueue;
import com.google.common.base.FinalizableSoftReference;
import org.junit.Test;

import java.lang.ref.SoftReference;


/**
 * @Author: alex
 * @Description: 软引用  设置：-Xmx50m -Xms50m -Xmn40m -verbose:gc
 * <p>
 * 假设一个对象仅仅具有软引用，则内存空间足够，垃圾回收器就不会回收它；
 * 假设内存空间不足了，就会回收这些对象的内存。仅仅要垃圾回收器没有回收它，该对象就能够被程序使用。软引用可用来实现内存敏感的快速缓存
 * <p>
 * ReferenceQueue及Reference引用链
 * 这里的queue名义上是一个队列,但实际内部并非有实际的存储结构,它的存储是依赖于内部节点之间的关系来表达.可以理解为queue是一个类似于链表的结构,
 * 这里的节点其实就是reference本身。 可以理解为queue为一个链表的容器,其自己仅存储当前的head节点,而后面的节点由每个reference节点自己通过next来保持即可.
 * <p>
 * 每个引用对象都有相应的状态描述,即描述自己以及包装的对象当前处于一个什么样的状态,以方便进行查询,定位或处理。
 * Active:活动状态,即相应的对象为强引用状态,还没有被回收,这个状态下对象不会放到queue当中.在这个状态下next为null,queue为定义时所引用的queue.
 * Pending:准备放入queue当中,在这个状态下要处理的对象将挨个地排队放到queue当中.在这个时间窗口期,相应的对象为pending状态.不管什么reference,进入到此状态的,即可认为相应的此状态下,next为自己(由jvm设置),queue为定义时所引用的queue.
 * Enqueued:相应的对象已经为待回收,并且相应的引用对象已经放到queue当中了.准备由外部线程来询循queue获取相应的数据.此状态下,next为下一个要处理的对象,queue为特殊标识对象ENQUEUED.
 * Inactive:即此对象已经由外部从queue中获取到,并且已经处理掉了.即意味着此引用对象可以被回收,并且对内部封装的对象也可以被回收掉了(实际的回收运行取决于clear动作是否被调用).可以理解为进入到此状态的肯定是应该被回收掉的.
 * jvm并不需要定义状态值来判断相应引用的状态处于哪个状态,只需要通过计算next和queue即可进行判断.
 * @Date: created in 2017/12/21.
 * @see StrongReference
 * 强引用是使用最普遍的引用。假设一个对象具有强引用，那垃圾回收器绝不会回收它。
 * 当内存空间不足，Java虚拟机宁愿抛出OutOfMemoryError错误，使程序异常终止，也不会靠任意回收具有强引用的对象来解决内存不足的问题
 */
public class SoftReference_test {
    private User user = new User("alex", new byte[1024 * 1024 * 28]);//28mb

    //几乎回收不来
    @Test
    public void softGc() {
        SoftReference<User> softUser = new SoftReference<User>(user);
        user = null;
        int i = 0;
        while (softUser.get() != null) {
            System.out.println(String.format("Get str from object of SoftReference: %s, count: %d", softUser.get(), ++i));
            if (i % 10 == 0) {
                System.out.println("gc start!");
                System.gc();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }
        }
        System.out.println("object is null!");
    }
}
