package com.dataexp.tasknode.task.operation;

import com.dataexp.common.metadata.InnerMsg;

import java.util.Map;

public class FilterOperation extends BaseOperation {

    /**
     * 过滤组件的过滤表达式
     * key: 字段序号
     * value：该字段过滤条件
     */
    private Map<Integer, String> filterPattern;

    public FilterOperation(int nodeId, int inputPortId, int outputPortId, Map<Integer, String> filterPattern) {
        super(nodeId, inputPortId, outputPortId);
        this.filterPattern = filterPattern;
    }

    @Override
    public void processMsg(InnerMsg input) {
        if (filter(input.getMsgContent())) {
            for (BaseOperation op : getNexOperationList()) {
                op.processMsg(input);
            }
        }
    }

    public boolean filter(String msgContent) {
        return "".equals(msgContent) ? false : msgContent.length() > 8;
    }
}
