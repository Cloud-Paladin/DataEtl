package com.dataexp.graph.logic.component;

import com.dataexp.graph.logic.LogicNode;

import java.util.List;

public class WashNode extends LogicNode {
    public WashNode(int id, int x, int y) {
        super(id, x, y);
    }

    public WashNode(int id, String name, int x, int y) {
        super(id, name, x, y);
    }

    @Override
    public int defaultInputPortNumber() {
        return 1;
    }

    @Override
    public int defaultOutputPorNumber() {
        return 1;
    }

    @Override
    public int maxInputPortNumber() {
        return 1;
    }

    @Override
    public int maxOutputPortNumber() {
        //标准输出一个,格式异常、处理异常，校验异常各一个
        return 4;
    }

    @Override
    public String getDefaultName() {
        return "清洗";
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
