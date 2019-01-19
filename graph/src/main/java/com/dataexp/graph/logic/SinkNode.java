package com.dataexp.graph.logic;

public class SinkNode extends LogicNode {

    public SinkNode(int id, String name, int xcoordinate, int ycoordinate) {
        super(id, name, xcoordinate, ycoordinate);
    }


    public InputPort createInputPort(int id, String name) {
        //输出节点默认只能有一个输入
        if (getInputPorts().size() > 0) {
            return null;
        }
        return super.createInputPort(id, name);
    }

    //输出节点默认没有输出端口
    public OutputPort createOutputPort(int id, String name) {
        return null;
    }

}
