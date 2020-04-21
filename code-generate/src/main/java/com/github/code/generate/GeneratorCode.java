package com.github.code.generate;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.github.code.generate.core.VelocityEngine;
import com.github.code.generate.core.VelocityParaContext;
import com.github.code.generate.dao.DBManager;
import com.github.code.generate.util.FileDirUtils;
import com.github.code.generate.util.PropertiesUtil;
import com.google.common.collect.Lists;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.JavaFormatterOptions;
import com.github.code.generate.domain.FileConfig;
import com.github.code.generate.domain.GeneratorConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class GeneratorCode {
    private static List<FileConfig> table2FileParaConfigList = new ArrayList<FileConfig>();
    private static List<FileConfig> singleFileParaConfigList = new ArrayList<FileConfig>();
    private static Map<String, String> configMap = new HashMap<String, String>();
    private static GeneratorConfig globalConfig;
    private static ClassLoader classLoader;

    public static void main(String[] args) {
        try {
            init();
            generate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void init() throws Exception {
        globalConfig = new GeneratorConfig(PropertiesUtil.loadProperties("config/config.properties", false));
        table2FileParaConfigList = initFileConfig(FileDirUtils.getDirProperties("Table2FileParaConfig"));
        singleFileParaConfigList = initFileConfig(FileDirUtils.getDirProperties("SingleFileParaConfig"));
        classLoader = Thread.currentThread().getContextClassLoader();
    }

    private static List<FileConfig> initFileConfig(List<Properties> properties) {
        List<FileConfig> result = Lists.newArrayList();
        if (properties == null || properties.isEmpty()) throw new RuntimeException("未找到要生成的配置文件.");
        properties.forEach(x -> result.add(new FileConfig(x)));
        return result;
    }

    public static void generate() throws Exception {
        Connection conn = DBManager.getSQLConnection(globalConfig);
        List<String> alltables = null;
        //alltables = getMYSQLAllTables(conn);
        alltables = Lists.newArrayList("base_product_info_sap");
        VelocityParaContext velocityParaContext = VelocityParaContext.builder().connection(conn).generatorConfig(globalConfig).build();
        List<Map<String, Object>> velocityParameterMapList = velocityParaContext.generateAllTableVelocity(alltables, conn);
        conn.close();
        System.out.println("生成java文件：");
        Formatter formatter = new Formatter(JavaFormatterOptions.builder().style(JavaFormatterOptions.Style.AOSP).build());
        for (Map<String, Object> velocityParameterMap : velocityParameterMapList) {
            for (FileConfig fileConfig : table2FileParaConfigList) {
                String velocityContent = IOUtils.resourceToString(fileConfig.getVelocityName(), Charset.defaultCharset(), classLoader);
                velocityParaContext.fillVelocityParameter(velocityParameterMap, fileConfig.getPackagePath());
                String genContent = VelocityEngine.evaluate(velocityContent, velocityParameterMap);
                String childPath = fileConfig.getSrcTargetChildPath();
                if (fileConfig.getFileType().equals("java")) {
                    childPath = childPath + File.separator + fileConfig.getPackagePath().replace(".", "/");
                    String formatSource = formatter.formatSource(genContent);
                    genContent = formatSource;
                }
                createFile(childPath, (String) velocityParameterMap.get("className") + fileConfig.getFileExtendName(), fileConfig.getFileType(), genContent);
            }
        }
        System.out.println("生成js文件：");
        for (FileConfig fileConfig : singleFileParaConfigList) {
            String childPath = fileConfig.getSrcTargetChildPath();
            if (fileConfig.getFileType().equals("java")) {
                childPath = childPath + fileConfig.getPackagePath().replace(".", "/");
            }
            String velocityContent = IOUtils.resourceToString(fileConfig.getVelocityName(), Charset.defaultCharset(), classLoader);
            List<Map<String, Object>> params = Lists.newArrayList(velocityParameterMapList);
            String genContent = VelocityEngine.evaluate(velocityContent, params);
            createFile(childPath, fileConfig.getFileExtendName(), fileConfig.getFileType(), genContent);
        }
    }


    public static void createFile(String childPath, String fileName, String fileType, String content) throws IOException {
        File f = new File(globalConfig.getCodeOutputRootDir() + childPath + File.separator + fileName + "." + fileType);
        System.out.println(f);
        FileUtils.write(f, content, "UTF-8");
    }
}
