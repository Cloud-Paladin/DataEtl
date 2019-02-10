package com.dataexp.graph.job;

import com.dataexp.graph.logic.*;
import com.dataexp.graph.logic.component.WashNode;
import com.dataexp.jobengine.operation.BaseOperation;
import com.dataexp.jobengine.operation.OutputConfig;
import com.dataexp.jobengine.operation.SinkFunction;
import com.dataexp.jobengine.operation.WashOperation;
import com.dataexp.jobengine.task.ExceptionSinkTask;
import com.dataexp.jobengine.task.ExceptionSourceTask;
import com.dataexp.jobengine.task.OuterSinkTask;
import com.dataexp.jobengine.task.OuterSourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将LoigcGraph 转化为JobGraph
 */
public class JobGraphGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(JobGraphGenerator.class);

    public static JobGraph createJobGraph(LogicGraph logicGraph, int jobId) {
        //清理逻辑图，去掉无效节点和端口
        if (!logicGraph.cleanGraph()) {
            return null;
        }
        /**
         * 1.输入输出接口独立抽出单独的组件，ExecGraph需要,同时生成OuterOurceTask 和 OuterSinkTask
         * 2.遇到WashNode有异常导出到异常队列或从异常队列倒回时创建单独的组件，ExecGraph需要，同时生成ExceptionSourceTask和SinkTask
         * 3.初始化中间链表和最终Vertex链表
         * 4.遍历逻辑图的节点，从任意一个组件出发
         * 遍历组件的输入端口，以输入端口为链的初始化入口生成一个空的链
         * 如果该组件和上游组件不能chain起来，则将当前链加入最终链列表，否则加入中间链列表
         * 将组件内该入口到出口（多个）的处理过程转换成数据流的Operation
         *分支组件内部，组件出端口有多个下游组件时该层有多个叶节点
         *下游组件的入口如果在已解析中间链表则直接将该组件在中间链列表中的chain取出来链上
         *每个树分支的扩张碰到后续操作不能chain上的操作终止
         *
         *
         */

        JobGraph jobGraph = new JobGraph();
        jobGraph.setJobId(jobId);
        for (BaseLogicNode logicNode : logicGraph.getNodeMap().values()) {
            genOperationChain(logicNode,jobGraph);
        }
        return jobGraph;
    }

    public static BaseOperation genOperationChain(BaseLogicNode logicNode,JobGraph jobGraph) {
        if (logicNode instanceof AbstractSinkNode) {
            OuterSinkTask outerSinkTask = genOutSinkTask((AbstractSinkNode) logicNode);
            jobGraph.getOuterSinkTaskList().add(outerSinkTask);
        } else if (logicNode instanceof AbstractSourceNode) {
            OuterSourceTask outerSourceTask = genOutSourceTask((AbstractSourceNode) logicNode);
            jobGraph.getOuterSourceTaskList().add(outerSourceTask);
        } else {
            BaseOperation operation;
            //每个输入端口都会产生一个operation
            for (InputPort port : logicNode.getInputPortMap().values()) {
                if(jobGraph.getTmpOperationChain().containsKey(port.getId())){
                    //该输入端口已经在临时链中生成过
                    continue;
                }
                operation = logicNode.genBaseOperation(port.getId());
                if (logicNode.getChainingStrategy() != ChainingStrategy.ALWAYS) {
                    jobGraph.getFinalOperationChain().put(port.getId(), operation);
                } else {
                    jobGraph.getTmpOperationChain().put(port.getId(), operation);
                }
                //TODO:生成opearion后续操作列表
                for (OutputConfig outputConfig : operation.getOutputConfigList()) {
                    int outputPortId = outputConfig.getOutputPortId();
                    OutputPort outputPort = logicNode.getOutputPortById(outputPortId);
                    for (InputPort targetPort : outputPort.getLinkedPortMap().values()) {
                        if (targetPort.getParentNode().getChainingStrategy() == ChainingStrategy.ALWAYS && (
                                logicNode.getChainingStrategy() != ChainingStrategy.NEVER)) {
                            //将后续操作chain起来
                            outputConfig.addNextOperation(genOperationChain(targetPort.getParentNode(), jobGraph));
                        } else {
                            //chain的一个出口，需要标识出口信息
                            SinkFunction sinkFunction = new SinkFunction();
                            sinkFunction.setNodeId(logicNode.getId());
                        }
                    }
                }

                //TODO:operation的输出中获取后续的节点进行判断
                if (operation instanceof WashOperation) {
                    WashOperation washOperation = (WashOperation) operation;
                    //TODO:清洗节点其他异常判断需要提供各类属性判断
                    if (washOperation.getExceptionConfig() == 2) {
                        ExceptionSinkTask exceptionSinkTask = new ExceptionSinkTask(jobGraph.getJobId(), logicNode.getId());
                        jobGraph.getExceptionSinkTaskList().add(exceptionSinkTask);
                    }
                    //TODO:清洗节点异常队列回流选项
                }
            }
        }
        return null;
    }


    public static OuterSinkTask genOutSinkTask(AbstractSinkNode node) {
        return null;
    }

    public static OuterSourceTask genOutSourceTask(AbstractSourceNode node) {
        return null;
    }

    public static ExceptionSinkTask genExceptionSinkTask(WashNode node) {
        return null;
    }

    public static ExceptionSourceTask genExceptionSourceTask(WashNode node) {
        return null;
    }
}
