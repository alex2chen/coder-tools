package com.github.code.generate.core;

import com.google.common.collect.Maps;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by fei.chen on 2018/6/28.
 */
public class VelocityEngine {
    public static String evaluate(String content, List<Map<String, Object>> params) {
        Map<String, Object> param = Maps.newConcurrentMap();
        param.put("param", params);
        return evaluate(content, param);
    }

    public static String evaluate(String content, Map<String, Object> variables) {
        String result = null;
        VelocityContext velocityContext = new VelocityContext();
        if (variables != null) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key != null && key.trim().length() > 1) {
                    velocityContext.put(key, value);
                }
            }
        }

        StringWriter writer = new StringWriter();
        try {
            Velocity.evaluate(velocityContext, writer, "", content);
            result = writer.toString();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return result;
    }
}
