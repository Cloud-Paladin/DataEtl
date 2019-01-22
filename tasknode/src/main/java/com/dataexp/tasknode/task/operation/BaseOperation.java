package com.dataexp.tasknode.task.operation;

import com.dataexp.common.metadata.FieldType;
import com.dataexp.common.metadata.InnerMsg;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有操作的基础类
 */
public abstract class BaseOperation {

    /**
     * 该操作对应逻辑图节点编号
     */
    private int nodeId;

    /**
     * 该操作对应逻辑图节点的输入端口
     */
    private int inputPortId;

    /**
     * 该操作对应逻辑图的输出端口
     */
    private int outputPortId;

    /**
     * 该操作的后续操作列表
     */
    private List<BaseOperation> nexOperationList = new ArrayList<>();

    /**
     * 操作的输入数据格式
     */
    private List<FieldType> inputType = new ArrayList<>();
    /**
     * 操作的输出数据格式
     */
    private List<FieldType> outputType = new ArrayList<>();

    public BaseOperation() {

    }

    public BaseOperation(int nodeId, int inputPortId, int outputPortId) {
        this.nodeId = nodeId;
        this.inputPortId = inputPortId;
        this.outputPortId = outputPortId;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getInputPortId() {
        return inputPortId;
    }

    public void setInputPortId(int inputPortId) {
        this.inputPortId = inputPortId;
    }

    public int getOutputPortId() {
        return outputPortId;
    }

    public void setOutputPortId(int outputPortId) {
        this.outputPortId = outputPortId;
    }

    public List<BaseOperation> getNexOperationList() {
        return nexOperationList;
    }

    public void setNexOperationList(List<BaseOperation> nexOperationList) {
        this.nexOperationList = nexOperationList;
    }

    public void addNextOperation(BaseOperation operation) {
        getNexOperationList().add(operation);
    }

    /**
     * 具体的清洗操作处理函数
     * @param input
     * @return
     */
    public abstract  void processMsg(InnerMsg input);

    /**
     *  获取操作的下一步操作
     * @return
     */
    public List<BaseOperation> getNextOperation(){
        return nexOperationList;
    }

}
