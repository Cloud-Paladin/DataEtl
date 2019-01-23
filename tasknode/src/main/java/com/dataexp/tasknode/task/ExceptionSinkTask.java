package com.dataexp.tasknode.task;

import com.dataexp.common.metadata.InnerMsg;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * 任务引擎内部Sink任务，将清洗节点的异常数据导出到
 * kafka的异常消息队列，由于内部流转的数据格式和外部接口不一样，序列化的方式也不一样
 * @author: Bing.Li
 * @create: 2019-01-23 14:17
 */
public class ExceptionSinkTask extends BaseTask implements Runnable{

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionSinkTask.class);

    /**
     * 内部异常队列
     */
    private ArrayBlockingQueue<InnerMsg> sourceQueue;

    /**
     * 数据消费kafka队列
     */
    private KafkaProducer targetTopic;

    /**
     * 状态控制
     */
    private boolean cancle = true;

    public ExceptionSinkTask(int nodeId, int jobId, int poolSize, ArrayBlockingQueue<InnerMsg> sourceQueue, KafkaProducer targetTopic) {
        super(nodeId, jobId, poolSize);
        this.sourceQueue = sourceQueue;
        this.targetTopic = targetTopic;
    }

    @Override
    public ThreadFactory genThreadFactory() {
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("JobId:"+ jobId +"ExceptionSinkTask nodeId:" + nodeId + " sequence:" + threadSequence.incrementAndGet());
                return t;
            }
        };
    }

    @Override
    public void run() {
        cancle = false;
        InnerMsg message;
        while(!cancle) {
            try {
                message = sourceQueue.poll(1, TimeUnit.SECONDS);
                //TODO: 将消息批量内容序列化（JSON）到kafka中
            } catch (InterruptedException e) {
                LOG.error(e.getStackTrace().toString());
            }
        }
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

    public boolean isCancle() {
        return cancle;
    }

    public void setCancle(boolean cancle) {
        this.cancle = cancle;
    }

    @Override
    public void prepareCancle() {
        setCancle(true);
    }

    @Override
    public boolean canStop() {
        return sourceQueue.isEmpty();
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
