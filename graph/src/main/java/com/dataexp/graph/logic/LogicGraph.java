package com.dataexp.graph.logic;

import com.dataexp.graph.logic.component.LogicNode;
import com.dataexp.graph.logic.component.SinkNode;
import com.dataexp.graph.logic.component.SourceNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 逻辑图维护类，负责和界面交互维护逻辑图以及组件结构
 */
public class LogicGraph {

    private static final Logger LOG = LoggerFactory.getLogger(LogicGraph.class);

    private boolean chaining = true;

    private Map<Integer, LogicNode> logicNodes;
    private Set<Integer> sources;
    private Set<Integer> sinks;

    //TODO:添加节点端口，删除节点端口

    //TODO：添加端口连线，删除端口连线

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
}
