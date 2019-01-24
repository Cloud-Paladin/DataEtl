package com.dataexp.graph.logic.component;

import com.dataexp.graph.logic.BaseLogicNode;

import java.util.List;

public class FilterNodeBase extends BaseLogicNode {
    public FilterNodeBase(int id, int x, int y) {
        super(id, x, y);
    }

    public FilterNodeBase(int id, String name, int x, int y) {
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
        return 1;
    }

    @Override
    public String getDefaultName() {
        return "过滤";
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