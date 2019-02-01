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
 * 任务引擎内部Sink任务，将清洗节点的异常数据导出到
 * kafka的异常消息队列，由于内部流转的数据格式和外部接口不一样，序列化的方式也不一样
 * @author: Bing.Li
 * @create: 2019-01-23 14:17
 */
public class ExceptionSinkTask extends BaseSinkTask{

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionSinkTask.class);

    public ExceptionSinkTask() {
        super();
    }

    public ExceptionSinkTask(int JobId, int rootNodeId) {
        super(JobId, rootNodeId);
    }

    public ExceptionSinkTask(int jobId, int rootNodeId, int poolSize) {
        super(jobId, rootNodeId, poolSize);
    }

    public ExceptionSinkTask(int jobId, int nodeId, int poolSize, ArrayBlockingQueue<InnerMsg> sourceQueue, KafkaProducer targetTopic) {
        super(jobId, nodeId, poolSize, sourceQueue, targetTopic);
    }

    @Override
    public ThreadFactory genThreadFactory() {
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("JobId:" + jobId + "ExceptionSinkTask rootNodeId:" + rootNodeId + " sequence:" + threadSequence.incrementAndGet());
                return t;
            }
        };
    }

    @Override
    public String serializeMsg(InnerMsg input) {
        //TODO:将innerMsg格式化为JSON字符串
        return input.getMsgContent();
    }

    @Override
    public List<Integer> getInputPortIdList() {
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

    public static void main(String[] args) throws InterruptedException {
        ExceptionSinkTask est = new ExceptionSinkTask(1,1,4,new ArrayBlockingQueue<InnerMsg>(10),null);
        est.start();
        System.out.println(est.getStatus());
        Thread.sleep(2000);
        System.out.println(est.canStop());
        est.prepareCancle();
        Thread.sleep(500);
        System.out.println(est.getStatus());
        Thread.sleep(500);
        System.out.println(est.getStatus());
        est.stop();
        System.out.println(est.pool.isTerminated());
        Thread.sleep(500);
        System.out.println(est.getStatus());
        Thread.sleep(500);
        System.out.println(est.getStatus());
        Thread.sleep(500);
        System.out.println(est.getStatus());
    }
}
