package com.github.common.bean.vo;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/4/27
 */
public class FromBean implements Serializable, Cloneable {
    private String name;
    private int age;
    private String address;
    private String idno;
    private double money;
    private List<String> alias;

    public FromBean() {
        alias = Lists.newArrayList();
    }

    public FromBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public List<String> getAlias() {
        return alias;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
    }

    public int getDirectAge(long id) {
        return age;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        FromBean o = null;
        try {
            o = (FromBean) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    @Override
    public String toString() {
        return "FromBean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", idno='" + idno + '\'' +
                ", money=" + money +
                ", alias=" + alias +
                '}';
    }
}