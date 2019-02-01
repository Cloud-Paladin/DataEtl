package com.dataexp.graph.logic;

import com.dataexp.common.metadata.FieldType;
import com.dataexp.graph.logic.component.ComponentFactory;
import com.dataexp.graph.logic.serial.SerialGraph;
import com.dataexp.graph.logic.serial.SerialInputPort;
import com.dataexp.graph.logic.serial.SerialNode;
import com.dataexp.graph.logic.serial.SerialOutputPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 逻辑图,负责维护逻辑图及其组件结构
 * //TODO:注意：逻辑图的操作不是线程安全的,需要前台控制同时只能有一个编辑会话
 *
 * @author: Bing.Li
 * @create: 2019-01-23 14:17
 */
public class LogicGraph {

    private static final Logger LOG = LoggerFactory.getLogger(LogicGraph.class);

    /**
     * 逻辑图包含的节点,key为节点id
     */
    private Map<Integer, BaseLogicNode> nodeMap = new HashMap<>();

    private int maxNodeId = 1;
    private int maxPortId = 1;

    public LogicGraph() {
    }


    public int getMaxNodeId() {
        return maxNodeId;
    }

    public int getMaxPortId() {
        return maxPortId;
    }


    private int getNextNodeId() {
        return maxNodeId++;
    }

    private int getNextPortId() {
        return maxPortId++;
    }

    public Map<Integer, BaseLogicNode> getNodeMap() {
        return nodeMap;
    }

    public void setNodeMap(Map<Integer, BaseLogicNode> nodeMap) {
        this.nodeMap = nodeMap;
    }


    public void setMaxNodeId(int maxNodeId) {
        this.maxNodeId = maxNodeId;
    }

    public void setMaxPortId(int maxPortId) {
        this.maxPortId = maxPortId;
    }

    /**
     * 获取逻辑图序列化后的结果
     *
     * @return
     */
    public static SerialGraph genSerialGraph(LogicGraph logicGraph) {
        SerialGraph serialGraph = new SerialGraph();
        serialGraph.setMaxNodeId(logicGraph.getMaxNodeId());
        serialGraph.setMaxPortId(logicGraph.getMaxPortId());
        for (BaseLogicNode node : logicGraph.getNodeMap().values()) {
            SerialNode snode = node.genSerialNode();
            serialGraph.addNode(snode);
            List<SerialInputPort> inputPortList = node.genSerialInputPortList();
            for (SerialInputPort iport : inputPortList) {
                serialGraph.addInputPort(iport);
            }
            List<SerialOutputPort> outputPortList = node.genSerialOutputList();
            for (SerialOutputPort oport : outputPortList) {
                serialGraph.addOutputPort(oport);
            }
        }
        return serialGraph;
    }

    /**
     * 根据序列化内容反序列化生成LogicGraph
     *
     * @return
     */
    public static LogicGraph deSerialGraph(SerialGraph serialGraph) {
        LogicGraph logicGraph = new LogicGraph();
        logicGraph.setMaxNodeId(serialGraph.getMaxNodeId());
        logicGraph.setMaxPortId(serialGraph.getMaxPortId());

        Map<Integer, BaseLogicNode> tmpNode = new HashMap<>();
        Map<Integer, InputPort> tmpInputPort = new HashMap<>();
        Map<Integer, OutputPort> tmpOutputPort = new HashMap<>();
        //首先反序列化生成基础的节点，输入输出端口实例并初始化基础属性
        for (SerialNode snode : serialGraph.getNodeList()) {
            BaseLogicNode node = ComponentFactory.createNode(snode.getType(), snode.getId(), snode.getName(), snode.getX(), snode.getY());
            if (null != node) {
                node.deSerialNodeAttr(snode);
                tmpNode.put(node.getId(), node);
                logicGraph.addNode(node);
            }
        }
        for (SerialInputPort sport : serialGraph.getInputPortList()) {
            InputPort port = new InputPort();
            port.deSerialPortAttr(sport);
            tmpInputPort.put(port.getId(), port);
        }
        for (SerialOutputPort sport : serialGraph.getOutputPortList()) {
            OutputPort port = new OutputPort();
            port.deSerialPortAttr(sport);
            tmpOutputPort.put(port.getId(), port);
        }
        //添加端口和端口的关系
        for (SerialInputPort sport : serialGraph.getInputPortList()) {
            InputPort inputPort = tmpInputPort.get(sport.getId());
            if (null != inputPort) {
                for (int linkedPortId : sport.getLinkedPortList()) {
                    OutputPort outputPort = tmpOutputPort.get(linkedPortId);
                    if (null != outputPort) {
                        //输入输出端口互相添加链接
                        inputPort.addLinkedPort(outputPort);
                        outputPort.addLinkedPort(inputPort);
                    }
                }
            }
        }
        //节点和端口之间的联系
        for (SerialNode serialNode : serialGraph.getNodeList()) {
            BaseLogicNode logicNode = tmpNode.get(serialNode.getId());
            for (int portId : serialNode.getInputPortList()) {
                InputPort port = tmpInputPort.get(portId);
                if (null != port) {
                    //节点和输入端口间的关系
                    logicNode.addInputPort(port);
                    port.setParentNode(logicNode);
                }
            }
            for (int portId : serialNode.getOutputPortList()) {
                OutputPort port = tmpOutputPort.get(portId);
                if (null != port) {
                    //节点和输出端口间的关系
                    logicNode.addOutputPort(port);
                    port.setParentNode(logicNode);
                }
            }
        }
        return logicGraph;
    }

    /**
     * 清理自己，为转换做准备，包括清理无效节点，无效端口等
     *
     * @return
     */
    public boolean cleanGraph() {
        //清理前进行检查，查看图是否能够运行
        if (!graphCheck()) {
            return false;
        }
        //TODO:整体检查，删除孤立节点,如果grapCheck不允许有孤立节点则不需要做

        for (BaseLogicNode node : getNodeMap().values()) {
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
     *
     * @return
     */
    public boolean graphCheck() {

        /**
         *     TODO：单个组件检查
         *     最小校验规则：各组件只检查满足自己运行条件的最小需要字段，如过滤组件只校验过滤条件相关字段
         *     严格校验规则：每个接口的格式必须和配置生效时完全一致
         */
    if (genGraphExceptions().size() > 0) {
            return false;
        }
        /**
         * TODO:整张图检查，检查连通性，孤立节点等
         * 流程为空、流程没有一个有效的输入节点、
         * 流程回环校验
         */
        return true;
    }

    /**
     * 获取整张图的异常
     *
     * @return
     */
    public Map<Integer, List<String>> genGraphExceptions() {
        Map<Integer, List<String>> exceptionMap = new HashMap<Integer, List<String>>(8);
        for (BaseLogicNode node : getNodeMap().values()) {
            List<String> temp = node.getExceptions();
            if (null != temp && temp.size() > 0) {
                exceptionMap.put(node.getId(), temp);
            }
        }
        return exceptionMap;
    }

    /**
     * 获取整张图的警告
     *
     * @return
     */
    public Map<Integer, List<String>> genGraphWarnings() {
        Map<Integer, List<String>> wariningMap = new HashMap<Integer, List<String>>(8);
        for (BaseLogicNode node : getNodeMap().values()) {
            List<String> temp = node.getWarnings();
            if (null != temp && temp.size() > 0) {
                wariningMap.put(node.getId(), temp);
            }
        }
        return wariningMap;
    }

    /**
     * 逻辑图生成一个节点
     *
     * @param typeName
     * @param name
     * @param x
     * @param y
     * @return
     */
    public BaseLogicNode createNode(String typeName, String name, int x, int y) {
        BaseLogicNode node = ComponentFactory.createNode(typeName, getNextNodeId(), name, x, y);
        initNodePorts(node);
        addNode(node);
        return node;
    }

    public void initNodePorts(BaseLogicNode node) {
        for (int i = 1; i <= node.defaultInputPortNumber(); i++) {
            InputPort port = node.createInputPort(getNextPortId());
        }
        for (int i = 1; i <= node.defaultOutputPorNumber(); i++) {
            OutputPort port = node.createOutputPort(getNextPortId());
        }
    }

    /**
     * 逻辑图删除一个节点
     *
     * @param nodeId
     * @return
     */
    public boolean deleteNode(int nodeId) {
        BaseLogicNode node = getNodeMap().get(nodeId);
        if (null == node) {
            return false;
        }
        for (InputPort in : node.getInputPortMap().values()) {
            if (in.getLinkedPortMap().size() > 0) {
                return false;
            }
        }
        for (OutputPort out : node.getOutputPortMap().values()) {
            if (out.getLinkedPortMap().size() > 0) {
                return false;
            }
        }
        removeNode(nodeId);
        return true;
    }

    public boolean addNode(BaseLogicNode node) {
        return (nodeMap.put(node.getId(), node) == null);
    }

    public BaseLogicNode removeNode(int nodeId) {
        return nodeMap.remove(nodeId);
    }

    //TODO:配置节点属性，各节点自己完成

    public InputPort createInputPort(int nodeId) {
        BaseLogicNode ln = nodeMap.get(nodeId);
        if (ln != null) {
            return ln.createInputPort(getNextPortId());
        }
        return null;
    }

    public boolean removeInputPort(int nodeId, int portId) {
        BaseLogicNode ln = nodeMap.get(nodeId);
        if (ln != null) {
            return ln.removeInputPort(portId);
        }
        return false;
    }

    public OutputPort createOutputPort(int nodeId) {
        BaseLogicNode logicNode = nodeMap.get(nodeId);
        if (logicNode != null) {
            return logicNode.createOutputPort(getNextPortId());
        }
        return null;
    }

    public boolean removeOutputPort(int nodeId, int portId) {
        BaseLogicNode ln = nodeMap.get(nodeId);
        if (ln != null) {
            return ln.removeOutputPort(portId);
        }
        return false;
    }

    public BaseLogicNode getNodeFromPortId(int portId, boolean isOutput) {
        for (BaseLogicNode node : getNodeMap().values()) {
            if (isOutput) {
                if (null != node.getOutputPortById(portId)) {
                    return node;
                }
            } else {
                if (null != node.getInputPortById(portId)) {
                    return node;
                }
            }
        }
        return null;
    }

    /**
     * 检查两个端口的数据格式是否一致s
     *
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
        for (int i = 0; i < format1.size(); i++) {
            FieldType ft1 = format1.get(i);
            FieldType ft2 = format2.get(i);
            //目前仅仅判断基本类型，扩展类型和字段名称未判断
            if (ft1.getBaseType() != ft2.getBaseType()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 添加端口之间的连线
     *
     * @param outputPortId 输出端口Id
     * @param inputPortId  输入端口Id
     * @return
     */
    public boolean linkPort(int outputPortId, int inputPortId) {
        //输出端口没有格式则返回错误
        BaseLogicNode sourceNode = getNodeFromPortId(outputPortId, true);
        BaseLogicNode targetNode = getNodeFromPortId(inputPortId, false);

        if (null != sourceNode && null != targetNode && sourceNode != targetNode) {

            OutputPort outputPort = sourceNode.getOutputPortById(outputPortId);
            InputPort inputPort = targetNode.getInputPortById(inputPortId);

            //判断连接添加后是否会出现回环现象，出现回环现象返回错误
            if (loopBackCheck(outputPort, inputPort, null)) {
                return false;
            }

            //判断两个端口是否已经存在连线
            if (outputPort.getLinkedPortMap().containsKey(inputPortId) || inputPort.getLinkedPortMap().containsKey(outputPortId)) {
                return false;
            }

            //输出端口没格式不允许添加连线
            if (outputPort.getPortDataFormat().size() == 0) {
                return false;
            }

            //如果输入端口有格式，则判断两个格式是否一致，如果不一致提示错误,只要格式一致，允许一个输入端口从多个输出端口连线
            if (inputPort.getPortDataFormat().size() > 0 && !isDataFormatEqual(outputPort, inputPort)) {
                return false;
            }

            //如果输入端口无格式，将输入端口的格式赋给输入端口
            if (0 == inputPort.getPortDataFormat().size()) {
                inputPort.setPortDataFormat(outputPort.getPortDataFormat());
                //TODO:测试代码，默认将ip的所有出口的格式也设置为入口格式，
                //TODO: 对于split节点，需要特殊处理，每添加一个输出端口，如果已经有输入端口格式则默认都赋上
                for (OutputPort p : targetNode.getOutputPortMap().values()) {
                    p.setPortDataFormat(outputPort.getPortDataFormat());
                }
            }

            //添加端口连接
            outputPort.addLinkedPort(inputPort);
            inputPort.addLinkedPort(outputPort);
            return true;
        }
        return false;
    }

    /**
     * 检查在两个端口间添加一条边后，逻辑图是否会出现回环
     *
     * @param outputPort：待添加边的出端口
     * @param inputPort：待添加边的入端口
     * @return 添加该边后逻辑图是否会出现回环
     */
    public boolean loopBackCheck(OutputPort outputPort, InputPort inputPort, Map<Integer, BaseLogicNode> checkedNode) {
        if (null == checkedNode) {
            checkedNode = new HashMap<>();
        }
        BaseLogicNode node = inputPort.getParentNode();
        if (null != node && !checkedNode.containsValue(node)) {
            if (node.getOutputPortMap().size() == 0) {
                return false;
            } else if (node.getOutputPortMap().containsValue(outputPort)) {
                return true;
            } else {
                for (OutputPort outPort : node.getOutputPortMap().values()) {
                    for (InputPort inPort : outPort.getLinkedPortMap().values()) {
                        if (loopBackCheck(outputPort, inPort, checkedNode)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 删除端口连接
     *
     * @param outputPortId 输出端口Id
     * @param inputPortId  输入端口Id
     * @return
     */
    public boolean unlinkPort(int outputPortId, int inputPortId) {
        BaseLogicNode sourceNode = getNodeFromPortId(outputPortId, true);
        BaseLogicNode targetNode = getNodeFromPortId(inputPortId, false);

        if (null != sourceNode && null != targetNode && sourceNode != targetNode) {
            OutputPort op = sourceNode.getOutputPortById(outputPortId);
            InputPort ip = targetNode.getInputPortById(inputPortId);
            op.removeLinkedPort(ip);
            ip.removeLinkedPort(op);
            return true;
        }
        return false;
    }
}
