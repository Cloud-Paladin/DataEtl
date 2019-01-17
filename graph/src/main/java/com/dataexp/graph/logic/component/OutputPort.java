package com.dataexp.graph.logic.component;

public class OutputPort extends LogicPort {

    public OutputPort(LogicNode parentNode) {
        super(parentNode);
    }


    /**
     * 从输出端口添加一条到另一个组件的输入端口的连线
     * @param port：另一个组件的输入端口
     * @return 是否成功添加了连线
     */
    public boolean addEdge(InputPort port) {
        //TODO:回环校验,组件内回环和多组件回环
        if(port.getEdges().size() > 0) {
            return false;
        }
        LogicEdge edge = new LogicEdge(this, port);
        return edges.add(edge);
    }
}
