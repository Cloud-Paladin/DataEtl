package com.dataexp.tasknode.task;

import com.dataexp.common.metadata.InnerMsg;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;

/**
 * 任务流和外界数据接口
 * 从kafka队列读取数据
 * @author: Bing.Li
 * @create: 2019-01-23 14:17
 */
public class OuterSourceTask extends BaseSourceTask{

    private static final Logger LOG = LoggerFactory.getLogger(OuterSourceTask.class);

    /**
     * 输出端口
     */
    private int outputPortId;

    /**
     * 探针正在抽取数据
     */
    private boolean isPinData = false;

    /**
     * 探针
     */
    PinContainer container;

    public OuterSourceTask(int jobId, int rootNodeId, int poolSize, KafkaConsumer sourceTopic, List<ArrayBlockingQueue<InnerMsg>> targetQueueList, int outputPortId, boolean isPinData) {
        super(jobId, rootNodeId, poolSize, sourceTopic, targetQueueList);
        this.outputPortId = outputPortId;
        this.isPinData = isPinData;
    }

    @Override
    public ThreadFactory genThreadFactory() {
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("JobId:" + jobId + "OuterSourceTask rootNodeId:" + rootNodeId + " outputPortId: " + outputPortId + " sequence:" + threadSequence.incrementAndGet());
                return t;
            }
        };
    }

    @Override
    public InnerMsg deSerializeMsg(String content) {
        InnerMsg msg = new InnerMsg();
        msg.setMsgContent(content);
        if (LOG.isDebugEnabled()) {
            LOG.debug("OuterSourceTask get msg:{}", content);
        }
        if (isPinData && null != container) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("OuterSourceTask pin msg:{}", content);
            }
            container.collect(msg.getMsgContent());
        }
        return msg;
    }

    @Override
    public List<Integer> getOutputPortIdList() {
        List<Integer> result = new ArrayList<>();
        result.add(outputPortId);
        return result;
    }

    @Override
    public void pinData(int portId, PinContainer container) {
        if (portId != outputPortId) {
            return;
        }
        isPinData = true;
        this.container = container;
    }

    @Override
    public List<Integer> pinPortIdList() {
        List<Integer> result = new ArrayList<>();
        if (isPinData) {
            result.add(outputPortId);
        }
        return result;
    }

    @Override
    public void releasePin(int portId) {
        if (portId != outputPortId) {
            return;
        }
        clearPin();
    }

    @Override
    public void clearPin() {
        isPinData = false;
        container = null;
    }
}
