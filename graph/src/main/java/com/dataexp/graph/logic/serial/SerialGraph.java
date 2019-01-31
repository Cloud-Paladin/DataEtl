package com.dataexp.graph.logic.serial;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Bing.Li
 * @create: 2019-01-30
 */
public class SerialGraph {
    private List<SerialNode> nodeList = new ArrayList<>();
    private List<SerialInputPort> inputPortList = new ArrayList<>();
    private List<SerialOutputPort> outputPortList = new ArrayList<>();
    private int maxNodeId;
    private int maxPortId;
    private int maxEdgeId;

    public SerialGraph() {
    }

    public int getMaxNodeId() {
        return maxNodeId;
    }

    public void setMaxNodeId(int maxNodeId) {
        this.maxNodeId = maxNodeId;
    }

    public int getMaxPortId() {
        return maxPortId;
    }

    public void setMaxPortId(int maxPortId) {
        this.maxPortId = maxPortId;
    }

    public int getMaxEdgeId() {
        return maxEdgeId;
    }

    public void setMaxEdgeId(int maxEdgeId) {
        this.maxEdgeId = maxEdgeId;
    }

    public void addNode(SerialNode node) {
        getNodeList().add(node);
    }

    public List<SerialNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<SerialNode> nodeList) {
        this.nodeList = nodeList;
    }

    public void addInputPort(SerialInputPort port){
        getInputPortList().add(port);
    }

    public void addOutputPort(SerialOutputPort port) {
        getOutputPortList().add(port);
    }

    public List<SerialInputPort> getInputPortList() {
        return inputPortList;
    }

    public void setInputPortList(List<SerialInputPort> inputPortList) {
        this.inputPortList = inputPortList;
    }

    public List<SerialOutputPort> getOutputPortList() {
        return outputPortList;
    }

    public void setOutputPortList(List<SerialOutputPort> outputPortList) {
        this.outputPortList = outputPortList;
    }
}
