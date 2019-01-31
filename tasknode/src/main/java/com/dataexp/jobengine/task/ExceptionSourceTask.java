package com.dataexp.jobengine.task;


import com.dataexp.common.metadata.InnerMsg;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;

/**
 * 任务引擎内部Source任务，把清洗节点的kafka异常数据队列数据导入回清洗节点
 * 内部流转的数据格式和外部接口不一样，序列化的方式也不一样
 * @author: Bing.Li
 * @create: 2019-01-23 14:17
 */
public class ExceptionSourceTask extends BaseSourceTask{


    private static final Logger LOG = LoggerFactory.getLogger(ExceptionSourceTask.class);

    public ExceptionSourceTask(int jobId, int rootNodeId, int poolSize, KafkaConsumer sourceTopic, List<ArrayBlockingQueue<InnerMsg>> targetQueueList) {
        super(jobId, rootNodeId, poolSize, sourceTopic, targetQueueList);
    }

    @Override
    public InnerMsg deSerializeMsg(String content) {
        InnerMsg msg = new InnerMsg();
        //注意:在将异常数据从队列取得塞回清洗节点queue时候，要清除掉消息体的Exception信息
        msg.clearException();
        msg.setMsgContent(content);
        return msg;
    }

    @Override
    public ThreadFactory genThreadFactory() {
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("JobId:" + jobId + "ExceptionSourceTask rootNodeId:" + rootNodeId + " sequence:" + threadSequence.incrementAndGet());
                return t;
            }
        };
    }

    @Override
    public List<Integer> getOutputPortIdList() {
        return new ArrayList<Integer>();
    }

    @Override
    public void pinData(int portId, PinContainer container) {
    }

    @Override
    public List<Integer> pinPortIdList() {
        return new ArrayList<Integer>();
    }

    @Override
    public void releasePin(int portId) {
    }

    @Override
    public void clearPin() {
    }
}

