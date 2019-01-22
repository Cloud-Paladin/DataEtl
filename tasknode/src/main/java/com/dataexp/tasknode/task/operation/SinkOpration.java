package com.dataexp.tasknode.task.operation;

import com.dataexp.common.metadata.InnerMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;

/**
 *  JobVertex的消息投递操作，将本JobVertex处理完毕的
 *  消息投递到对应的内部消息队列中
 */
public class SinkOpration extends BaseOperation{

    private static final Logger LOG = LoggerFactory.getLogger(SinkOpration.class);
    /**
     * sink操作目标数据queue
     */
    private ArrayBlockingQueue<InnerMsg> targetQueue;

    public SinkOpration(int nodeId, int inputPortId, int outputPortId, ArrayBlockingQueue<InnerMsg> targetQueue) {
        super(nodeId, inputPortId, outputPortId);
        this.targetQueue = targetQueue;
    }

    @Override
    public void processMsg(InnerMsg input) {
        try {
            targetQueue.put(input);
            System.out.println("get msg:" + input.getMsgContent());
        } catch (InterruptedException e) {
            LOG.error(e.getStackTrace().toString());
        }
    }
}
