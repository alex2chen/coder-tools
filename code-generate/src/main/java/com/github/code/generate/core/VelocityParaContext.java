package com.github.code.generate.core;

;
import com.github.code.generate.util.PropertiesUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.github.code.generate.dao.DBManager;
import com.github.code.generate.domain.Field;
import com.github.code.generate.domain.GeneratorConfig;
import com.github.code.generate.util.JavaBeansUtil;

import java.sql.*;
import java.util.*;


public class VelocityParaContext {
    private GeneratorConfig generatorConfig;
    private Connection connection;
    private Map<String, String> classNameMap;//key是表名,value表示类名
    private Map<String, String> instanceNameMap;//key是表名,value表示类的变量名
    private Map<String, String> fieldNameMap;//key是表的字段名,value表示类的属性（用驼峰表示）
    //private Map<String, Field> fieldsMap = new HashMap<String, Field>();//key是表字段名字

    public VelocityParaContext(GeneratorConfig generatorConfig, Connection connection, Map<String, String> classNameMap, Map<String, String> instanceNameMap, Map<String, String> fieldNameMap) {
        this.generatorConfig = generatorConfig;
        this.connection = connection;
        this.classNameMap = classNameMap;
        this.instanceNameMap = instanceNameMap;
        this.fieldNameMap = fieldNameMap;
    }

    public static VelocityParaContextBuilder builder() {
        return new VelocityParaContextBuilder();
    }

    public List<Map<String, Object>> generateAllTableVelocity(List<String> alltables, Connection conn) throws Exception {
        List<Map<String, Object>> velocityParameterMapList = Lists.newArrayList();
        System.out.println("即将生成数据库表名如下:");
        for (int j = 0; j < alltables.size(); j++) {
            String tablename = alltables.get(j);
            Map<String, Object> velocityParameterMap = fillVelocityParameter(conn, tablename);
            velocityParameterMapList.add(velocityParameterMap);
            System.out.println(tablename);
        }
        return velocityParameterMapList;
    }

    private Map<String, Object> fillVelocityParameter(Connection conn, String tableName) throws SQLException {
        Map<String, Object> commonContext = Maps.newHashMap();
        String className = JavaBeansUtil.getCamelCaseString(tableName, true);//classNameMap.get(tableName) == null ? tableName : classNameMap.get(tableName)
        String beanName = JavaBeansUtil.getCamelCaseString(tableName, false);//instNameMap.get(tableName) == null ? tableName : beanNameMap.get(tableName)
        Set<String> imports = new HashSet<String>();
        List<Field> fields = new ArrayList<Field>();

        commonContext.put(ConstantVelocity.CLASS_NAME, className);
        commonContext.put(ConstantVelocity.TABLE_NAME, tableName);
        commonContext.put(ConstantVelocity.INST_NAME, beanName);
        commonContext.put("arr0", "[0]");
        commonContext.put("parameterMap", "parameterMap");
        commonContext.put("excludeFieldsMap", generatorConfig.getSqlExcludeFields());
        commonContext.put("importBean", "com.github.demo.domain." + className);
        commonContext.put("importService", "com.github.demo.service." + className + "Service");
        commonContext.put("importMapper", "com.github.demo.dao." + className + "Mapper");
        // commonContext.put(PK_ID, pkId);
        String pkField = DBManager.getMYSQLPkField(conn, tableName);
        List<String> commentList = DBManager.listMYSQLComments(conn, tableName);
        commonContext.put(ConstantVelocity.PK_ID, pkField);
        commonContext.put(ConstantVelocity.PK_NAME, JavaBeansUtil.getCamelCaseString(pkField, false));
        String pkType = null;
        String pkJavaFullType = null;
        ResultSetMetaData rsmd = DBManager.getFields(conn, tableName);
        // 表的字段数量
        int columnCounts = rsmd.getColumnCount();
        for (int i = 1; i <= columnCounts; i++) {
            String name = rsmd.getColumnName(i);
            String columnClassName = rsmd.getColumnClassName(i);
            String fdname = name;
            String javaType = columnClassName.substring(columnClassName.lastIndexOf(".") + 1);
            boolean isPk = (pkField != null && pkField.equalsIgnoreCase(fdname)) || (pkField == null && i == 1);
            if (isPk) {
                pkType = javaType;
                pkJavaFullType = columnClassName;
            }
            Field field = new Field();
            field.setFieldName(name);
            String propertyName = JavaBeansUtil.getCamelCaseString(name, false);//fieldNameMap.get(fieldName) == null ? fieldName : name;
            field.setPropertyName(propertyName);
            field.setSetterName(JavaBeansUtil.getSetterMethodName(propertyName));
            field.setGetterName(JavaBeansUtil.getGetterMethodName(propertyName));
            field.setJavaType(javaType);
            field.setJdbcType(rsmd.getColumnTypeName(i));
            field.setJavaFullType(rsmd.getColumnClassName(i));
            field.setComment(commentList.get(i - 1));
            fields.add(field);
            if (field.getJavaFullType().indexOf("java.lang") == -1) {
                imports.add(field.getJavaFullType());
            }
        }
        commonContext.put(ConstantVelocity.FIELDS, fields);
        commonContext.put(ConstantVelocity.PK_TYPE, pkType);
        commonContext.put(ConstantVelocity.IMPORTS, imports);
        commonContext.put(ConstantVelocity.AUTHOR, "alex@kxtx.com");
        commonContext.put("result", "result");
        commonContext.put(ConstantVelocity.pk_JavaFullType, pkJavaFullType);
        return commonContext;
    }

    public void fillVelocityParameter(Map<String, Object> commonContext, String packagePath) {
        commonContext.put("package", packagePath);
    }

    public static class VelocityParaContextBuilder {
        private GeneratorConfig generatorConfig;
        private Connection connection;

        public VelocityParaContextBuilder generatorConfig(GeneratorConfig generatorConfig) {
            this.generatorConfig = generatorConfig;
            return this;
        }

        public VelocityParaContextBuilder connection(Connection connection) {
            this.connection = connection;
            return this;
        }

        public VelocityParaContext build() {
            Map<String, String> classNameMap = PropertiesUtil.loadProperties("DB2ClassMapping/classNameMapping.properties");
            Map<String, String> instanceNameMap = PropertiesUtil.loadProperties("DB2ClassMapping/classVarNameMapping.properties");
            Map<String, String> fieldNameMap = PropertiesUtil.loadProperties("DB2ClassMapping/ClassFieldNameMapping.properties");
            return new VelocityParaContext(generatorConfig, connection, classNameMap, instanceNameMap, fieldNameMap);
        }
    }
}
