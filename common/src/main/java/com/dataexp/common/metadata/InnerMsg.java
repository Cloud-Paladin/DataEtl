package com.dataexp.common.metadata;

import java.time.LocalDateTime;

//清洗流程内部流转的基础消息格式
public class InnerMsg {

    //任务id
    private int taskId;

    //消息获取时间
    private LocalDateTime produceTime;

    //该数据入口组件（jobGraph）节点Id
    private int sourceNodeId;

    //当前处理组件（jobGraph）节节点Id
    private int currentNodeId;

    //清洗流经路线组件掩码
    private int flowMask;

    /**
     * 当出现split-select操作时，记录split操作产生的分支name
     * 用于后续的select选择
     */
    private String selectName;

    //异常定义
    private ExceptionContent exceptionContent;

    //消息体
    private String msgContent;

    public InnerMsg(){

    }

    public InnerMsg(int taskId, LocalDateTime produceTime, int sourceNodeId, int currentNodeId, int flowMask, String msgContent) {
        this.taskId = taskId;
        this.produceTime = produceTime;
        this.sourceNodeId = sourceNodeId;
        this.currentNodeId = currentNodeId;
        this.flowMask = flowMask;
        this.msgContent = msgContent;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public LocalDateTime getProduceTime() {
        return produceTime;
    }

    public void setProduceTime(LocalDateTime produceTime) {
        this.produceTime = produceTime;
    }

    public int getSourceNodeId() {
        return sourceNodeId;
    }

    public void setSourceNodeId(int sourceNodeId) {
        this.sourceNodeId = sourceNodeId;
    }

    public int getCurrentNodeId() {
        return currentNodeId;
    }

    public void setCurrentNodeId(int currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    public int getFlowMask() {
        return flowMask;
    }

    public void setFlowMask(int flowMask) {
        this.flowMask = flowMask;
    }

    public ExceptionContent getExceptionContent() {
        return exceptionContent;
    }

    public void setExceptionContent(ExceptionContent exceptionContent) {
        this.exceptionContent = exceptionContent;
    }


}
