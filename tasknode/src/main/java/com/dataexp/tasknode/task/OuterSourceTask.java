package com.dataexp.tasknode.task;

import com.dataexp.common.metadata.InnerMsg;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 任务流和外界数据接口
 * 从kafka队列读取数据
 * @author: Bing.Li
 * @create: 2019-01-23 14:17
 */
public class OuterSourceTask implements Runnable{

    private static final Logger LOG = LoggerFactory.getLogger(OuterSourceTask.class);
    /**
     * 数据来源kafka队列
     */
    private KafkaConsumer sourceTopic;

    /**
     * 数据源任务输出任务队列
     */
    private List<ArrayBlockingQueue<InnerMsg>> targetQueueList = new ArrayList<>();

    /**
     * 状态控制
     */
    private boolean cancle = true;

    public KafkaConsumer getSourceTopic() {
        return sourceTopic;
    }

    public void setSourceTopic(KafkaConsumer sourceTopic) {
        this.sourceTopic = sourceTopic;
    }

    public List<ArrayBlockingQueue<InnerMsg>> getTargetQueueList() {
        return targetQueueList;
    }

    public void setTargetQueueList(List<ArrayBlockingQueue<InnerMsg>> targetQueueList) {
        this.targetQueueList = targetQueueList;
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
        while(!cancle) {
            //TODO:从kafka队列批量获取数据，对字符串反序列化(JSON)得到MsgFormat对象
            for (ArrayBlockingQueue<InnerMsg> queue : targetQueueList) {
                try {
                    queue.put(new InnerMsg());
                } catch (InterruptedException e) {
                   LOG.error(e.getStackTrace().toString());
                }
            }
        }
    }
}
