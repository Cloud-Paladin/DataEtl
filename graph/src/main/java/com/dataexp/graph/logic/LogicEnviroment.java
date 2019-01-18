package com.dataexp.graph.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//和web前台进行交互,维护逻辑流程图的生命周期和环境
public class LogicEnviroment {

    private static final Logger LOG = LoggerFactory.getLogger(LogicEnviroment.class);

    //所有正在编辑的逻辑图，key为会话id
    private static final Map<String, LogicGraph> graphMap = new ConcurrentHashMap<>();




    /**
     * //TODO:初始化一个流程编辑,如果流程未初始化则初始化一个，如果
     * 是已有流程从数据库反序列化流程并将结构返回，同时生成
     * 会话id
     * @return
     */
    public static String initSession(int jobId) {
        String content = getJobSerialContent(jobId);
        LogicGraph lg;
        if(content.equals("")) {
            lg = createGraph();
        } else {
            lg = deSerializeGraph(content);
        }
        String sessionId = jobId + "-" + LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8));
        for (int nodeId : lg.getLogicNodes().keySet()) {
            LogicNode node = lg.getLogicNodes().get(nodeId);
            //TODO：递归遍历节点，端口，连线，并构造为新生成格式提交给前台
            node.getInputPorts();
            node.getOutputPorts();

        }

        return "";
    }


    //TODO:初始化生成一个只带初始入口节点的逻辑流程
    public static LogicGraph createGraph() {
        return new LogicGraph();
    }

    //TODO:序列化当前LogicGraph
    public static String serializeGraph(LogicGraph lg) {
        return "";
    }

    //TODO:反序列化生成LogicGraph
    public static LogicGraph  deSerializeGraph(String content) {
        LogicGraph lg = new LogicGraph();
        return lg;
    }

    //TODO：数据库中根据jobid查找逻辑图序列化内容
    public static String getJobSerialContent(int jobId) {
        return "";
    }


}
