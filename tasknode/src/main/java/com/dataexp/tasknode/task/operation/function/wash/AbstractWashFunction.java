package com.dataexp.tasknode.task.operation.function.wash;

import com.dataexp.common.metadata.InnerMsg;

/**
 *  清洗节点的清洗函数s
 * @author: Bing.Li
 * @create: 2019-01-23 14:19
 */
public abstract class AbstractWashFunction {

    /**
     * 清洗函数，一次处理一行
     * @param input 输入的内容
     * @return 清洗后的内容，null表示清洗异常
     */
    public abstract void wash(InnerMsg input);

}
