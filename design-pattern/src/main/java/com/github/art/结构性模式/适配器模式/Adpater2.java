package com.github.art.结构性模式.适配器模式;

/**
 * Adpater2
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/22
 */
public class Adpater2 implements AdptTarget {
    private AdptSource source;
    public AdptSource getSource() {
        return source;
    }

    public void setSource(AdptSource source) {
        this.source = source;
    }

    @Override
    public void request() {
        this.source.complateReq();
        System.out.println("对象适配完成");
    }
}
