package com.github.art.结构性模式.代理模式;

/**
 * Proxy
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/22
 */
public class Proxy implements Subject {
    private Subject subject;

    public Proxy(Subject subject) {
        this.subject = subject;
    }

    @Override
    public void request() {
        beforeRequest();
        subject.request();
        afterRequest();
    }

    private void beforeRequest() {
        System.out.println("处理请求前");
    }

    private void afterRequest() {
        System.out.println("处理请求后");
    }
}
