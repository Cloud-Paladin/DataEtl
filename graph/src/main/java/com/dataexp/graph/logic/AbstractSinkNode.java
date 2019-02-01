package com.dataexp.graph.logic;

public abstract class AbstractSinkNode extends BaseLogicNode {

    public AbstractSinkNode(int id, int x, int y) {
        super(id, x, y);
    }

    public AbstractSinkNode(int id, String name, int x, int y) {
        super(id, name, x, y);
    }

    @Override
    public ChainingStrategy getChainingStrategy() {
        return ChainingStrategy.NEVER;
    }

    @Override
    public int defaultInputPortNumber() {
        return 1;
    }

    @Override
    public int defaultOutputPorNumber() {
        return 0;
    }

    @Override
    public int maxInputPortNumber() {
        return 1;
    }

    @Override
    public int maxOutputPortNumber() {
        return 0;
    }
}
