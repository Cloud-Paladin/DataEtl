package com.dataexp.tasknode.task.operation;

import com.dataexp.common.metadata.FieldType;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 操作的输入配置
 * @author: Bing.Li
 * @create: 2019-01-23 16:49
 **/
public class InputConfig {
    /**
     * 该操作对应逻辑图节点的输入端口
     */
    private int inputPortId;

    /**
     * 该操作的输入数据格式
     */
    private List<FieldType> inputType = new ArrayList<>();

    public InputConfig(int inputPortId, List<FieldType> inputType) {
        this.inputPortId = inputPortId;
        this.inputType = inputType;
    }

    public int getInputPortId() {
        return inputPortId;
    }

    public void setInputPortId(int inputPortId) {
        this.inputPortId = inputPortId;
    }

    public List<FieldType> getInputType() {
        return inputType;
    }

    public void setInputType(List<FieldType> inputType) {
        this.inputType = inputType;
    }
}
