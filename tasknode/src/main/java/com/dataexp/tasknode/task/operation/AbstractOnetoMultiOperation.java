package com.dataexp.tasknode.task.operation;

import com.dataexp.common.metadata.FieldType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 一个输入多个输出的操作
 * @author: Bing.Li
 * @create: 2019-01-23 14:19
 **/
public abstract class AbstractOnetoMultiOperation extends BaseOperation {
//    /**
//     * 该操作对应逻辑图的输出端口
//     */
//    private List<Integer> outputPortIdList = new ArrayList<>();
//
//    /**
//     * 该操作对应后续操作map
//     * key为输出端口号
//     * value为该输出端口对应后续操作list
//     */
//    private Map<Integer, List<OperationFunction>> nextOperationMap = new HashMap<>();
//
//    /**
//     * 操作的输出数据格式
//     * key为输出端口号
//     * value为该端口的输出格式
//     */
//    private Map<Integer, List<FieldType>> outputTypeMap = new HashMap<>();

    /**
     * 输出端口配置列表
     */
    private Map<Integer, OutputConfig> outputConfigMap = new HashMap<>();

    public AbstractOnetoMultiOperation(int nodeId, int inputPortId, List<FieldType> inputType) {
        super(nodeId, inputPortId, inputType);
    }

    public AbstractOnetoMultiOperation(int nodeId, int inputPortId, List<FieldType> inputType, Map<Integer, OutputConfig> outputConfigMap) {
        super(nodeId, inputPortId, inputType);
        this.outputConfigMap = outputConfigMap;
    }

    public Map<Integer,OutputConfig> getOutputConfigMap() {
        return outputConfigMap;
    }

    public void setOutputConfigMap(Map<Integer, OutputConfig> outputConfigMap) {
        this.outputConfigMap = outputConfigMap;
    }

    public List<OperationFunction> getNextOperationListById(int outputPortId) {
        OutputConfig config = outputConfigMap.get(outputPortId);
        if (null != config) {
            return config.getNextOperationList();
        }
        return null;
    }

}
