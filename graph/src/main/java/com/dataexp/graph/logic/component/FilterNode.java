package com.dataexp.graph.logic.component;

import com.dataexp.graph.logic.LogicNode;

import java.util.ArrayList;
import java.util.List;

public class FilterNode extends LogicNode {
    public FilterNode(int id, int x, int y) {
        super(id, x, y);
    }

    public FilterNode(int id, String name, int x, int y) {
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

    @Override
    /**
     * 清洗节点的第一个输出端口是默认的数据出口，不可删除
     */
    public List<Integer> getForcedPortId() {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(getNormalOutputPort());
        return result;
    }

    public int getNormalOutputPort() {
        return getInputPortMap().keySet().iterator().next();
    }

}
