package com.dataexp.graph.logic;

import com.dataexp.common.metadata.FieldType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;


/**
 * @description:  逻辑图,负责维护逻辑图及其组件结构
 * @author: Bing.Li
 * @create: 2019-01-23 14:17
 **/
public class LogicGraph {

    private static final Logger LOG = LoggerFactory.getLogger(LogicGraph.class);
    //Chain策略
    private boolean chaining = true;
    //逻辑图包含的节点,key为节点id
    private Map<Integer, LogicNode> nodeMap = new HashMap<>();

    /**
     * 逻辑图的边Map
     */
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

    /**
     * 清理自己，为转换做准备，包括清理无效节点，无效端口等
     * @return
     */
    public boolean cleanGraph() {
        //清理前进行检查，查看图是否能够运行
        if (!graphCheck()) {
            return false;
        }
        //TODO:整体检查，删除孤立节点和边,如果grapCheck不允许有孤立节点则不需要做

        for (LogicNode node : getNodeMap().values()) {
            //TODO:每个节点清理自己的无效端口
        }

        //清理后进行检查，查看图是否能够运行
        if (!graphCheck()) {
            return false;
        }
        return true;
    }

    /**
     * 逻辑图检查，判断是否能够运行
     * @return
     */
    public boolean graphCheck() {
        //单个组件检查
        if (getGraphExceptions().size() > 0) {
            return false;
        }
        //TODO:整张图检查，检查连通性，孤立节点等
        return true;
    }

    /**
     * 获取整张图的异常
     * @return
     */
    public Map<Integer, List<String>> getGraphExceptions() {
        Map<Integer,List<String>> exceptionMap = new HashMap<Integer, List<String>>();
        for (LogicNode node : getNodeMap().values()) {
            List<String> temp = node.getExceptions();
            if (temp.size() > 0) {
                exceptionMap.put(node.getId(), temp);
            }
        }
        return exceptionMap;
    }

    /**
     * 获取整张图的警告
     * @return
     */
    public Map<Integer, List<String>> getGraphWarnings() {
        Map<Integer,List<String>> wariningMap = new HashMap<Integer, List<String>>();
        for (LogicNode node : getNodeMap().values()) {
            List<String> temp = node.getWarnings();
            if (temp.size() > 0) {
                wariningMap.put(node.getId(), temp);
            }
        }
        return wariningMap;
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

    public LogicNode getNodeFromPortId(int portId) {
        for (LogicNode node : getNodeMap().values()) {
            if (null != node.getInputPortById(portId)) {
                return node;
            }
            if (null != node.getOutputPortById(portId)) {
                return node;
            }
        }
        return null;
    }

    /**
     * 检查两个端口的数据格式是否一致s
     * @param p1
     * @param p2
     * @return
     */
    public static boolean isDataFormatEqual(LogicPort p1, LogicPort p2) {
        List<FieldType> format1 = p1.getPortDataFormat();
        List<FieldType> format2 = p2.getPortDataFormat();
        if (format1.size() != format2.size()) {
            return false;
        }
        for (int i=0; i<format1.size(); i++) {
            FieldType ft1 = format1.get(i);
            FieldType ft2 = format2.get(i);
            //目前仅仅判断基本类型，扩展类型和字段名称未判断
            if (ft1.getBaseType() != ft2.getBaseType() ) {
                return false;
            }
        }
        return true;
    }

    //TODO：添加端口连线，删除端口连线
    public LogicEdge createEdge(int outputPortId, int inputPortId) {
        //判断连接添加后是否会出现回环现象，出现回环现象返回错误
        if (loopBackCheck(outputPortId, inputPortId)) {
            return null;
        }

        //输出端口没有格式则返回错误
        LogicNode sourceNode = getNodeFromPortId(outputPortId);
        LogicNode targetNode = getNodeFromPortId(inputPortId);

        if (null != sourceNode && null != targetNode && sourceNode != targetNode) {

            OutputPort op = sourceNode.getOutputPortById(outputPortId);
            InputPort ip = targetNode.getInputPortById(inputPortId);

            //判断两个端口是否已经存在连线
            for (LogicEdge eg : op.getEdges().values()) {
                if (outputPortId == eg.getOutputPort().getId() && inputPortId == eg.getInputPort().getId()) {
                    return null;
                }
            }

            //输出端口没格式不允许添加连线
            if ( op.getPortDataFormat().size() == 0) {
                return null;
            }
            //如果输入端口有格式，则判断两个格式是否一致，如果不一致提示错误,只要格式一致，允许一个输入端口从多个输出端口连线
            if (ip.getPortDataFormat().size() > 0 && !isDataFormatEqual(op, ip)) {
                return null;
            }
            //如果输入端口无格式，将输入端口的格式赋给输入端口
            if (0 == ip.getPortDataFormat().size()){
                ip.setPortDataFormat(op.getPortDataFormat());
                //TODO:测试代码，默认将ip的所有出口的格式也设置为入口格式，以后删除！
                for (OutputPort p : targetNode.getOutputPortMap().values()) {
                    p.setPortDataFormat(op.getPortDataFormat());
                }
            }
            //生成连线
            LogicEdge eg = new LogicEdge(op, ip, getNextEdgeId());
            op.addEdge(eg);
            ip.addEdge(eg);
            edgeMap.put(eg.getId(), eg);
            return eg;
        }
        return null;
    }

    /**
     * 检查在两个端口间添加一条边后，逻辑图是否会出现回环
     * @param outputPortId：待添加边的出端口
     * @param inputPortId：待添加边的入端口
     * @return 添加该边后逻辑图是否会出现回环
     */
    public boolean loopBackCheck(int outputPortId, int inputPortId) {
        //TODO:回环检查
        return false;
    }

    //TODO:删除边
    public boolean removeEdge(int edgeId) {
        LogicEdge eg = edgeMap.get(edgeId);
        if (null == eg) {
            return false;
        }
        eg.getOutputPort().getEdges().remove(edgeId);
        eg.getInputPort().getEdges().remove(edgeId);
        return false;
    }

    public boolean isChaining() {
        return chaining;
    }

    public void setChaining(boolean chaining) {
        this.chaining = chaining;
    }

    public Map<Integer, LogicNode> getNodeMap() {
        return nodeMap;
    }

    public Map<Integer, LogicEdge> getEdgeMap() {
        return edgeMap;
    }

}
