package com.dataexp.jobengine.operation;

import com.dataexp.common.metadata.FieldType;
import com.dataexp.common.metadata.InnerMsg;

import java.util.*;


/**
 * 各处理组件对应的基础操作类
 * @author: Bing.Li
 * @create: 2019-01-23 14:19
 */
public abstract class BaseOperation{

    /**
     * 该操作对应逻辑图节点编号
     */
    private int nodeId;

    /**
     * 基础组件只有一个输入端口
     */
    private InputConfig inputConfig;

    /**
     * 输出端口配置列表
     * //TODO:是在这里进行排序还是组件的配置已经配置了端口id对应的功能需要确认
     * 保持有序，清洗节点默认第一个端口为正常出口
     */
    private List<OutputConfig> outputConfigList = new ArrayList<>();

    public BaseOperation() {
    }

    public BaseOperation(int nodeId, InputConfig inputConfig, List<OutputConfig> outputConfigList) {
        this.nodeId = nodeId;
        this.inputConfig = inputConfig;
        this.outputConfigList = outputConfigList;
    }

    /**
     * 具体的清洗操作处理函数
     * @param input 输入的消息
     * @return
     */
    public abstract void processMsg(InnerMsg input);

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

    public InputConfig getInputConfig() {
        return inputConfig;
    }

    public void setInputConfig(InputConfig inputConfig) {
        this.inputConfig = inputConfig;
    }

    public List<OutputConfig> getOutputConfigList() {
        return outputConfigList;
    }

    public void setOutputConfigList(List<OutputConfig> outputConfigList) {
        this.outputConfigList = outputConfigList;
    }

    public void addOutputConfig(OutputConfig config) {
        this.outputConfigList.add(config);
    }

    public List<BaseOperation> getNextOperationListById(int outputPortId) {
        for (OutputConfig config : outputConfigList) {
            if (config.getOutputPortId() == outputPortId) {
                return config.getNextOperationList();
            }
        }
        return null;
    }

    public List<BaseOperation> getFirstPortOperationList() {
        OutputConfig first = outputConfigList.get(0);
        if (null != first) {
            return first.getNextOperationList();
        }
        return null;
    }

}
