package com.dataexp.graph.logic;

public abstract class SinkNode extends LogicNode {

    public SinkNode(int id, int x, int y) {
        super(id, x, y);
    }

    public SinkNode(int id, String name, int x, int y) {
        super(id, name, x, y);
    }


    @Override
    public int defaultInputPortNumber() {
        return 1;
    }

    @Override
    public int defaultOutputPorNumber() {
        return 0;
    }

}
