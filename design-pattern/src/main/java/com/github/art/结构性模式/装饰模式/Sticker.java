package com.github.art.结构性模式.装饰模式;

/**
 * Sticker
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/22
 */
public class Sticker extends Decorator {
    public Sticker(Phone phone) {
        super(phone);
    }
    @Override
    public void print() {
        super.print();
        addSticker();
    }
    private void addSticker(){
        System.out.println("现在苹果手机有贴膜了");
    }
}
