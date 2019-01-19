package com.dataexp.graph.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用的流程组件接口，定义组件的通用操作
 */
public abstract class LogicNode {

    //逻辑流程图中的节点id
    private final int id;

    //节点名称
    private String name;

    //节点坐标
    private int xcoordinate, ycoordinate;

    //节点输入端口
    private Map<Integer, InputPort> inputPorts = new HashMap<>();
    //节点输出端口
    private Map<Integer, OutputPort> outputPorts = new HashMap<>();

    protected final int maxInput = 1;

    public LogicNode(int id, String name, int xcoordinate, int ycoordinate) {
        this.id = id;
        this.name = name;
        this.xcoordinate = xcoordinate;
        this.ycoordinate = ycoordinate;
    }

    //移动节点
    public void moveNode(int xcoordinate, int ycoordinate) {
        setXcoordinate(xcoordinate);
        setYcoordinate(ycoordinate);
    }

    public void setXcoordinate(int xcoordinate) {
        this.xcoordinate = xcoordinate;
    }

    public void setYcoordinate(int ycoordinate) {
        this.ycoordinate = ycoordinate;
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

    public Map<Integer, InputPort> getInputPorts() {
        return inputPorts;
    }

    public void setInputPorts(Map<Integer, InputPort> inputPorts) {
        this.inputPorts = inputPorts;
    }

    public InputPort createInputPort(int id, String name) {
        InputPort port = new InputPort(this, id, name);
        inputPorts.put(port.getId(), port);
        return port;
    }

    public boolean removeInputPort(int id) {
        return inputPorts.remove(id) == null ? false : true;
    }

    public OutputPort createOutputPort(int id ,String name) {
        OutputPort port = new OutputPort(this, id, name);
        outputPorts.put(port.getId(), port);
        return port;
    }

    public boolean removeOutputPort(int id) {
        return outputPorts.remove(id) == null ? false : true;
    }

    public Map<Integer, OutputPort> getOutputPorts() {
        return outputPorts;
    }

    public void setOutputPorts(Map<Integer, OutputPort> outputPorts) {
        this.outputPorts = outputPorts;
    }

    public int getXcoordinate() {
        return xcoordinate;
    }

    public int getYcoordinate() {
        return ycoordinate;
    }
}

