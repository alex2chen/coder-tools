package com.github.art.行为型模式.备忘录模式;

/**
 * ContactPerson
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class ContactPerson {
    private String name;
    private String mobileNumber;

    public ContactPerson() {
    }

    public ContactPerson(String name, String mobileNumber) {
        this.name = name;
        this.mobileNumber = mobileNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String toString() {
        return "ContactPerson{" +
                "name='" + name + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }
}
