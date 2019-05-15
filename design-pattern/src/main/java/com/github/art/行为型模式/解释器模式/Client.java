package com.github.art.行为型模式.解释器模式;

/**
 * Client
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class Client {
    public static void main(String[] args) {
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Constant constant = new Constant(true);

        InterpreterContext context = new InterpreterContext();
        context.put(x, false);
        context.put(y, true);
        System.out.println("x=" + x.interpret(context));
        System.out.println("y=" + y.interpret(context));
        Expression exp = new Or(new And(constant, x), new And(y, new Not(x)));
        System.out.println(exp.toString() + "=" + exp.interpret(context));

    }
}
