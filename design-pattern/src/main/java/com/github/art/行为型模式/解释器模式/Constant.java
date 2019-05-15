package com.github.art.行为型模式.解释器模式;

/**
 * Constant
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class Constant extends Expression {
    private boolean value;

    public Constant() {
    }

    public Constant(boolean value) {
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public boolean interpret(InterpreterContext ctx) {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Constant) {
            return this.value == ((Constant) obj).value;
        }
        return false;
    }

    @Override
    public String toString() {
        return new Boolean(this.value).toString();
    }
}
