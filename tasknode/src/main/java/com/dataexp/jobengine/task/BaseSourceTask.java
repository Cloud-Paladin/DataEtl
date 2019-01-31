package com.dataexp.jobengine.task;

import com.dataexp.common.metadata.InnerMsg;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 基础的sourcetask,输入是kafka的topic，输出是任务内部的消息队列
 * @author: Bing.Li
 * @create: 2019-01-24 14:35
 */
public abstract class BaseSourceTask extends BaseTask{

     private static final Logger LOG = LoggerFactory.getLogger(BaseSourceTask.class);

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

     public BaseSourceTask(int jobId, int rootNodeId, int poolSize, KafkaConsumer sourceTopic, List<ArrayBlockingQueue<InnerMsg>> targetQueueList) {
          super(jobId, rootNodeId, poolSize);
          this.sourceTopic = sourceTopic;
          this.targetQueueList = targetQueueList;
     }

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

     @Override
     public void run() {
          cancle = false;
          while(!cancle) {
               //TODO:从kafka队列批量获取数据
               String fromKafka = "hello";
               InnerMsg input = deSerializeMsg(fromKafka);
               for (ArrayBlockingQueue<InnerMsg> queue : targetQueueList) {
                    try {
                         queue.put(input);
                    } catch (InterruptedException e) {
                         LOG.error("error occcured:", e);;
                    }
               }
          }
     }

     /**
      * 将kafka的消息内容重组成内部消息格式
      * @param content
      * @return
      */
     public abstract InnerMsg deSerializeMsg(String content);

     @Override
     public boolean canStop() {
          return true;
     }

     @Override
     public List<Integer> getInputPortIdList() {
          return new ArrayList<Integer>();
     }
}

