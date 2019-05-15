package com.github.art.行为型模式.备忘录模式;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Caretaker
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class Caretaker {
    private Map<String, ContactMemento> contactMementoMap;

    public Caretaker() {
        contactMementoMap = new HashMap<>();
    }

    public Map<String, ContactMemento> getContactMementoMap() {
        return contactMementoMap;
    }

    public void setContactMementoMap(Map<String, ContactMemento> contactMementoMap) {
        this.contactMementoMap = contactMementoMap;
    }

    /**
     * @param key
     * @return
     */
    public ContactMemento getMemento(String key) {
        return contactMementoMap.get(key);
    }

    /**
     * @param key
     * @param contactMemento
     */
    public void saveMemento(String key, ContactMemento contactMemento) {
        this.contactMementoMap.put(key, contactMemento);
    }

    public List<String> showHistory() {
        return this.contactMementoMap.keySet().stream().collect(Collectors.toList());
    }
}
