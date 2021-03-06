package com.dataexp.jobengine.operation;

import com.dataexp.common.metadata.FieldType;
import com.dataexp.common.metadata.InnerMsg;
import com.dataexp.jobengine.function.wash.AbstractWashFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

/**
 *  清洗操作
 * @author: Bing.Li
 * @create: 2019-01-23 14:19
 */
public class WashOperation extends BaseOperation{

    /**
     * 当前清洗模板的版本号
     */
    private int version = 1;

    /**
     * 总体异常配置
     * 0：丢弃
     * 1：异常队列（不从端口出去，直接通过该节点的内部异常队列对接该节点的InternalSinkTask
     * 2：指定端口出去
     * 3：查看每个异常类型的具体配置
     *
     */
    private int exceptionConfig = 0;
    private int exceptionOutPort = 0;

    /**
     * 格式异常配置
     * 0: 丢弃
     * 1： 异常队列（不从端口出去，直接通过该节点的内部异常队列对接该节点的InternalSinkTask
     * 2：指定端口出去
     */
    private int formatException = 0;
    private int formatExceptionOutPort = 0;

    /**
     * 执行异常配置
     * 0: 丢弃
     * 1： 异常队列（不从端口出去，直接通过该节点的内部异常队列对接该节点的InternalSinkTask
     * 2：指定端口出去
     */
    private int execException  = 0;
    private int execExceptionOutPort = 0;

    /**
     * 检查异常配置
     * 0: 丢弃
     * 1： 异常队列（不从端口出去，直接通过该节点的内部异常队列对接该节点的InternalSinkTask
     * 2：指定端口出去
     */
    private int checkException  = 0;
    private int checkExceptionOutPort = 0;

    /**
     * 当前清洗节点绑定的默认输出sink，将异常数据输出到该节点的异常队列
     */
    private SinkFunction exceptionSink;

    /**
     * 清洗函数列表
     */
    private List<AbstractWashFunction> functionList = new ArrayList<>();

    /**
     * 清洗操作的默认正常数据出口
     */
    private int normalOutputPortId;

    /**
     * 清洗操作正常的数据出口后续操作列表
     */
    private List<BaseOperation> normalOperationFuunctionList = new ArrayList<>();

    public WashOperation() {
    }

    public WashOperation(int nodeId, InputConfig inputConfig, List<OutputConfig> outputConfigList) {
        super(nodeId, inputConfig, outputConfigList);
        //第一个输出端口为正常数据输出端口，要求map必须是有序的
        normalOutputPortId = outputConfigList.get(0).getOutputPortId();
        normalOperationFuunctionList = outputConfigList.get(0).getNextOperationList();
    }

    @Override
    public void processMsg(InnerMsg input) {

        String originContent = input.getMsgContent();
        for (AbstractWashFunction wf : functionList) {
             wf.wash(input);
            switch(input.getExceptionType()){
                case NORMAL:
                    for (BaseOperation op : normalOperationFuunctionList) {
                        op.processMsg(input);
                    }
                    break;
                case FORMAT:
                    input.setMsgContent(originContent);
                    processExceptionMsg(formatException, formatExceptionOutPort,input);
                    break;
                case EXEC:
                    input.setMsgContent(originContent);
                    processExceptionMsg(execException, execExceptionOutPort,input);
                    break;
                case CHECK:
                    input.setMsgContent(originContent);
                    processExceptionMsg(checkException, checkExceptionOutPort,input);
                    break;
                 default:
            }
        }
    }

    /**
     * 处理异常消息
     * @param config 异常消息处理的配置
     * @param outputPort 异常输出端口
     * @param msg 出现异常的消息
     */
    public void processExceptionMsg(int config, int outputPort, InnerMsg msg) {
        /**
         * 丢弃消息
         */
        if (0 == exceptionConfig) {
        }
        /**
         * 消息发送到该清洗节点的异常队列
         */
        else if (1 == exceptionConfig) {
            exceptionSink.processMsg(msg);
        }
        /**
         * 消息从指定端口输出
         */
        else if (2 == exceptionConfig) {
            sendExceptionMsgOut(exceptionOutPort, msg);
        }
        /**
         * 由各异常类型自定义消息处理
         */
        else if (3 == exceptionConfig) {
            /**
             * 丢弃消息
             */
            if (0 == config) {
            }
            /**
             * 消息发送到该清洗节点的异常队列
             */
            else if (1 == config) {
                exceptionSink.processMsg(msg);
            }
            /**
             * 消息从指定端口输出
             */
            else if (2 == config) {
                sendExceptionMsgOut(outputPort,msg);
            }
        }
    }

    /**
     * 将异常消息输出到特定端口
     * @param outputPortId
     * @param msg
     */
    public void sendExceptionMsgOut(int outputPortId, InnerMsg msg) {
        List<BaseOperation> opList = getNextOperationListById(outputPortId);
        if (null != opList) {
            msg.clearException();
            for (BaseOperation opf : opList) {
                opf.processMsg(msg);
            }
        }
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getExceptionConfig() {
        return exceptionConfig;
    }

    public void setExceptionConfig(int exceptionConfig) {
        this.exceptionConfig = exceptionConfig;
    }

    public int getExceptionOutPort() {
        return exceptionOutPort;
    }

    public void setExceptionOutPort(int exceptionOutPort) {
        this.exceptionOutPort = exceptionOutPort;
    }

    public int getFormatException() {
        return formatException;
    }

    public void setFormatException(int formatException) {
        this.formatException = formatException;
    }

    public int getFormatExceptionOutPort() {
        return formatExceptionOutPort;
    }

    public void setFormatExceptionOutPort(int formatExceptionOutPort) {
        this.formatExceptionOutPort = formatExceptionOutPort;
    }

    public int getExecException() {
        return execException;
    }

    public void setExecException(int execException) {
        this.execException = execException;
    }

    public int getExecExceptionOutPort() {
        return execExceptionOutPort;
    }

    public void setExecExceptionOutPort(int execExceptionOutPort) {
        this.execExceptionOutPort = execExceptionOutPort;
    }

    public int getCheckException() {
        return checkException;
    }

    public void setCheckException(int checkException) {
        this.checkException = checkException;
    }

    public int getCheckExceptionOutPort() {
        return checkExceptionOutPort;
    }

    public void setCheckExceptionOutPort(int checkExceptionOutPort) {
        this.checkExceptionOutPort = checkExceptionOutPort;
    }

    public SinkFunction getExceptionSink() {
        return exceptionSink;
    }

    public void setExceptionSink(SinkFunction exceptionSink) {
        this.exceptionSink = exceptionSink;
    }

    public List<AbstractWashFunction> getFunctionList() {
        return functionList;
    }

    public void setFunctionList(List<AbstractWashFunction> functionList) {
        this.functionList = functionList;
    }
}
