package com.github.art.行为型模式.解释器模式;

/**
 * Or
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class Or extends Expression {
    private Expression left, right;

    public Or() {
    }

    public Or(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public void setLeft(Expression left) {
        this.left = left;
    }

    public Expression getRight() {
        return right;
    }

    public void setRight(Expression right) {
        this.right = right;
    }

    @Override
    public boolean interpret(InterpreterContext ctx) {
        return left.interpret(ctx) || right.interpret(ctx);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Or) {
            return left.equals(((Or) obj).left) && right.equals(((Or) obj).right);
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " OR " + right.toString() + ")";
    }
}
