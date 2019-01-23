package com.dataexp.tasknode.task.operation;

import com.dataexp.common.metadata.FieldType;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 操作的输出配置
 * @author: Bing.Li
 * @create: 2019-01-23 16:53
 **/
public class OutputConfig {

    /**
     * 输出端口
     */
    private int outputPortId;

    /**
     * 输出端口对应的后续操作列表
     */
    private List<OperationFunction> nextOperationList = new ArrayList<>();

    /**
     * 输出端口的输出数据格式
     */
    private List<FieldType> outputType = new ArrayList<>();

    public OutputConfig(int outputPortId, List<OperationFunction> nextOperationList, List<FieldType> outputType) {
        this.outputPortId = outputPortId;
        this.nextOperationList = nextOperationList;
        this.outputType = outputType;
    }

    public int getOutputPortId() {
        return outputPortId;
    }

    public void setOutputPortId(int outputPortId) {
        this.outputPortId = outputPortId;
    }

    public List<OperationFunction> getNextOperationList() {
        return nextOperationList;
    }

    public void setNextOperationList(List<OperationFunction> nextOperationList) {
        this.nextOperationList = nextOperationList;
    }

    public void addNextOperation(OperationFunction operationFunction) {
        nextOperationList.add(operationFunction);
    }

    public List<FieldType> getOutputType() {
        return outputType;
    }

    public void setOutputType(List<FieldType> outputType) {
        this.outputType = outputType;
    }
}
