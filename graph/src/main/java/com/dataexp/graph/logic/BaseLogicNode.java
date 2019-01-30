package com.dataexp.graph.logic;

import com.dataexp.graph.logic.component.ComponentType;
import com.dataexp.graph.logic.serial.SerialInputPort;
import com.dataexp.graph.logic.serial.SerialNode;
import com.dataexp.graph.logic.serial.SerialOutputPort;
import com.dataexp.graph.logic.serial.SerialPort;

import java.util.*;

/**
 * 通用的流程组件接口，定义组件的通用操作
 */
public abstract class BaseLogicNode {

    //逻辑流程图中的节点id
    private int id;

    //节点名称
    private String name;

    //节点坐标
    private int x, y;

    /**
     *  节点输入端口
     */
    private SortedMap<Integer, InputPort> inputPortMap = new TreeMap<>();
    /**
     *  节点输出端口
     */
    private SortedMap<Integer, OutputPort> outputPortMap = new TreeMap<>();


    public BaseLogicNode() {
    }

    public BaseLogicNode(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.name = getDefaultName();
    }

    public BaseLogicNode(int id, String name, int x, int y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    /**
     * 得到该节点所有配置属性的String表示
     * @return
     */
    public abstract String genNodeConfig();

    /**
     * 初始化节点的配置属性
     * @param config 节点配置属性生成的String表示
     */
    public abstract void initNodeConfig(String config);


    /**
     * 根据内容构造自己的SerivalNode
     * @return
     */
    public SerialNode genSerialNode() {
        SerialNode sn = new SerialNode();
        sn.setType(ComponentType.getComponentTypeName(this.getClass()));
        sn.setId(id);
        sn.setName(name);
        sn.setX(x);
        sn.setY(y);
        sn.setInputPortList(Arrays.asList((Integer[])inputPortMap.keySet().toArray()));
        sn.setInputPortList(Arrays.asList((Integer[])outputPortMap.keySet().toArray()));
        sn.setConfig(genNodeConfig());
        return sn;
    }

    /**
     * 根据内容构造自己的输入端口SerialPort
     * @return
     */
    public List<SerialInputPort> genSerialInputPortList() {
        List<SerialInputPort> spList = new ArrayList<>();
        for (InputPort port : inputPortMap.values()) {
            SerialInputPort sp = port.genSerialPort();
            spList.add(sp);
        }
        return spList;
    }

    /**
     * 根据内容构造自己的输出端口SerialPort
     * @return
     */
    public List<SerialOutputPort> genSerialOutputList() {
        List<SerialOutputPort> spList = new ArrayList<>();
        for (OutputPort port : outputPortMap.values()) {
            SerialOutputPort sp = port.genSerialPort();
            spList.add(sp);
        }
        return spList;
    }

    /**
     * 通过JSON字符串反序列化节点
     * @return
     */
    public SerialNode deSerialNode(String input) {
        //TODO:
        return new SerialNode();
    }
    /**
     * 初始化的输入端口数量
     * @return
     */
    public abstract int defaultInputPortNumber();

    /**
     * 初始化的输出端口数量
     * @return
     */
    public abstract int defaultOutputPorNumber();

    /**
     * 最大输入端口数量
     * @return
     */
    public abstract int maxInputPortNumber();

    /**
     * 最大输出端口数量
     * @return
     */
    public abstract int maxOutputPortNumber();

    /**
     * 有需要时继承，返回节点不能删除的端口号
     * @return
     */
    public List<Integer> getForcedPortId() {
        return new ArrayList<Integer>();
    }

    /**
     * 返回节点的默认名称
     * @return
     */
    public abstract String getDefaultName();

    /**
     * 返回节点当前异常
     * @return
     */
    public abstract List<String> getExceptions();

    /**
     * 返回节点警告
     * @return
     */
    public abstract List<String> getWarnings();

    /**
     * 移动节点
     * @param x
     * @param y
     */
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

    public void setId(int id) {
        this.id = id;
    }

    public void setInputPortMap(SortedMap<Integer, InputPort> inputPortMap) {
        this.inputPortMap = inputPortMap;
    }

    public void setOutputPortMap(SortedMap<Integer, OutputPort> outputPortMap) {
        this.outputPortMap = outputPortMap;
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

    //TODO: 删除输入端口
    public boolean removeInputPort(int portId) {
        InputPort ip = getInputPortById(portId);
        if (ip != null && !getForcedPortId().contains(portId) && getInputPortMap().size() > defaultInputPortNumber()) {
            //TODO:和连接端口互删联系
//            for (LogicEdge ed : ip.getEdges().values()) {
//                //TODO:删除端口连线
//            }
            getInputPortMap().remove(portId);
        }
        return false;
    }

    //TODO:删除输出端口
    public boolean removeOutputPort(int portId) {
        OutputPort op = getOutputPortById(portId);
        if (op != null && !getForcedPortId().contains(portId) && getOutputPortMap().size() > defaultOutputPorNumber()) {
            //TODO:和连接端口互删
            //            for (LogicEdge ed : op.getEdges().values()) {
//                //TODO:删除端口连线
//            }
            getOutputPortMap().remove(portId);
            return false;
        }
        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

