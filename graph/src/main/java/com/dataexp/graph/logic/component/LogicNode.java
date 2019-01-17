package com.dataexp.graph.logic.component;

import java.util.List;

/**
 * 通用的流程组件接口，定义组件的通用操作
 */
public abstract class LogicNode {

    //逻辑流程图中的节点id
    private int id;

    //节点坐标
    private int xcoordinate,ycoordinate;

    //节点输入端口
    private List<InputPort> inputPorts;
    //节点输出端口
    private List<OutputPort> outputPorts;


    public LogicNode(int id, int xcoordinate, int ycoordinate) {
        this.id = id;
        this.xcoordinate = xcoordinate;
        this.ycoordinate = ycoordinate;
    }

    //移动节点
    public void moveNode(int xcoordinate, int ycoordinate) {
        setXcoordinate(xcoordinate);
        setYcoordinate(ycoordinate);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<InputPort> getInputPorts() {
        return inputPorts;
    }

    public void setInputPorts(List<InputPort> inputPorts) {
        this.inputPorts = inputPorts;
    }

    public List<OutputPort> getOutputPorts() {
        return outputPorts;
    }

    public void setOutputPorts(List<OutputPort> outputPorts) {
        this.outputPorts = outputPorts;
    }

    public void setXcoordinate(int xcoordinate) {
        this.xcoordinate = xcoordinate;
    }

    public void setYcoordinate(int ycoordinate) {
        this.ycoordinate = ycoordinate;
    }

    public int getXcoordinate() {
        return xcoordinate;
    }

    public int getYcoordinate() {
        return ycoordinate;
    }
}

