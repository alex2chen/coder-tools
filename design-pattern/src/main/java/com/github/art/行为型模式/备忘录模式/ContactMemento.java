package com.github.art.行为型模式.备忘录模式;

import java.util.List;

/**
 * ContactMemento
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class ContactMemento {
    private List<ContactPerson> contactPersonBak;

    public ContactMemento() {
    }

    public ContactMemento(List<ContactPerson> contactPersonBak) {
        this.contactPersonBak = contactPersonBak;
    }

    public List<ContactPerson> getContactPersonBak() {
        return contactPersonBak;
    }

    public void setContactPersonBak(List<ContactPerson> contactPersonBak) {
        this.contactPersonBak = contactPersonBak;
    }
}
