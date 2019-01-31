package com.dataexp.graph.logic.serial;

import com.dataexp.common.metadata.FieldType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Bing.Li
 * @create: 2019-01-30
 */
public class SerialPort {
    private int nodeId;
    private int id;
    private String name;
    private List<Integer> linkedPortList = new ArrayList<>();
    private List<FieldType> portDataFormat = new ArrayList<>();

    public SerialPort() {
    }

    public SerialPort(int nodeId,int id, String name, List<Integer> linkedPortList, List<FieldType> portDataFormat) {
        this.nodeId = nodeId;
        this.id = id;
        this.name = name;
        this.linkedPortList = linkedPortList;
        this.portDataFormat = portDataFormat;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getLinkedPortList() {
        return linkedPortList;
    }

    public void setLinkedPortList(List<Integer> linkedPortList) {
        this.linkedPortList = linkedPortList;
    }

    public List<FieldType> getPortDataFormat() {
        return portDataFormat;
    }

    public void setPortDataFormat(List<FieldType> portDataFormat) {
        this.portDataFormat = portDataFormat;
    }
}
