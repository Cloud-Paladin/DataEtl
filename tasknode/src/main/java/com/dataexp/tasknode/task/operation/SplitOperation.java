package com.dataexp.tasknode.task.operation;

import com.dataexp.common.metadata.FieldType;
import com.dataexp.common.metadata.InnerMsg;

import java.util.*;

/**
 *  分支操作
 * @author: Bing.Li
 * @create: 2019-01-23 14:19
 */
public class SplitOperation extends AbstractOnetoMultiOperation {

    /**
     * 分支操作的分支条件列表
     * 注意map需要保持端口号有序，分割操作命中前面端口的条件就不会往下匹配
     * key为出口端口号
     * value为分支条件
     */
    private SortedMap<Integer, String> splitCondition = new TreeMap<>();

    public SplitOperation(int nodeId, int inputPortId, List<FieldType> inputType, SortedMap<Integer, OutputConfig> outputConfigMap, SortedMap<Integer, String> splitCondition) {
        super(nodeId, inputPortId, inputType, outputConfigMap);
        this.splitCondition = splitCondition;
    }

    public SortedMap<Integer, String> getSplitCondition() {
        return splitCondition;
    }

    public void setSplitCondition(SortedMap<Integer, String> splitCondition) {
        this.splitCondition = splitCondition;
    }

    public void addSplitCondition(int outputPortId, String condition) {
        splitCondition.put(outputPortId, condition);
    }

    @Override
    public void processMsg(InnerMsg input) {
        for (int outputPortId : splitCondition.keySet()) {
            if (checkCondition(splitCondition.get(outputPortId), input)) {
                List<OperationFunction> opList = getNextOperationListById(outputPortId);
                if (null != opList) {
                    for (OperationFunction op : opList) {
                        op.processMsg(input);
                        //命中一条分支则返回
                        return;
                    }
                }
           }
        }
    }

    /**
     * 检查消息是否满足条件
     *
     * @param condition 条件表达式
     * @param msg       消息s
     * @return
     */
    private boolean checkCondition(String condition, InnerMsg msg) {
        return true;
    }


}
