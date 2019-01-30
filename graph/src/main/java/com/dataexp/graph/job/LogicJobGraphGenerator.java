package com.dataexp.graph.job;

import com.alibaba.fastjson.JSON;
import com.dataexp.common.metadata.BaseType;
import com.dataexp.common.metadata.FieldType;
import com.dataexp.graph.logic.LogicGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 序列化后的端口
 */
class SerialPort<T extends SerialPort> {
    private int id;
    private String name;
    private int pNodeId;
    private List<Integer> linkedPortList;
    private List<FieldType> portDataFormat;
    private SortedMap<Integer, T> linkedPortMap;

    public SortedMap<Integer, T> getLinkedPortMap() {
        return linkedPortMap;
    }

    public void setLinkedPortMap(SortedMap<Integer, T> linkedPortMap) {
        this.linkedPortMap = linkedPortMap;
    }

    public SerialPort() {
    }

    public SerialPort(int id, String name, int pNodeId, List<Integer> linkedPortList, List<FieldType> portDataFormat) {
        this.id = id;
        this.name = name;
        this.pNodeId = pNodeId;
        this.linkedPortList = linkedPortList;
        this.portDataFormat = portDataFormat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getpNodeId() {
        return pNodeId;
    }

    public void setpNodeId(int pNodeId) {
        this.pNodeId = pNodeId;
    }

    public List<Integer> getLinkedPortList() {
        return linkedPortList;
    }

    public void setLinkedPortList(List<Integer> linkedPortList) {
        this.linkedPortList = linkedPortList;
    }

    public List<FieldType> getPortDataFormat() {
        return portDataFormat;
    }

    public void setPortDataFormat(List<FieldType> portDataFormat) {
        this.portDataFormat = portDataFormat;
    }

    public static SerialPort genDemoPort() {
        SerialPort sp = new SerialPort();
        List<Integer> tlist = new ArrayList<>();
        tlist.add(2);
        tlist.add(3);
        FieldType ft1 = new FieldType();
        ft1.setBaseType(BaseType.NUMBER);
        ft1.setDataType("age");
        ft1.setName("age");
        FieldType ft2 = new FieldType();
        ft2.setBaseType(BaseType.STRING);
        ft2.setDataType("city");
        ft2.setName("city");
        List<FieldType> portDataFormat = new ArrayList<>();
        portDataFormat.add(ft1);
        portDataFormat.add(ft2);

        sp.setId(1);
        sp.setName("hee");
        sp.setpNodeId(1);
        sp.setLinkedPortList(tlist);
        sp.setPortDataFormat(portDataFormat);
        return sp;
    }

    public static void main(String[] args) {

        SerialPort sp = genDemoPort();
        String jString = JSON.toJSONString(sp);
        System.out.println(jString);
        SerialPort sp2 = JSON.parseObject(jString, SerialPort.class);
        System.out.println(sp2);
    }
}

class SerialInputPort extends SerialPort<SerialOutputPort> {
    public static SerialInputPort genDemoPort() {
        SerialInputPort sp = new SerialInputPort();
        List<Integer> tlist = new ArrayList<>();
        tlist.add(2);
        tlist.add(3);
        FieldType ft1 = new FieldType();
        ft1.setBaseType(BaseType.NUMBER);
        ft1.setDataType("age");
        ft1.setName("age");
        FieldType ft2 = new FieldType();
        ft2.setBaseType(BaseType.STRING);
        ft2.setDataType("city");
        ft2.setName("city");
        List<FieldType> portDataFormat = new ArrayList<>();
        portDataFormat.add(ft1);
        portDataFormat.add(ft2);
        SortedMap<Integer, SerialOutputPort> lm = new TreeMap<>();
        SerialOutputPort sop1 = new SerialOutputPort();
        sop1.setId(1);
        SerialOutputPort sop2 = new SerialOutputPort();
        sop2.setId(2);
        lm.put(sop1.getId(), sop1);
        lm.put(sop2.getId(), sop2);

        sp.setId(1);
        sp.setName("hee");
        sp.setpNodeId(1);
        sp.setLinkedPortList(tlist);
        sp.setPortDataFormat(portDataFormat);
        sp.setLinkedPortMap(lm);
        return sp;
    }
    public static void main(String[] args) {

        SerialInputPort sp = genDemoPort();
        String jString = JSON.toJSONString(sp);
        System.out.println(jString);
//        SerialPort sp2 = JSON.parseObject(jString, );
        SerialInputPort sp2 = JSON.parseObject(jString, SerialInputPort.class);
        System.out.println(sp2);
    }
}

class SerialOutputPort extends SerialPort<SerialInputPort> {

}


/**
 * 序列化后的节点
 */
class SerialNode{
    private int id;
    private String name;
    private int x,y;
    private List<Integer> inputPortList;
    private List<Integer> outputPortList;
    private String config;
    private List<SerialPort> spList;


    public SerialNode() {
    }

    public SerialNode(int id, String name, int x, int y, List<Integer> inputPortList, List<Integer> outputPortList, String config) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.inputPortList = inputPortList;
        this.outputPortList = outputPortList;
        this.config = config;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public List<Integer> getInputPortList() {
        return inputPortList;
    }

    public void setInputPortList(List<Integer> inputPortList) {
        this.inputPortList = inputPortList;
    }

    public List<Integer> getOutputPortList() {
        return outputPortList;
    }

    public void setOutputPortList(List<Integer> outputPortList) {
        this.outputPortList = outputPortList;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public List<SerialPort> getSpList() {
        return spList;
    }

    public void setSpList(List<SerialPort> spList) {
        this.spList = spList;
    }

    public static SerialNode genDemoNode() {
        SerialNode sn = new SerialNode();
        List<Integer> inputPortList = new ArrayList<>();
        inputPortList.add(1);
        inputPortList.add(2);
        List<Integer> outputPortList = new ArrayList<>();
        outputPortList.add(3);
        outputPortList.add(4);
        List<SerialPort> spList = new ArrayList<>();
        spList.add(SerialPort.genDemoPort());
        spList.add(SerialPort.genDemoPort());
        spList.add(SerialPort.genDemoPort());
        sn.setId(1);
        sn.setName("node");
        sn.setX(50);
        sn.setY(50);
        sn.setConfig("nodeConfig");
        sn.setInputPortList(inputPortList);
        sn.setOutputPortList(outputPortList);
        sn.setSpList(spList);
        return sn;
    }

    public static void main(String[] args) {
        SerialNode sn = genDemoNode();
        String sString = JSON.toJSONString(sn);
        System.out.println(sString);
        SerialNode sn2 = JSON.parseObject(sString, SerialNode.class);
        System.out.println(sn2);
    }
}

class SerialLogicGraph {
    private List<SerialNode> nodeList;

    public SerialLogicGraph() {
    }

    public SerialLogicGraph(List<SerialNode> nodeList) {
        this.nodeList = nodeList;
    }

    public List<SerialNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<SerialNode> nodeList) {
        this.nodeList = nodeList;
    }

    public static SerialLogicGraph genDemoGraph() {

        SerialLogicGraph sg = new SerialLogicGraph();
        List<SerialNode> nodeList = new ArrayList<>();
        SerialNode node1 = SerialNode.genDemoNode();
        SerialNode node2 = SerialNode.genDemoNode();
        nodeList.add(node1);
        nodeList.add(node2);
        sg.setNodeList(nodeList);
        return sg;
    }

    public static void main(String[] args) {
        SerialLogicGraph sg = genDemoGraph();
        String jString = JSON.toJSONString(sg);
        System.out.println(jString);
        SerialLogicGraph sg2 = JSON.parseObject(jString,SerialLogicGraph.class);
        System.out.println(sg2);
    }
}

/**
 * 将LoigcGraph 转化为JobGraph
 */
public class LogicJobGraphGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(LogicJobGraphGenerator.class);

    public static JobGraph createJobGraph(LogicGraph logicGraph) {
        if (!logicGraph.cleanGraph()) {
            return null;
        }


        return null;
    }


}
