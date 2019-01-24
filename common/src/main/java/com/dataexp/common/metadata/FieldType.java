package com.dataexp.common.metadata;

import java.util.Objects;

/**
 * 定义字段数据类型
 */
public class FieldType {

    /**
     * 字段基础类型
     */
    private BaseType baseType;

    /**
     * 字段数据类型
     */
    private String dataType;

    /**
     * 字段名称
     */
    private String name;

    public FieldType(BaseType baseType, String dataType, String name) {
        this.baseType = baseType;
        this.dataType = dataType;
        this.name = name;
    }

    public BaseType getBaseType() {
        return baseType;
    }

    public void setBaseType(BaseType baseType) {
        this.baseType = baseType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FieldType fieldType = (FieldType) o;
        return baseType == fieldType.baseType &&
                dataType.equals(fieldType.dataType) &&
                name.equals(fieldType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseType, dataType, name);
    }
}
