package com.dataexp.common.metadata;


/**
 *   数据清洗异常类型
 * @author: Bing.Li
 * @create: 2019-01-23 14:17
 */
public enum ExceptionType {

    /**
     * 无异常
     */
    NORMAL,

    /**
     * 格式异常
     */
    FORMAT,

    /**
     * 执行异常
     */
    EXEC,

    /**
     * 校验异常
     */
    CHECK
}
