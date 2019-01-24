package com.dataexp.tasknode.task.operation;

import com.dataexp.common.metadata.FieldType;
import java.util.ArrayList;
import java.util.List;

/**
 *  一个输入一个输出的操作
 * @author: Bing.Li
 * @create: 2019-01-23 14:17
 */
public abstract class AbstractOneToOneOperation extends BaseOperation{

    /**
     * 唯一输出端口的端口配置
     */
    private OutputConfig outputConfig;

    public AbstractOneToOneOperation(int nodeId, int inputPortId, List<FieldType> inputType, OutputConfig outputConfig) {
        super(nodeId, inputPortId, inputType);
        this.outputConfig = outputConfig;
    }

    public int getOutputPortId() {
        return outputConfig.getOutputPortId();
    }

    public void setOutputPortId(int outputPortId) {
        outputConfig.setOutputPortId(outputPortId);
    }

    public List<OperationFunction> getNextOperationList() {
       return outputConfig.getNextOperationList();
    }

    public void setNextOperationList(List<OperationFunction> nextOperationList) {
        outputConfig.setNextOperationList(nextOperationList);
    }

    public void addNextOperation(OperationFunction operationFunction) {
        outputConfig.addNextOperation(operationFunction);
    }

    public List<FieldType> getOutputType() {
        return outputConfig.getOutputType();
    }

    public void setOutputType(List<FieldType> outputType) {
      outputConfig.setOutputType(outputType);
    }
}
