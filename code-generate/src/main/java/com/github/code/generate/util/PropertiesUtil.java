package com.github.code.generate.util;

import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

/**
 * Created by fei.chen on 2018/6/27.
 */
public class PropertiesUtil {
    /**
     * @param file ex：config/config.properties
     * @return
     */
    public static Map<String, String> loadProperties(String file) {
        Map<String, String> result = Maps.newHashMap();
        Properties properties = loadProperties(file, false);
        Enumeration<?> names = properties.propertyNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            result.put(name, properties.getProperty(name));
        }
        return result;
    }

    public static Properties loadProperties(File file) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(file);
        return loadProperties(inputStream);
    }

    public static Properties loadProperties(String file, boolean hasSkip) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
        return loadProperties(inputStream);
    }

    private static Properties loadProperties(InputStream fileStream) {
        Properties properties = new Properties();
        try {
            properties.load(fileStream);
            IOUtils.closeQuietly(fileStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
