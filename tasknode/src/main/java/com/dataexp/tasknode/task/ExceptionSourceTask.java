package com.dataexp.tasknode.task;


/**
 * @description:  任务引擎内部Source任务，把清洗节点的kafka异常数据队列数据导入回清洗节点
 * 内部流转的数据格式和外部接口不一样，序列化的方式也不一样
 * @author: Bing.Li
 * @create: 2019-01-23 14:17
 **/
public class ExceptionSourceTask {
    //TODO:注意，在将异常数据取得塞回清洗节点队列的时候，要清除掉消息体的Exception信息

}

