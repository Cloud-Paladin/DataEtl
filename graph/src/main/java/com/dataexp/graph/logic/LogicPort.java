package com.dataexp.graph.logic;

import com.dataexp.common.metadata.FieldType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LogicPort {

    //该端口归属节点
    private final LogicNode parentNode;

    //端口名称
    private String name;

    //端口对应的边
    private final Set<LogicEdge> edges = new HashSet<LogicEdge>();

    //端口绑定的数据格式
    private List<FieldType> portDataFormat = new ArrayList<>();

    public LogicPort(LogicNode parentNode) {
        this.parentNode = parentNode;
    }


    public LogicNode getParentNode() {
        return parentNode;
    }

    public Set<LogicEdge> getEdges() {
        return edges;
    }

    public List<FieldType> getPortDataFormat() {
        return portDataFormat;
    }

    public void setPortDataFormat(List<FieldType> portDataFormat) {
        this.portDataFormat = portDataFormat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
