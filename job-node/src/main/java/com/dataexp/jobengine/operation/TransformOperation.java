package com.dataexp.jobengine.operation;

import com.dataexp.common.metadata.FieldType;
import com.dataexp.common.metadata.InnerMsg;

import java.util.ArrayList;
import java.util.List;

/**
 *  所有操作的基础类
 * @author: Bing.Li
 * @create: 2019-01-23 14:19
 */
public class TransformOperation extends BaseOperation{

    /**
     * 格式转换操作执行列表
     * 每条记录代表一个操作，选择第几列，新增一列默认值等
     * 在格式转换组件和合并组件中用到
     */
    private List<String> transList = new ArrayList<>();

    public TransformOperation() {
    }

    public TransformOperation(int nodeId, InputConfig inputConfig, List<OutputConfig> outputConfigList) {
        super(nodeId, inputConfig, outputConfigList);
    }

    public List<String> getTransList() {
        return transList;
    }

    public void setTransList(List<String> transList) {
        this.transList = transList;
    }

    @Override
    public void processMsg(InnerMsg input) {
        transform(input);
        List<BaseOperation> opList = getFirstPortOperationList();
        if (null != opList) {
            for (BaseOperation op : opList) {
                op.processMsg(input);
            }
        }
    }

    /**
     * 格式转换操作
     * @param input
     */
    public void transform(InnerMsg input) {

    }
}
