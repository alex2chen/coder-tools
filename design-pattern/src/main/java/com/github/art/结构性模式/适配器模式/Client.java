package com.github.art.结构性模式.适配器模式;

/**
 * Client
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/31
 */
public class Client {
    public static void main(String[] args) {
        AdptTarget adptTarget = new Adpater();
        adptTarget.request();
    }
}
