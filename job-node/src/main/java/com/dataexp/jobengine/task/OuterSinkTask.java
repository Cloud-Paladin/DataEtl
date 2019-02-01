package com.dataexp.jobengine.task;

import com.dataexp.common.metadata.InnerMsg;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;

/**
 * 任务流和外部的接口
 * 将处理完毕数据写入kafka队列
 *
 * @author: Bing.Li
 * @create: 2019-01-23 14:17
 */
public class OuterSinkTask extends BaseSinkTask {

    private static final Logger LOG = LoggerFactory.getLogger(OuterSinkTask.class);

    /**
     * 输入端口
     */
    private int inputPortId;

    /**
     * 探针正在抽取数据
     */
    private boolean isPinData = false;

    /**
     * 探针容器
     */
    PinContainer container;

    public OuterSinkTask(int inputPortId) {
        super();
        this.inputPortId = inputPortId;
    }

    public OuterSinkTask(int JobId, int rootNodeId, int inputPortId) {
        super(JobId, rootNodeId);
        this.inputPortId = inputPortId;
    }

    public OuterSinkTask(int jobId, int rootNodeId, int poolSize, int inputPortId) {
        super(jobId, rootNodeId, poolSize);
        this.inputPortId = inputPortId;
    }

    public OuterSinkTask(int jobId, int nodeId, int poolSize, ArrayBlockingQueue<InnerMsg> sourceQueue, KafkaProducer targetTopic, int inputPortId) {
        super(jobId, nodeId, poolSize, sourceQueue, targetTopic);
        this.inputPortId = inputPortId;
    }

    @Override
    public ThreadFactory genThreadFactory() {
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("JobId:" + jobId + "OuterSinkTask rootNodeId:" + rootNodeId + " inputPortId: " + inputPortId + " sequence:" + threadSequence.incrementAndGet());
                return t;
            }
        };
    }

    @Override
    public String serializeMsg(InnerMsg input) {
        //TODO:将innerMsg格式的内容取出
        String result = input.getMsgContent();
        if (LOG.isDebugEnabled()) {
            LOG.debug("outersinktask get msg:{}", result);
        }
        if (isPinData && null != container) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("outersinktask pin msg:{}", result);
            }
            container.collect(input.getMsgContent());
        }
        return input.getMsgContent();
    }

    @Override
    public List<Integer> getInputPortIdList() {
        List<Integer> result = new ArrayList<>();
        result.add(inputPortId);
        return result;
    }

    @Override
    public void pinData(int portId, PinContainer container) {
        if (portId != inputPortId) {
            return;
        }
        isPinData = true;
        this.container = container;
    }

    @Override
    public List<Integer> pinPortIdList() {
        List<Integer> result = new ArrayList<>();
        if (isPinData) {
            result.add(inputPortId);
        }
        return result;
    }

    @Override
    public void releasePin(int portId) {
        if (portId != inputPortId) {
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
