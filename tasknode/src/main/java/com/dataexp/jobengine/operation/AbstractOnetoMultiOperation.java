package com.dataexp.jobengine.operation;

import com.dataexp.common.metadata.FieldType;

import java.util.*;

/**
 *  一个输入多个输出的操作
 * @author: Bing.Li
 * @create: 2019-01-23 14:19
 */
public abstract class AbstractOnetoMultiOperation extends BaseOperation {
    /**
     * 输出端口配置列表
     * 保持有序，清洗节点默认第一个端口为正常出口
     */
    private SortedMap<Integer, OutputConfig> outputConfigMap = new TreeMap<>();

    public AbstractOnetoMultiOperation(int nodeId, int inputPortId, List<FieldType> inputType, SortedMap<Integer, OutputConfig> outputConfigMap) {
        super(nodeId, inputPortId, inputType);
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
