package com.dataexp.graph.logic.component;

import com.dataexp.common.metadata.FieldType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LogicPort {

    //该端口归属节点
    protected final LogicNode parentNode;

    //端口对应的边
    protected final Set<LogicEdge> edges = new HashSet<LogicEdge>();

    //端口绑定的数据格式
    protected List<FieldType> portDataFormat;

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
}
