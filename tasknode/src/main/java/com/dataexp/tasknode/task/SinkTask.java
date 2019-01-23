package com.dataexp.tasknode.task;

import com.dataexp.common.metadata.InnerMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @description:  任务流和外部的接口
 * 将处理完毕数据写入kafka队列
 * @author: Bing.Li
 * @create: 2019-01-23 14:17
 **/
public class SinkTask implements Runnable{

    private static final Logger LOG = LoggerFactory.getLogger(SinkTask.class);

    /**
     * 数据消费kafka队列名称
     */
    private String targetTopicName;

    /**
     * 数据sink来源队列
     */
    private ArrayBlockingQueue<InnerMsg> sourceQueue;

    /**
     * 状态控制
     */
    private boolean cancle;

    public String getTargetTopicName() {
        return targetTopicName;
    }

    public void setTargetTopicName(String targetTopicName) {
        this.targetTopicName = targetTopicName;
    }

    public ArrayBlockingQueue<InnerMsg> getSourceQueue() {
        return sourceQueue;
    }

    public void setSourceQueue(ArrayBlockingQueue<InnerMsg> sourceQueue) {
        this.sourceQueue = sourceQueue;
    }

    public boolean isCancle() {
        return cancle;
    }

    public void setCancle(boolean cancle) {
        this.cancle = cancle;
    }

    @Override
    public void run() {
        cancle = false;
        InnerMsg message;
        while(!cancle) {
            try {
                message = sourceQueue.take();
                //TODO: 将消息批量内容序列化（JSON）到kafka中
            } catch (InterruptedException e) {
                LOG.error(e.getStackTrace().toString());
            }
        }
    }
}
