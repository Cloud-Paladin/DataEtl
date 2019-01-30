package com.dataexp.graph.logic.component;

import com.dataexp.graph.logic.BaseLogicNode;

import java.util.List;

public class SplitNode extends BaseLogicNode {
    public SplitNode(int id, int x, int y) {
        super(id, x, y);
    }

    public SplitNode(int id, String name, int x, int y) {
        super(id, name, x, y);
    }

    @Override
    public String genNodeConfig() {
        return null;
    }

    @Override
    public void initNodeConfig(String config) {

    }

    @Override
    public int defaultInputPortNumber() {
        return 1;
    }

    @Override
    public int defaultOutputPorNumber() {
        return 2;
    }

    @Override
    public int maxInputPortNumber() {
        return 1;
    }

    @Override
    public int maxOutputPortNumber() {
        return 8;
    }

    @Override
    public String getDefaultName() {
        return "分支";
    }

    @Override
    public List<String> getExceptions() {
        return null;
    }

    @Override
    public List<String> getWarnings() {
        return null;
    }
}
