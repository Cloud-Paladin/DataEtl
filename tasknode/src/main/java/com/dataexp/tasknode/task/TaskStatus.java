package com.dataexp.tasknode.task;

/**
 * task状态
 * @author: Bing.Li
 * @create: 2019-01-23 22:24
 */
public enum TaskStatus {
    /**
     * task停止
     */
    STOPPED,

    /**
     * task运行中
     */
    RUNNING,

    /**
     * task停止中
     */
    STOPPING,
    /**
     * task 暂停
     */
    PAUSED
}
