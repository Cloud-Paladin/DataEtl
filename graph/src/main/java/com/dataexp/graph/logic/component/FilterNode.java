package com.dataexp.graph.logic.component;

import com.dataexp.graph.logic.BaseLogicNode;
import com.dataexp.graph.logic.ChainingStrategy;
import com.dataexp.jobengine.operation.BaseOperation;
import com.dataexp.jobengine.operation.FilterOperation;

import java.util.List;
import java.util.Map;

public class FilterNode extends BaseLogicNode {
    public FilterNode(int id, int x, int y) {
        super(id, x, y);
    }

    public FilterNode(int id, String name, int x, int y) {
        super(id, name, x, y);
    }

    @Override
    public String genNodeConfig() {
        return null;
    }

    @Override
    public void initNodeConfig(String config) {

    }

    /**
     * //TODO:获取该过滤组件节点的过滤参数列表
     *
     * @return
     */
    public Map<Integer, String> genFilterPattern() {
        return null;
    }

    @Override
    public BaseOperation genBaseOperation(int inputPortId) {
        BaseOperation operation = new FilterOperation();
        setOperationBaseAttr(operation,inputPortId);
        ((FilterOperation) operation).setFilterPattern(genFilterPattern());
        return operation;
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
