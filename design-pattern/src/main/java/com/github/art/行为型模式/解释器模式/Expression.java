package com.github.art.行为型模式.解释器模式;

/**
 * Expression
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public abstract class Expression {
    public abstract boolean interpret(InterpreterContext ctx);

    public abstract boolean equals(Object obj);

    public abstract String toString();
}
