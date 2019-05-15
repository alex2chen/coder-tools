package com.github.art.行为型模式.解释器模式;

/**
 * Variable
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class Variable extends Expression {
    private String name;

    public Variable() {
    }

    public Variable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean interpret(InterpreterContext ctx) {
        return ctx.lookup(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Variable) {
            return this.name.equals(((Variable) obj).name);
        }
        return false;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
