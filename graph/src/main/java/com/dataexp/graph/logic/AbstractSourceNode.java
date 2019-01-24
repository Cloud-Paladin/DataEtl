package com.dataexp.graph.logic;


public abstract class AbstractSourceNode extends BaseLogicNode {

    public AbstractSourceNode(int id, String name, int x, int y) {
        super(id, name, x, y);
    }

    public AbstractSourceNode(int id, int x, int y) {
        super(id, x, y);
    }

    @Override
    public int defaultInputPortNumber() {
        return 0;
    }

    @Override
    public int defaultOutputPorNumber() {
        return 1;
    }

    @Override
    public int maxInputPortNumber() {
        return 0;
    }

    @Override
    public int maxOutputPortNumber() {
        return 1;
    }
}
