package com.dataexp.graph.logic.component;

import com.dataexp.graph.logic.BaseLogicNode;
import com.dataexp.graph.logic.ChainingStrategy;

import java.util.List;

public class UnionNode extends BaseLogicNode {
    public UnionNode(int id, int x, int y) {
        super(id, x, y);
    }

    public UnionNode(int id, String name, int x, int y) {
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
        return 2;
    }

    @Override
    public int defaultOutputPorNumber() {
        return 1;
    }

    @Override
    public int maxInputPortNumber() {
        return 8;
    }

    @Override
    public int maxOutputPortNumber() {
        return 1;
    }

    @Override
    public String getDefaultName() {
        return "合并转换";
    }

    @Override
    public ChainingStrategy getChainingStrategy() {
        return ChainingStrategy.ALWAYS;
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
