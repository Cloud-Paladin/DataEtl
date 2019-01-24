package com.dataexp.tasknode;

/**
 * 定义任务的各种状态
 *
 * @author: Bing.Li
 * @create: 2019-01-24 16:56
 */
public enum JobStatus {
    /**
     * 任务执行状态
     */
    RUNNING,
    /**
     * 任务已停止
     */
    STOPPED,
    /**
     * 任务停止中
     */
    STOPPING,
    /**
     * 任务被暂停
     */
    PAUSED,
    /**
     * 任务初始化完毕，还没开始运行
     */
    READY
}
