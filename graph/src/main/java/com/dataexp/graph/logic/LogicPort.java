package com.dataexp.graph.logic;

import com.dataexp.common.metadata.FieldType;

import java.util.*;

public class LogicPort<T extends LogicPort>  {

    /**
     * 该端口归属节点
     */
    private BaseLogicNode parentNode;

    /**
     * 该端口id
     */
    private int id;

    /**
     * 端口名称
     */
    private String name;

    /**
     * 端口绑定的数据格式
     */
    private List<FieldType> portDataFormat = new ArrayList<>();

    /**
     * 连接的其他端口列表，对于InputPort来说就是OutputPort
     * 对于OutputPort来说就是InputPort
     * key: port的Id
     * value: port
     */
    private Map<Integer, T> linkedPortMap = new HashMap<>();

    public LogicPort() {
    }

    public void setParentNode(BaseLogicNode parentNode) {
        this.parentNode = parentNode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LogicPort(BaseLogicNode parentNode, int id, String name) {
        this.id = id;
        this.parentNode = parentNode;
        this.name = name;
    }

    public BaseLogicNode getParentNode() {
        return parentNode;
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

    public Map<Integer, T> getLinkedPortMap() {
        return linkedPortMap;
    }

    public void setLinkedPortMap(Map<Integer, T> linkedPortMap) {
        this.linkedPortMap = linkedPortMap;
    }

    public void addLinkedPort(T port) {
        getLinkedPortMap().put(port.getId(),port);
    }

    public void removeLinkedPort(T port) {
        getLinkedPortMap().remove(port.getId());
    }
}
