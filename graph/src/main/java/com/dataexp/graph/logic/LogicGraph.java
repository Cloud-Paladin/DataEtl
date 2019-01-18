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
    private Map<Integer, LogicNode> logicNodes = new HashMap<>();
    //逻辑图包含的数据源节点id
    private Set<Integer> sources = new HashSet<>();
    //逻辑图包含的目的节点id
    private Set<Integer> sinks = new HashSet<>();

    //节点,边,端口的当前最大id
    private int maxNodeId = 0;
    private int maxEdgeId = 0;
    private int maxPortId = 0;

    public LogicGraph() {
    }

    public LogicGraph(List<LogicNode> nodes) {
        for (LogicNode node : nodes) {
            addNode(node);
        }
    }

    public boolean addNode(LogicNode node) {
        if (node instanceof SourceNode) {
            sources.add(node.getId());
        } else if (node instanceof SinkNode) {
            sinks.add(node.getId());
        }
        return (logicNodes.put(node.getId(), node) == null);
    }

    public boolean removeNode(LogicNode node) {
        if (node instanceof SourceNode) {
            sources.remove(node.getId());
        } else if (node instanceof SinkNode) {
            sinks.remove(node.getId());
        }
        return (logicNodes.remove(node.getId()) != null);
    }

    //TODO:配置节点属性

    //TODO:添加节点端口，删除节点端口，修改端口名称

    //TODO：添加端口连线，删除端口连线

    public boolean isChaining() {
        return chaining;
    }

    public void setChaining(boolean chaining) {
        this.chaining = chaining;
    }

    public Map<Integer, LogicNode> getLogicNodes() {
        return logicNodes;
    }

    public Set<Integer> getSources() {
        return sources;
    }

    public Set<Integer> getSinks() {
        return sinks;
    }


    public static void main(String[] args) {
        LogicGraph a = new LogicGraph();
        System.out.println(a.getSources().size());
    }
}
