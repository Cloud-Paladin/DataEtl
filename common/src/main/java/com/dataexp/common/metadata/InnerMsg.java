package com.dataexp.common.metadata;

import java.time.LocalDateTime;


/**
 * @description: 清洗流程内部流转的基础消息格式
 * @author: Bing.Li
 * @create: 2019-01-23 14:17
 **/
public class InnerMsg {

    /**
     * 消息对应的任务id
     */
    private int jobId;

    /**
     * 消息获取时间
     */
    private LocalDateTime produceTime;

    /**
     * 该消息入口组件节点Id
     */
    private int sourceNodeId;

    /**
     * 当前处理组件Id
     */
    private int currentNodeId;

    /**
     * 清洗刘静路线组件掩码
     */
    private int flowMask;

    /**
     * 异常产生时，如果是清洗节点，清洗模板的版本号
     */
    private int exceptionWashTemplateVersion = -1;

    /**
     *  异常类型
     */
    private ExceptionType exceptionType = ExceptionType.NORMAL;

    /**
     * 消息体
     */
    private String msgContent;

    public InnerMsg(){

    }

    public InnerMsg(int jobId, LocalDateTime produceTime, int sourceNodeId, int currentNodeId, int flowMask, String msgContent) {
        this.jobId = jobId;
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

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
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


    public int getExceptionWashTemplateVersion() {
        return exceptionWashTemplateVersion;
    }

    public void setExceptionWashTemplateVersion(int exceptionWashTemplateVersion) {
        this.exceptionWashTemplateVersion = exceptionWashTemplateVersion;
    }


    public ExceptionType getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    /**
     * 清空该消息的异常信息
     */
    public void clearException() {
        setExceptionWashTemplateVersion(-1);
        setExceptionType(ExceptionType.NORMAL);
    }

}
