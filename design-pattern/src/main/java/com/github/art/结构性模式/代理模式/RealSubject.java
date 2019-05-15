package com.github.art.结构性模式.代理模式;

/**
 * RealSubject
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/22
 */
public class RealSubject implements Subject {
    @Override
    public void request() {
        System.out.println("处理请求");
    }
}
