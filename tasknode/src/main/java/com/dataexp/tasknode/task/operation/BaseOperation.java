package com.dataexp.tasknode.task.operation;

import com.dataexp.common.metadata.FieldType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description: 所有操作的基础类
 * @author: Bing.Li
 * @create: 2019-01-23 14:19
 **/
public abstract class BaseOperation implements OperationFunction{

    /**
     * 该操作对应逻辑图节点编号
     */
    private int nodeId;

    private InputConfig inputConfig;

    public BaseOperation(int nodeId, int inputPortId, List<FieldType> inputType) {
        this.nodeId = nodeId;
        this.inputConfig = new InputConfig(inputPortId, inputType);
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getInputPortId() {
        return inputConfig.getInputPortId();
    }

    public void setInputPortId(int inputPortId) {
       inputConfig.setInputPortId(inputPortId);
    }

    public List<FieldType> getInputType() {
        return inputConfig.getInputType();
    }

    public void setInputType(List<FieldType> inputType) {
        inputConfig.setInputType(inputType);
    }
}
