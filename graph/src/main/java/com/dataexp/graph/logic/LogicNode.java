package com.dataexp.graph.logic;

import java.util.*;

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
    private Map<Integer, InputPort> inputPortMap = new TreeMap<>();
    //节点输出端口
    private Map<Integer, OutputPort> outputPortMap = new TreeMap<>();

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

    //初始化的输入输出端口数量
    public abstract int defaultInputPortNumber();
    public abstract int defaultOutputPorNumber();

    //最大输入输出端口数量
    public abstract int maxInputPortNumber();
    public abstract int maxOutputPortNumber();

    //有需要时继承，返回节点不能删除的端口号
    public List<Integer> getForcedPortId() {
        return new ArrayList<Integer>();
    }

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

    public Map<Integer, InputPort> getInputPortMap() {
        return inputPortMap;
    }

    public InputPort getInputPortById(int portId) {
        return getInputPortMap().get(portId);
    }

    public Map<Integer, OutputPort> getOutputPortMap() {
        return outputPortMap;
    }

    public OutputPort getOutputPortById(int portId) {
        return getOutputPortMap().get(portId);
    }

    public InputPort createInputPort(int id) {
        if (getInputPortMap().size() >= maxInputPortNumber()) {
            return null;
        }
        String portName = "输入端口" + (inputPortMap.size() + 1);
        InputPort port = new InputPort(this, id, portName);
        inputPortMap.put(port.getId(), port);
        return port;
    }


    public OutputPort createOutputPort(int id) {
        if (getOutputPortMap().size() >= maxOutputPortNumber()) {
            return null;
        }
        String portName = "输出端口" + (outputPortMap.size() + 1);
        OutputPort port = new OutputPort(this, id, portName);
        outputPortMap.put(port.getId(), port);
        return port;
    }

    //删除输入端口
    public boolean removeInputPort(int portId) {
        InputPort ip = getInputPortById(portId);
        if (ip != null & !getForcedPortId().contains(portId) & getInputPortMap().size() > defaultInputPortNumber()) {
            for (LogicEdge ed : ip.getEdges().values()) {
                //TODO:删除端口连线
            }
            getInputPortMap().remove(portId);
        }
        return false;
    }

    //TODO:删除输出端口
    public boolean removeOutputPort(int portId) {
        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

