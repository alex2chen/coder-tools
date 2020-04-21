package com.github.code.generate.domain;

import java.util.Properties;

public class FileConfig {
    private String packagePath;
    private String velocityName;
    private String fileExtendName;
    private String fileType;
    private String srcTargetChildPath;

    public FileConfig() {
    }

    public FileConfig(Properties props) {
        if (props != null) {
            packagePath = (props.getProperty("packagePath"));
            velocityName = (props.getProperty("velocityName"));
            fileExtendName = (props.getProperty("fileExtendName"));
            fileType = (props.getProperty("fileType"));
            srcTargetChildPath = (props.getProperty("srcTargetChildPath"));
        }
    }

    public String getSrcTargetChildPath() {
        return srcTargetChildPath;
    }

    public void setSrcTargetChildPath(String srcTargetChildPath) {
        this.srcTargetChildPath = srcTargetChildPath;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public String getVelocityName() {
        return velocityName;
    }

    public void setVelocityName(String velocityName) {
        this.velocityName = velocityName;
    }

    public String getFileExtendName() {
        return fileExtendName;
    }

    public void setFileExtendName(String fileExtendName) {
        this.fileExtendName = fileExtendName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

}
