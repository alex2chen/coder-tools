package com.github.code.generate.domain;

public class Field {
    private String propertyName;
    private String fieldName;
    private String javaFullType;
    private String javaType;
    private String jdbcType;
    private String getterName;
    private String setterName;
    private String comment;

    private int length;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getGetterName() {
        return getterName;
    }

    public void setGetterName(String getterName) {
        this.getterName = getterName;
    }

    public String getSetterName() {
        return setterName;
    }

    public void setSetterName(String setterName) {
        this.setterName = setterName;
    }

    public String getJavaFullType() {
        return javaFullType;
    }

    public void setJavaFullType(String javaFullType) {
        this.javaFullType = javaFullType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
