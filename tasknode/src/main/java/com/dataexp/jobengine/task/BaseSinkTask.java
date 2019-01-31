package com.dataexp.jobengine.task;

import com.dataexp.common.metadata.InnerMsg;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 基础的sinktask，输入是任务内部消息队列，输出时kafka的topic
 *
 * @author: Bing.Li
 * @create: 2019-01-24 11:02
 */
public abstract class BaseSinkTask extends BaseTask {

    private static final Logger LOG = LoggerFactory.getLogger(BaseSinkTask.class);

    /**
     * 内部消息队列
     */
    protected ArrayBlockingQueue<InnerMsg> sourceQueue;

    /**
     * 数据消费kafka队列
     */
    protected KafkaProducer targetTopic;

    /**
     * 状态控制
     */
    protected boolean cancle = true;

    public BaseSinkTask(int jobId, int nodeId, int poolSize, ArrayBlockingQueue<InnerMsg> sourceQueue, KafkaProducer targetTopic) {
        super(jobId, nodeId, poolSize);
        this.sourceQueue = sourceQueue;
        this.targetTopic = targetTopic;
    }

    public KafkaProducer getTargetTopic() {
        return targetTopic;
    }

    public void setTargetTopic(KafkaProducer targetTopic) {
        this.targetTopic = targetTopic;
    }

    public ArrayBlockingQueue<InnerMsg> getSourceQueue() {
        return sourceQueue;
    }

    public void setSourceQueue(ArrayBlockingQueue<InnerMsg> sourceQueue) {
        this.sourceQueue = sourceQueue;
    }

    @Override
    public boolean canStop() {
        return sourceQueue.isEmpty();
    }

    @Override
    public void run() {
        cancle = false;
        InnerMsg input;
        String output;
        while (!cancle) {
            try {
                input = sourceQueue.poll(1, TimeUnit.SECONDS);
                //TODO: 将消息批量内容序列化（JSON）到kafka中
                if (null != input) {
                    output = serializeMsg(input);
                }
            } catch (InterruptedException e) {
                LOG.error("error occcured:", e);;
            }
        }
    }

    public void runtest() {
        cancle = false;
        InnerMsg input;
        String output;
        while (getStatus()!=TaskStatus.RUNNING) {
            try {
                input = sourceQueue.poll(1, TimeUnit.SECONDS);
                //TODO: 将消息批量内容序列化（JSON）到kafka中
                if (null != input) {
                    output = serializeMsg(input);
                }
            } catch (InterruptedException e) {
                LOG.error("error occcured:", e);;
            }
        }
    }

    /**
     * 序列化消息
     *
     * @param input
     * @return
     */
    public abstract String serializeMsg(InnerMsg input);

    @Override
    public List<Integer> getOutputPortIdList() {
        return new ArrayList<Integer>();
    }
}
