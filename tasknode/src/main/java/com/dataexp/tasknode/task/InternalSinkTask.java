package com.dataexp.tasknode.task;

/**
 * 任务引擎内部Sink任务，将清洗节点的异常数据导出到异常队列
 * 读取异常数据写入kafka的异常消息队列
 * 内部流转的数据格式和外部接口不一样，序列化的方式也不一样
 *
 */
public class InternalSinkTask {
}
