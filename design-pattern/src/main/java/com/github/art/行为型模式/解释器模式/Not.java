package com.github.art.行为型模式.解释器模式;

/**
 * Not
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class Not extends Expression {
    private Expression exp;

    public Not() {
    }

    public Not(Expression exp) {
        this.exp = exp;
    }

    public Expression getExp() {
        return exp;
    }

    public void setExp(Expression exp) {
        this.exp = exp;
    }

    @Override
    public boolean interpret(InterpreterContext ctx) {
        return !exp.interpret(ctx);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Not) {
            return exp.equals(((Not) obj).exp);
        }
        return false;
    }

    @Override
    public String toString() {
        return "(Not " + exp.toString() + ")";
    }
}
