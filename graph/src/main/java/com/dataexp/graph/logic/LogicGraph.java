package com.dataexp.graph.logic;

import com.dataexp.graph.logic.component.LogicNodeFactory;
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
    private Map<Integer, LogicNode> logicNodes = new HashMap<>();

    //节点,边,端口的当前最大id
    private int maxNodeId = 1;
    private int maxPortId = 1;
    private int maxEdgeId = 1;

    public LogicGraph() {
    }

    public LogicGraph(List<LogicNode> nodes, List<LogicPort> ports, List<LogicEdge> edges) {
        for (LogicNode node : nodes) {
            addNode(node);
        }
    }

    private int getNextNodeId() {
        return maxNodeId++;
    }

    private int getNextEdgeId() {
        return maxEdgeId++;
    }

    private int getNextPortId(){
        return maxPortId++;
    }

    public boolean createNode(String type, String name, int xcoordinate, int ycoordinate) {
        LogicNode node = LogicNodeFactory.createNode(getNextNodeId(), type, name, xcoordinate, ycoordinate);
        return node == null ? false : addNode(node);
    }

    public boolean addNode(LogicNode node) {
        //TODO:添加节点的端点
        //TODO:添加节点的边
        return (logicNodes.put(node.getId(), node) == null);
    }

    public boolean removeNode(LogicNode node) {
        //TODO:删除节点的端点
        //TODO：删除节点的边
        return (logicNodes.remove(node.getId()) != null);
    }

    //TODO:配置节点属性

    //TODO:添加节点端口，删除节点端口，修改端口名称
    public boolean createInputPort(int nodeId){
        return false;
    }

    public boolean createOutputPort(int nodeId){
        return false;
    }

    //TODO：添加端口连线，删除端口连线

    /**
     * 从输出端口添加一条到另一个组件的输入端口的连线
     * @param ：
     * @return 是否成功添加了连线
     */
    public boolean createEdge(OutputPort OP, InputPort IP) {
        //TODO:回环校验,组件内回环和多组件回环
        if(IP.can) {
            return false;
        }

        LogicEdge edge = new LogicEdge(OP,IP,);
        OP.addEdge(edge);
        IP.addEdge(edge);
        return true;
    }

    public boolean removeEdge(LogicEdge edge) {
        return false;
    }

    public boolean isChaining() {
        return chaining;
    }

    public void setChaining(boolean chaining) {
        this.chaining = chaining;
    }

    public Map<Integer, LogicNode> getLogicNodes() {
        return logicNodes;
    }

    public static void main(String[] args) {
        LogicGraph a = new LogicGraph();
    }
}
