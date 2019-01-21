package com.dataexp.graph.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 逻辑图,负责维护逻辑图及其组件结构
 */
public class LogicGraph {

    private static final Logger LOG = LoggerFactory.getLogger(LogicGraph.class);
    //Chain策略
    private boolean chaining = true;
    //逻辑图包含的节点,key为节点id
    private Map<Integer, LogicNode> nodeMap = new HashMap<>();

    private Map<Integer, LogicEdge> edgeMap = new HashMap<>();

    //节点,边,端口的当前最大id
    private int maxNodeId = 1;
    private int maxPortId = 1;
    private int maxEdgeId = 1;

    public LogicGraph() {
    }

    private int getNextNodeId() {
        return maxNodeId++;
    }

    private int getNextEdgeId() {
        return maxEdgeId++;
    }

    private int getNextPortId() {
        return maxPortId++;
    }

    public LogicNode createNode(String type, int x, int y) {
        LogicNode node = ComponentFactory.createNode(getNextNodeId(), type, x, y);
        initNodePorts(node);
        addNode(node);
        return node;
    }

    public LogicNode createNode(String type, String name, int x, int y) {
        LogicNode node = ComponentFactory.createNode(getNextNodeId(), type, name, x, y);
        initNodePorts(node);
        addNode(node);
        return node;
    }

    public void initNodePorts(LogicNode node) {
        for (int i = 1; i <= node.defaultInputPortNumber(); i++) {
            InputPort port = node.createInputPort(getNextPortId());
        }
        for (int i = 1; i <= node.defaultOutputPorNumber(); i++) {
            OutputPort port = node.createOutputPort(getNextPortId());
        }
    }

    public boolean addNode(LogicNode node) {
        return (nodeMap.put(node.getId(), node) == null);
    }

    public LogicNode findNode(int nodeId) {
        return nodeMap.get(nodeId);
    }

    public LogicNode removeNode(int nodeId) {
        return nodeMap.remove(nodeId);
    }

    //TODO:配置节点属性

    //TODO:添加节点端口，删除节点端口，修改端口名称
    public InputPort createInputPort(int nodeId) {
        LogicNode ln = nodeMap.get(nodeId);
        if (ln != null) {
            return ln.createInputPort(getNextPortId());
        }
        return null;
    }

    public OutputPort createOutputPort(int nodeId) {
        LogicNode ln = nodeMap.get(nodeId);
        if (ln != null) {
            return ln.createOutputPort(getNextPortId());
        }
        return null;
    }

    public boolean removeInputPort(int nodeId, int portId) {
        LogicNode ln = nodeMap.get(nodeId);
        if (ln != null) {
            return ln.removeInputPort(portId);
        }
        return false;
    }

    public boolean removeOutputPort(int nodeId, int portId) {
        LogicNode ln = nodeMap.get(nodeId);
        if (ln != null) {
            return ln.removeOutputPort(portId);
        }
        return false;
    }

    //TODO：添加端口连线，删除端口连线
    public boolean createEdge(int OutputPortId, int InputPortId) {
        //判断连接添加后是否会出现回环现象，出现回环现象返回错误
        //获取输出端口数据格式，获取输入端口数据格式
        //如果输入端口无格式，则向输入端口询问是否可以接收连接，连接成功过后将输入端口的格式赋给输入端口
        //如果输入端口有格式，则判断两个格式是否一致，如果不一致提示错误

        return false;
    }

    public boolean removeEdge(int OutputPortId, int InputPortId) {

        return false;
    }
    /**
     * 从输出端口添加一条到另一个组件的输入端口的连线
     *
     * @param ：
     * @return 是否成功添加了连线
     */
//    public boolean createEdge(OutputPort OP, InputPort IP) {
//        //TODO:回环校验,组件内回环和多组件回环
//        if (IP.can) {
//            return false;
//        }
//
//        LogicEdge edge = new LogicEdge(OP, IP, );
//        OP.addEdge(edge);
//        IP.addEdge(edge);
//        return true;
//    }


    public boolean isChaining() {
        return chaining;
    }

    public void setChaining(boolean chaining) {
        this.chaining = chaining;
    }

    public Map<Integer, LogicNode> getNodeMap() {
        return nodeMap;
    }

    public static void main(String[] args) {
        LogicGraph lg = new LogicGraph();
        lg.createNode("FileSink", 0, 20);
        System.out.println(lg.nodeMap.get(1));
    }
}
