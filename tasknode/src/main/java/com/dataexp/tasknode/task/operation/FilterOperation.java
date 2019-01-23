package com.dataexp.tasknode.task.operation;

import com.dataexp.common.metadata.FieldType;
import com.dataexp.common.metadata.InnerMsg;

import java.util.List;
import java.util.Map;

/**
 * @description: 过滤操作
 * @author: Bing.Li
 * @create: 2019-01-23 14:19
 **/
public class FilterOperation extends AbstractOneToOneOperation {

    /**
     * 过滤组件的过滤表达式
     * key: 字段序号
     * value：该字段过滤条件
     */
    private Map<Integer, String> filterPattern;

    public FilterOperation(int nodeId, int inputPortId, List<FieldType> inputType) {
        super(nodeId, inputPortId, inputType);
    }

    public FilterOperation(int nodeId, int inputPortId, List<FieldType> inputType, int outputPortId, List<OperationFunction> nextOperationList, List<FieldType> outputType) {
        super(nodeId, inputPortId, inputType, outputPortId, nextOperationList, outputType);
    }

    public Map<Integer, String> getFilterPattern() {
        return filterPattern;
    }

    public void setFilterPattern(Map<Integer, String> filterPattern) {
        this.filterPattern = filterPattern;
    }

    @Override
    public void processMsg(InnerMsg input) {
        if (filter(input)) {
            for (OperationFunction op : getNextOperationList()) {
                op.processMsg(input);
            }
        }
    }


    private boolean filter(InnerMsg msg) {

        return "".equals(msg.getMsgContent()) || null == msg.getMsgContent() ? false : msg.getMsgContent().length() > 8;
    }
}
