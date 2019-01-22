package com.dataexp.tasknode.Task.operation;

/**
 * 定义操作的chianing策略
 */
public enum ChainingStrategy {

    /**
     * 上下游都可以Chain
      */
    ALWAYS,

    /**
     * 上下游都不能Chain
     */
    NEVER,

    /**
     * 可以连接下游
     */
    HDAD
}
