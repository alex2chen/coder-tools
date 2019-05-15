package com.github.art.结构性模式.装饰模式;

/**
 * Accessories
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/22
 */
public class Accessories extends Decorator {
    public Accessories(Phone phone) {
        super(phone);
    }

    @Override
    public void print() {
        super.print();
        addAccessories();
    }
    private void addAccessories(){
        System.out.println("现在苹果手机有漂亮的挂件了");
    }
}
