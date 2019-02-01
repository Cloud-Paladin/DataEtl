package com.dataexp.jobengine.task;

import java.util.List;

/**
 * 可管理任务接口,定义所有任务必须实现的方法
 * @author: Bing.Li
 * @create: 2019-01-23 22:24
 */
public interface ManageableTask {

    /**
     * 获取任务当前状态
     * @return 任务状态
     */
    public TaskStatus getStatus();

    /**
     * 任务当前是否可以停止
     * @return
     */
    public boolean canStop();

    /**
     * 通知任务准备结束
     */
    public void prepareCancle();

    /**
     * 停止任务
     */
    public void stop();

    /**
     * 启动任务
     */
    public  void start();

    /**
     * 暂停任务
     */
    public void pause();

    /**
     * 返回任务对应的执行节点编号列表
     * @return
     */
    public List<Integer> getNodeList();

    /**
     * 返回任务对应的输入端口序号列表
     * @return
     */
    public List<Integer> getInputPortIdList();

    /**
     * 返回任务对应的输出端口序号列表
     * @return
     */
    public List<Integer> getOutputPortIdList();

    /**
     * 向任务节点注入数据探针容器，获取指定端口的实时
     * 数据采样
     * @param portId 需要采集数据的端口
     * @param container 采样数据容器
     */
    public void pinData(int portId, PinContainer container);

    /**
     * 返回当前任务激活的探针
     * @return
     */
    public List<Integer> pinPortIdList();

    /**
     * 释放指定端口的数据探针
     * @param portId
     */
    public void releasePin(int portId);

    /**
     * 通知job释放所有的pin探针
     */
    public void clearPin();
}
