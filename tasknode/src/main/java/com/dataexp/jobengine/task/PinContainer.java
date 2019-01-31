package com.dataexp.jobengine.task;

/**
 * 数据探针容器,用于实时向各执行任务收集特定
 * 输入输出端口流经的数据
 * @author: Bing.Li
 * @create: 2019-01-24 09:21
 */
public interface PinContainer {

    /**
     * 数据收集回调函数,注意该函数必须是非阻塞的,
     * 否则会阻塞正在执行的任务线程
     * @param data 收集的数据
     */
    public void collect(String data);
}
