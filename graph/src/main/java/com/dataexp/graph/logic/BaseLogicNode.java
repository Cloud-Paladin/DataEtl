package com.dataexp.graph.logic;

import com.dataexp.graph.logic.component.ComponentType;
import com.dataexp.graph.logic.serial.SerialInputPort;
import com.dataexp.graph.logic.serial.SerialNode;
import com.dataexp.graph.logic.serial.SerialOutputPort;
import com.dataexp.jobengine.operation.BaseOperation;
import com.dataexp.jobengine.operation.FilterOperation;
import com.dataexp.jobengine.operation.InputConfig;
import com.dataexp.jobengine.operation.OutputConfig;

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
     * 节点输入端口
     */
    private SortedMap<Integer, InputPort> inputPortMap = new TreeMap<>();
    /**
     * 节点输出端口
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
     *
     * @return
     */
    public abstract String genNodeConfig();

    /**
     * 初始化节点的配置属性
     *
     * @param config 节点配置属性生成的String表示
     */
    public abstract void initNodeConfig(String config);


    /**
     * 根据内容构造自己的SerivalNode
     *
     * @return
     */
    public SerialNode genSerialNode() {
        SerialNode sn = new SerialNode();
        setSerialNodeAttr(sn);
        return sn;
    }

    /**
     * 设置序列化节点属性
     */
    public void setSerialNodeAttr(SerialNode sn) {
        sn.setType(ComponentType.getComponentTypeName(this.getClass()));
        sn.setId(id);
        sn.setName(name);
        sn.setX(x);
        sn.setY(y);
        sn.setInputPortList(new ArrayList(inputPortMap.keySet()));
        sn.setOutputPortList(new ArrayList(outputPortMap.keySet()));
        sn.setConfig(genNodeConfig());
    }

    /**
     * 反向设置序列化节点基础属性
     * 注意：与端口的关系此时无法设置
     *
     * @param sn 序列化节点
     */
    public void deSerialNodeAttr(SerialNode sn) {
        setId(sn.getId());
        setName(sn.getName());
        setX(sn.getX());
        setY(sn.getY());
        initNodeConfig(sn.getConfig());
    }

    /**
     * 根据内容构造自己的输入端口SerialPort
     *
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
     *
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
     * 生成节点对应的从输入port开始操作对应的的BaseOperation
     * operaion的后续operaion在这里不填
     * 注意：输入和输出节点没有相应的operation
     * @param inputPortId: 输入端口端口号
     * @return
     */
    public abstract BaseOperation genBaseOperation(int inputPortId);

    /**
     * 对指定输入端口生成的operation填入基础的属性
     * @param operation
     */
    public void setOperationBaseAttr(BaseOperation operation, int inputPortId) {
        operation.setNodeId(getId());
        InputPort inputPort = getInputPortMap().get(inputPortId);
        operation.setInputConfig(new InputConfig(inputPort.getId(),inputPort.getPortDataFormat()));
        for (OutputPort outputPort : getOutputPortMap().values()) {
            OutputConfig outputConfig = new OutputConfig();
            //注意：此处没有生成nextOperationList
            outputConfig.setOutputPortId(outputPort.getId());
            outputConfig.setOutputType(outputPort.getPortDataFormat());
            operation.addOutputConfig(outputConfig);
        }
    }

    /**
     * 初始化的输入端口数量
     *
     * @return
     */
    public abstract int defaultInputPortNumber();

    /**
     * 初始化的输出端口数量
     *
     * @return
     */
    public abstract int defaultOutputPorNumber();

    /**
     * 最大输入端口数量
     *
     * @return
     */
    public abstract int maxInputPortNumber();

    /**
     * 最大输出端口数量
     *
     * @return
     */
    public abstract int maxOutputPortNumber();

    /**
     * 有需要时继承，返回节点不能删除的端口号
     *
     * @return
     */
    public List<Integer> getForcedPortId() {
        return new ArrayList<Integer>();
    }

    /**
     * 返回节点的默认名称
     *
     * @return
     */
    public abstract String getDefaultName();

    /**
     * 返回该组件的Chain策略
     *
     * @return
     */
    public abstract ChainingStrategy getChainingStrategy();

    /**
     * 返回节点当前异常
     *
     * @return
     */
    public abstract List<String> getExceptions();

    /**
     * 返回节点警告
     *
     * @return
     */
    public abstract List<String> getWarnings();

    /**
     * 移动节点
     *
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
        addInputPort(port);
        return port;
    }

    public void addInputPort(InputPort port) {
        inputPortMap.put(port.getId(), port);
    }


    public OutputPort createOutputPort(int id) {
        if (getOutputPortMap().size() >= maxOutputPortNumber()) {
            return null;
        }
        String portName = "输出端口" + (outputPortMap.size() + 1);
        OutputPort port = new OutputPort(this, id, portName);
        addOutputPort(port);
        return port;
    }

    public void addOutputPort(OutputPort port) {
        outputPortMap.put(port.getId(), port);
    }

    public boolean removeInputPort(int portId) {
        InputPort ip = getInputPortById(portId);
        if (ip != null && !getForcedPortId().contains(portId) && getInputPortMap().size() > defaultInputPortNumber()) {
            if (ip.getLinkedPortMap().size() > 0) {
                return false;
            }
            getInputPortMap().remove(portId);
        }
        return false;
    }

    public boolean removeOutputPort(int portId) {
        OutputPort op = getOutputPortById(portId);
        if (op != null && !getForcedPortId().contains(portId) && getOutputPortMap().size() > defaultOutputPorNumber()) {
            if (op.getLinkedPortMap().size() > 0) {
                return false;
            }
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

