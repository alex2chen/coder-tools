package com.github.code.generate.util;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

/**
 * Created by fei.chen on 2018/6/28.
 */
public class FileDirUtils {
    public static final String FILE_PROPERTY_FLAG = ".properties";

    public static List<File> getDirFiles(String dir) {
        return getDirFiles(dir, null, false);
    }

    public static List<File> getDirFiles(String dir, ClassLoader loader, boolean isPropertys) {
        if (loader == null) loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(dir);
        List<File> result = Lists.newArrayList();
        if (url != null) {
            String fileType = url.getProtocol();
            if ("file".equalsIgnoreCase(fileType)) {
                File file = new File(url.getPath());
                File[] childFiles = file.listFiles(x -> x.isFile() && (isPropertys ? x.getName().endsWith(FILE_PROPERTY_FLAG) : true));
                for (File childFile : childFiles) {
                    result.add(childFile);
                }
            }
        }
        return result;
    }

    public static List<Properties> getDirProperties(String dir) {
        List<Properties> result = Lists.newArrayList();
        List<File> dirFiles = getDirFiles(dir, null, true);
        dirFiles.forEach(x -> {
            try {
                result.add(PropertiesUtil.loadProperties(x));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        return result;
    }

}
