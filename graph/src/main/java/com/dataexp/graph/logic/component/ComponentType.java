package com.dataexp.graph.logic.component;


import com.dataexp.graph.logic.BaseLogicNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author: Bing.Li`
 * @create: 2019-01-30
 */
public enum ComponentType {
    /**
     * 文件输出组件
     */
    FILE_SINK("FileSink", "com.dataexp.graph.logic.component.FileSink"),
    FILE_SOURCE("FileSource","com.dataexp.graph.logic.component.FileSource"),
    FILTER_NODE("FilterNode", "com.dataexp.graph.logic.component.FilterNode"),
    FORMATTER_NODE("FormatterNode", "com.dataexp.graph.logic.component.FormatterNode"),
    SPLIT_NODE("SplitNode", "com.dataexp.graph.logic.component.SplitNode"),
    UNION_NODE("UnionNode", "com.dataexp.graph.logic.component.UnionNode"),
    WASH_NODE("WashNode", "com.dataexp.graph.logic.component.WashNode");

    private final Logger LOG = LoggerFactory.getLogger(ComponentType.class);
    private String typeName;
    private Class componentClass;

    private ComponentType(String typeName, String componentClassName) {
        this.typeName = typeName;
        try {
            this.componentClass = Class.forName(componentClassName);
        } catch (ClassNotFoundException e) {
            LOG.error("can initial component class of " + componentClassName + e);
        }
    }

    public String getTypeName() {
        return typeName;
    }

    public Class getComponentClass() {
        return componentClass;
    }

    /**
     * 根据组件类型名称获取组件的ENUM，支持大小写模糊匹配
     *
     * @param typeName
     * @return
     */
    public static ComponentType fromComponentType(String typeName) {
        if (typeName != null) {
            for (ComponentType b : ComponentType.values()) {
                if (typeName.equalsIgnoreCase(b.getTypeName())) {
                    return b;
                }
            }
        }
        return null;
    }

    /**
     * 根据传入的组件类，获取组件类型名称
     * @param T
     * @return
     */
    public static String getComponentTypeName(Class T) {
        if (T != null) {
            for (ComponentType b : ComponentType.values()) {
                if (b.getComponentClass() == T){
                       return b.getTypeName();
                }
            }
        }
        return null;
    }
}
