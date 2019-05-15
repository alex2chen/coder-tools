package com.github.art.行为型模式.备忘录模式;

import java.util.ArrayList;
import java.util.List;

/**
 * MobileOwner
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class MobileOwner {
    private List<ContactPerson> contactPersons;

    public MobileOwner() {
        contactPersons = new ArrayList<>();
    }

    public MobileOwner(List<ContactPerson> contactPersons) {
        this.contactPersons = contactPersons;
    }

    public List<ContactPerson> getContactPersons() {
        return contactPersons;
    }

    public void setContactPersons(List<ContactPerson> contactPersons) {
        this.contactPersons = contactPersons;
    }

    /**
     * @return
     */
    public ContactMemento createMemento() {
        //深复制
        ContactMemento contactMemento = new ContactMemento();
        List<ContactPerson> contactMementoList = new ArrayList<ContactPerson>();
        contactMementoList.addAll(contactPersons);
        contactMemento.setContactPersonBak(contactMementoList);
        return contactMemento;
    }

    /**
     * @param contactMemento
     */
    public void restoreMemento(ContactMemento contactMemento) {
        if (!contactMemento.getContactPersonBak().isEmpty()) {
            this.contactPersons = contactMemento.getContactPersonBak();
            //contactMemento.getContactPersonBak().forEach(System.out::println);
        }
    }

    /**
     * @param person
     */
    public void addContactPerson(ContactPerson person) {
        if (contactPersons != null)
            contactPersons.add(person);
    }
}
