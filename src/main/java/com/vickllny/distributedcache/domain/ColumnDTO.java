package com.vickllny.distributedcache.domain;

import com.google.common.base.CaseFormat;

public class ColumnDTO {
    /**
     * 数据库字段名称
     */
    protected String columnName;

    /**
     * 属性名称
     */
    protected String fieldName;

    /**
     * 属性类型
     */
    protected Class<?> javaType = String.class;

    public ColumnDTO() {
    }


    public ColumnDTO(final String columnName) {
        this(columnName, CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName));
    }

    public ColumnDTO(final String columnName, final String fieldName) {
        this.columnName = columnName;
        this.fieldName = fieldName;
    }

    public ColumnDTO(final String columnName, final String fieldName, final Class<?> javaType) {
        this.columnName = columnName;
        this.fieldName = fieldName;
        this.javaType = javaType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(final String fieldName) {
        this.fieldName = fieldName;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public void setJavaType(final Class<?> javaType) {
        this.javaType = javaType;
    }
}
