package com.dataexp.tasknode.task;


import com.dataexp.common.metadata.InnerMsg;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 任务引擎内部Source任务，把清洗节点的kafka异常数据队列数据导入回清洗节点
 * 内部流转的数据格式和外部接口不一样，序列化的方式也不一样
 * @author: Bing.Li
 * @create: 2019-01-23 14:17
 */
public class ExceptionSourceTask {

    //TODO:注意，在将异常数据从队列取得塞回清洗节点queue时候，要清除掉消息体的Exception信息

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionSourceTask.class);

    /**
     * 数据来源kafka队列
     *
     */
    private KafkaConsumer sourceTopic;

    /**
     * 目标消息队列
     */
    private ArrayBlockingQueue<InnerMsg> targetQueue;

}

