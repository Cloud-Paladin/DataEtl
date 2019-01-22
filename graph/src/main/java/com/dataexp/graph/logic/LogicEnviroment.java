package com.dataexp.graph.logic;

import com.alibaba.fastjson.JSONObject;
import com.dataexp.common.metadata.BaseType;
import com.dataexp.common.metadata.FieldType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//和web前台进行交互,维护逻辑流程图的生命周期和环境
public class LogicEnviroment {

    private static final Logger LOG = LoggerFactory.getLogger(LogicEnviroment.class);

    //所有正在编辑的逻辑图，key为会话id
    private static final Map<String, LogicGraph> GRAPH_MAP = new ConcurrentHashMap<>();


    public static String genSessionId(int jobId) {
        return jobId + "-" + LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8));
    }

    /**
     * //TODO:初始化一个流程编辑,如果流程未初始化则初始化一个，如果
     * 是已有流程从数据库反序列化流程并将结构返回，同时生成
     * 会话id
     *
     * @return
     */
    public static String initSession(int jobId) {
        String content = getJobSerialContent(jobId);
        LogicGraph lg;
        if ("".equals(content)) {
            lg = createGraph();
        } else {
            lg = deSerializeGraph(content);
        }

        for (int nodeId : lg.getNodeMap().keySet()) {
            LogicNode node = lg.getNodeMap().get(nodeId);
            //TODO：递归遍历节点，端口，连线，并构造为新生成格式提交给前台
            node.getInputPortMap();
            node.getOutputPortMap();

        }
        String sessionId = genSessionId(jobId);
        GRAPH_MAP.put(sessionId, lg);
        return sessionId;
    }

    public static LogicGraph getSessionLogicGraph(String sessionId) {
        return GRAPH_MAP.get(sessionId);
    }

    //TODO:系统初始化时读取所有job图生成graphMap

    //TODO:初始化数据字典传递给前端
    public static String getDictionary() {
        return "";
    }


    //TODO:初始化生成一个只带初始入口节点的逻辑流程
    public static LogicGraph createGraph() {
        LogicGraph lg = new LogicGraph();
//        lg.createNode("FileSource", 50, 50);
        return lg;
    }


    //TODO:序列化当前LogicGraph
    public static String serializeGraph(LogicGraph lg) {
        //保存节点
        //保存端口
        //保存连线？
        //保存节点，端口，连线的最大id
        return "";
    }

    //TODO:反序列化生成LogicGraph
    public static LogicGraph deSerializeGraph(String content) {
        LogicGraph lg = new LogicGraph();
        return lg;
    }

    //TODO：数据库中根据jobid查找逻辑图序列化内容
    public static String getJobSerialContent(int jobId) {
        return "";
    }


    public static String createNode(String sessionId, String type, int sourceNodeId, int x, int y) {
        LogicGraph lg = getSessionLogicGraph(sessionId);
        if (lg != null) {
            LogicNode node = lg.createNode(type, x, y);
            //TODO:获取添加节点涉及的节点和端口
            return node.toString();
        }

        //TODO:sourceNodeId的使用
        return "";
    }

    public static String removeNode(String sessionId, int nodeId) {
        LogicGraph lg = getSessionLogicGraph(sessionId);
        if (lg != null) {
            if (lg.removeNode(nodeId) != null) {
                return "节点删除成功";
            }
            //TODO:遍历节点的端口和边，构造输出
            return "没有该节点";
        }
        return "";
    }

    //TODO:更新节点配置
    public static String updateNode(String sessionId, int nodeId, String config) {
        return "";
    }

    //TODO:添加输入端口
    public static String createInputPort(String sessionId, int nodeId) {
        LogicGraph lg = getSessionLogicGraph(sessionId);
        if (lg != null) {
            InputPort ip = lg.createInputPort(nodeId);
            if (null == ip) {
                return "输入接口已到达上限";
            }
            return ip.toString();
        }
        return "流程图加载错误";
    }

    //TODO:添加输出端口
    public static String createOutputPort(String sessionId, int nodeId) {
        LogicGraph lg = getSessionLogicGraph(sessionId);
        if (lg != null) {
            OutputPort ip = lg.createOutputPort(nodeId);
            if (null == ip) {
                return "输出接口已到达上限";
            }
            return ip.toString();
        }
        return "流程图加载错误";
    }

    //TODO:删除输入端口
    public static String removeInputPort(String sessionId, int nodeId, int portId) {
        LogicGraph lg = getSessionLogicGraph(sessionId);
        if (lg != null) {
            Boolean result = lg.removeInputPort(nodeId, portId);
            return result.toString();
        }
        return "";
    }

    //TODO:删除输出端口
    public static String removeOutputPort(String sessionId, int nodeId) {
        return "";
    }

    //TODO:端口重命名
    public static String renamePort(String sessionId, int nodId, String name) {
        return "";
    }

    //TODO:添加连线
    public static String createEdge(String sessionId, int outputPortId, int inputPortId) {
        LogicGraph lg = getSessionLogicGraph(sessionId);
        if (lg != null) {
            lg.createEdge(outputPortId, inputPortId);
        }
        return "";
    }

    //TODO:删除连线
    public static String removeEdge(String sessionId, int edgeId) {
        return "";
    }

    public static void main(String[] args) {
        int jobId = 1;
        String sessionId = initSession(jobId);
        createNode(sessionId, "FileSource",-1, 50, 50);
        createNode(sessionId, "FileSink", -1, 5, 5);
        createNode(sessionId, "WashNode", -1, 5, 5);
//      createNode(sessionId, "FilterNode", -1, 5, 5);
//      createNode(sessionId, "FormatterNode", -1, 5, 5);
//      createNode(sessionId, "SplitNode", -1, 5, 5);
//      createNode(sessionId, "UnionNode", -1, 5, 5);
        LogicGraph lg = createGraph();
        LogicNode n1 = lg.createNode("FileSource", 10,10);
        LogicNode n2 = lg.createNode("FileSink", 10,10);
        LogicNode n3 = lg.createNode("WashNode", 10,10);
        lg.createOutputPort(n3.getId());
        List<FieldType> ls = new ArrayList<>();
        ls.add(new FieldType(BaseType.NUMBER, "String", "age"));
        n1.getOutputPortMap().values().iterator().next().setPortDataFormat(ls);
        lg.createEdge(n1.getOutputPortMap().keySet().iterator().next(), n3.getInputPortMap().keySet().iterator().next());
        lg.createEdge(n3.getOutputPortMap().keySet().iterator().next(), n2.getInputPortMap().keySet().iterator().next());
        System.out.println("Well donw!");
    }
}
