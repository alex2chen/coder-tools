package com.github.art.行为型模式.观察者模式;

/**
 * Client
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/25
 */
public class Client {
    public static void main(String []args){
        Observer observer1=new ConcreteObserver1();
        Observer observer2=new ConcreteObserver2();

        AbstractSubject subject=new ConcreteSubject("飞碟说");
        subject.addObserver(observer1);
        subject.removeObserver(observer2);

        subject.setState(1);

    }
}
