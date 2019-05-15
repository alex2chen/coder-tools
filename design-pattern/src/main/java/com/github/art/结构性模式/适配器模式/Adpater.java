package com.github.art.结构性模式.适配器模式;

/**
 * Adpater
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/22
 */
public class Adpater extends AdptSource implements AdptTarget {
    @Override
    public void request() {
        this.complateReq();
        System.out.println("类适配完成");
    }
}
