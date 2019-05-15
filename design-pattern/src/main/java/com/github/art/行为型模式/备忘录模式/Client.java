package com.github.art.行为型模式.备忘录模式;

/**
 * Client
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class Client {
    public static void main(String[] args) {
        MobileOwner mobileOwner = new MobileOwner();
        mobileOwner.addContactPerson(new ContactPerson("小明", "13987778987"));
        mobileOwner.addContactPerson(new ContactPerson("小红", "13798978779"));

        Caretaker caretaker = new Caretaker();
        //备份
        caretaker.saveMemento("201682901", mobileOwner.createMemento());
        //再新增
        mobileOwner.addContactPerson(new ContactPerson("小光", "15202239078"));
        //备份
        caretaker.saveMemento("201682902", mobileOwner.createMemento());
        //恢复
        mobileOwner.restoreMemento(caretaker.getMemento("201682901"));

        caretaker.getContactMementoMap().keySet().forEach(k->{
            System.out.println(k);
            System.out.println(caretaker.getContactMementoMap().get(k).getContactPersonBak());
            System.out.println("-----");
        });

        mobileOwner.getContactPersons().forEach(System.out::println);
    }
}
