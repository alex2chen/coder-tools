package com.github.common.script;

import org.junit.Test;
import org.mvel2.MVEL;
import org.mvel2.compiler.CompiledExpression;
import org.mvel2.compiler.ExpressionCompiler;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/5/13.
 */
public class Mvel_test {

    @Test
    public void compile() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("x", 10);
        params.put("y", 5 + 1);
        //编译后执行（加快执行）
        ExpressionCompiler compiler = new ExpressionCompiler("x * y");
        CompiledExpression exp = compiler.compile();
        System.out.println(MVEL.executeExpression(exp, params));
    }

    @Test
    public void interprete() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("x", 10);
        params.put("y", 5 + 1);
        System.out.println(MVEL.eval("x*y", params));
    }
}
