package com.dataexp.graph.logic;

import com.dataexp.common.metadata.FieldType;
import com.dataexp.graph.logic.serial.SerialInputPort;
import com.dataexp.graph.logic.serial.SerialPort;

import java.util.*;

public abstract class LogicPort<T extends LogicPort, U extends SerialPort>  {

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

    /**
     * 序列化获取SerialPort对象
     * @return
     */
    public abstract U genSerialPort();

    /**
     * 设置serialPort的基础参数
     * @return
     */
    public void setSerialPort(SerialPort sp){
        sp.setNodeId(getParentNode().getId());
        sp.setId(getId());
        sp.setName(getName());
        sp.setPortDataFormat(getPortDataFormat());
        sp.setLinkedPortList(Arrays.asList((Integer[])getLinkedPortMap().keySet().toArray()));
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
