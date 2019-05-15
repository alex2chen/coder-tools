package com.github.art.行为型模式.解释器模式;

import java.util.HashMap;
import java.util.Map;

/**
 * InterpreterContext
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/29
 */
public class InterpreterContext {
    private Map<Variable, Boolean> map;

    public InterpreterContext() {
        map = new HashMap<>();
    }

    public InterpreterContext(Map<Variable, Boolean> map) {
        this.map = map;
    }

    public Map<Variable, Boolean> getMap() {
        return map;
    }

    public void setMap(Map<Variable, Boolean> map) {
        this.map = map;
    }

    public void put(Variable var, boolean value) {
        this.map.put(var, value);
    }

    public boolean lookup(Variable var) {
        Boolean result = this.map.get(var);
        if (result == null) {
            throw new IllegalArgumentException("not key such");
        }
        return result;
    }
}
