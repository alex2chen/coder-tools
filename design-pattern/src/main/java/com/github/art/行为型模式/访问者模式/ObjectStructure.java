package com.github.art.行为型模式.访问者模式;

import java.util.ArrayList;
import java.util.List;

/**
 * ObjectStructure
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class ObjectStructure {
    private List<Person> elements;

    public ObjectStructure() {
        elements=new ArrayList<>();
    }

    public ObjectStructure(List<Person> elements) {
        this.elements = elements;
    }

    public List<Person> getElements() {
        return elements;
    }

    public void setElements(List<Person> elements) {
        this.elements = elements;
    }

    /**
     * @param person
     */
    public void attach(Person person) {
        elements.add(person);
    }

    /**
     * @param visitor
     */
    public void display(Visitor visitor) {
        if (!elements.isEmpty()) {
            elements.forEach(e -> {
                e.accept(visitor);
            });
        }
    }
}
