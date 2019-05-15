package com.github.art.创建型模式.抽象工厂;

/**
 * NanjiFactory
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/22
 */
public class NanjiFactory extends AbstractFactory {
    @Override
    public Yabo createYabo() {
        return new NanjiYabo();
    }

    @Override
    public Yazhou createYazhou() {
        return new NanjiYazhou();
    }
}
