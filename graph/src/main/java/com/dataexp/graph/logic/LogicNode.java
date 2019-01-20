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
    private int x, y;

    //节点输入端口
    private Map<Integer, InputPort> inputPorts = new HashMap<>();
    //节点输出端口
    private Map<Integer, OutputPort> outputPorts = new HashMap<>();

    protected final int maxInput = 1;

    public LogicNode(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.name = getDefaultName();
    }

    public LogicNode(int id, String name, int x, int y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public abstract int defaultInputPortNumber();
    public abstract int defaultOutputPorNumber();

    //返回节点的默认名称
    public abstract String getDefaultName();

    //返回节点当前异常
    public abstract List<String> getExceptions();

    //返回节点警告
    public abstract List<String> getWarnings();

    //移动节点
    public void moveNode(int x, int y) {
        setX(x);
        setY(y);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
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

    public InputPort createInputPort(int id) {
        String portName = "输入端口" + (inputPorts.size() + 1);
        InputPort port = new InputPort(this, id, portName);
        inputPorts.put(port.getId(), port);
        return port;
    }


    public OutputPort createOutputPort(int id) {
        String portName = "输出端口" + (outputPorts.size() + 1);
        OutputPort port = new OutputPort(this, id, portName);
        outputPorts.put(port.getId(), port);
        return port;
    }

    public Map<Integer, OutputPort> getOutputPorts() {
        return outputPorts;
    }

    public void setOutputPorts(Map<Integer, OutputPort> outputPorts) {
        this.outputPorts = outputPorts;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

