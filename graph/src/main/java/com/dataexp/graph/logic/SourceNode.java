package com.dataexp.graph.logic;

import jdk.internal.util.xml.impl.Input;

public class SourceNode extends LogicNode {

    public SourceNode(int id, String name, int xcoordinate, int ycoordinate) {
        super(id, name, xcoordinate, ycoordinate);
    }

    //源节点不能创建输入端口
    public InputPort createInputPort(int id, String name) {
        return null;
    }

    public OutputPort createOutputPort(int id, String name) {
        //源节点默认只能有一个输出端口
        if(getOutputPorts().size() > 0 ) {
            return null;
        }
        return super.createOutputPort(id, name);
    }

}
