package com.github.code.generate.domain;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class GeneratorConfig {
    private String codeOutputRootDir;
    private String dbType;
    private String dbDriver;
    private String dbUrl;
    private String dbUer;
    private String dbPass;
    private List<String> sqlExcludeFields;

    public GeneratorConfig() {
    }

    public GeneratorConfig(Properties props) {
        codeOutputRootDir = (props.getProperty("code.output.root.dir"));
        dbType = (props.getProperty("jdbc.db.type"));
        dbDriver = (props.getProperty("jdbc.driver"));
        dbUrl = (props.getProperty("jdbc.url"));
        dbUer = (props.getProperty("jdbc.user"));
        dbPass = (props.getProperty("jdbc.pass"));
        Optional.of(props.getProperty("jdbc.sql.exclude.fields")).ifPresent(x -> sqlExcludeFields = Splitter.on(x).splitToList(","));
    }

    public String getCodeOutputRootDir() {
        return codeOutputRootDir;
    }

    public void setCodeOutputRootDir(String codeOutputRootDir) {
        this.codeOutputRootDir = codeOutputRootDir;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUer() {
        return dbUer;
    }

    public void setDbUer(String dbUer) {
        this.dbUer = dbUer;
    }

    public String getDbPass() {
        return dbPass;
    }

    public void setDbPass(String dbPass) {
        this.dbPass = dbPass;
    }

    public List<String> getSqlExcludeFields() {
        return sqlExcludeFields;
    }

    public void setSqlExcludeFields(List<String> sqlExcludeFields) {
        this.sqlExcludeFields = sqlExcludeFields;
    }
}
