package com.dataexp.tasknode.task.operation;

import com.dataexp.common.metadata.InnerMsg;

/**
 *  所有操作需要实现的接口
 * @author: Bing.Li
 * @create: 2019-01-23 14:12
 */
public interface OperationFunction {
    /**
     * 具体的清洗操作处理函数
     * @param input 输入的消息
     * @return
     */
     void processMsg(InnerMsg input);
}
