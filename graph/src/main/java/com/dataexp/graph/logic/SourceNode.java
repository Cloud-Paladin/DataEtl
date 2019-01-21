package com.dataexp.graph.logic;

import jdk.internal.util.xml.impl.Input;

public abstract class SourceNode extends LogicNode {

    public SourceNode(int id, String name, int x, int y) {
        super(id, name, x, y);
    }

    public SourceNode(int id, int x, int y) {
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
