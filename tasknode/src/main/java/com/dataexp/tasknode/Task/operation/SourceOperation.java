package com.dataexp.tasknode.Task.operation;

import com.dataexp.common.metadata.InnerMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *  JobVertex的消息获取操作，读取本JobVertex需要处理的消息
 *  提交后续操作进行处理，一个JobVertex只能有一个SourceOperation
 *  作为执行树的root
 */
public class SourceOperation {

    private static final Logger LOG = LoggerFactory.getLogger(SourceOperation.class);

    /**
     * source操作来源数据queue
     */
    private ArrayBlockingQueue<InnerMsg> sourceQueue;

    public SourceOperation(int nodeId, int inputPortId, int outputPortId, ArrayBlockingQueue<InnerMsg> sourceQueue) {
        this.sourceQueue = sourceQueue;
    }

    public InnerMsg processMsg(InnerMsg input) {
        return null;
    }
}
