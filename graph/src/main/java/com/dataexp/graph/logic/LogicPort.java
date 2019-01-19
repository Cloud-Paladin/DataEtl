package com.dataexp.graph.logic;

import com.dataexp.common.metadata.FieldType;

import java.util.*;

public class LogicPort {

    //该端口归属节点
    private final LogicNode parentNode;

    //该端口id
    private final int id;

    //端口名称
    private String name;

    //端口对应的边
    private final Map<Integer, LogicEdge> edges = new HashMap<>();

    //端口绑定的数据格式
    private List<FieldType> portDataFormat = new ArrayList<>();

    public LogicPort(LogicNode parentNode, int id, String name) {
        this.id = id;
        this.parentNode = parentNode;
        this.name = name;
    }

    public LogicNode getParentNode() {
        return parentNode;
    }

    public boolean addEdge(LogicEdge edge) {
        return edges.put(edge.getId(), edge) == null ? true: false;
    }

    public Map<Integer, LogicEdge> getEdges() {
        return edges;
    }

    public List<FieldType> getPortDataFormat() {
        return portDataFormat;
    }

    public void clearPortDataFormat() {
        portDataFormat = new ArrayList<>();
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

    public int getId() {
        return id;
    }
}
