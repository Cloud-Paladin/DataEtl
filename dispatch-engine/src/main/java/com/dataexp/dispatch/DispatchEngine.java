package com.dataexp.dispatch;

import com.dataexp.dispatch.nodeagent.DataInterfaceNodeAgent;
import com.dataexp.dispatch.nodeagent.TaskNodeAgent;
import com.dataexp.graph.job.JobGraph;
import com.dataexp.graph.logic.LogicGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1. 解析LogicGraph获取JobGraph（该工作任务节点也会做，要注意保持版本一致）
 * 2. 分解出其中的Source节点、Sink节点，JobVertex节点
 * 3. 对Source节点和Sink节点分解出数据接口任务（清洗节点的异常数据Source,Sink除外）分配任务给数据接口节点
 * 4. 对Source(任务Source，清洗异常回流Source)节点按照当前的kafka分区数，任务节点数，分配每个任务节点的读取线程数
 * 5. 对Sink节点（任务Sink，清洗异常Sink）按照节点负载分配每个任务节点的消费线程数
 * 6. 对JobVertex节点分配任务节点线程数
 * 7. 各任务任务节点状态监控，每节点运行任务统计数据汇总,各节点任务状态管理（启动，停止，重启）
 * 8. 数据接口节点状态监控，每节点运行任务统计数据汇总
 * 9. 维护各job的消息topic的生命周期，job第一次运行时创建，修改时保持同步（新增，删除），job删除时删除所有
 * @author: Bing.Li
 * @create: 2019-01-23 14:17
 */

public class DispatchEngine {

    private static final Logger LOG = LoggerFactory.getLogger(DispatchEngine.class);
    /**
     * 当前保存的LogicGraph,key为jobId，value为LoigcGraph
     * 在任务下次启动时将从数据库获取最新版的LogicGraph进行更新（可以比较版本号或时间）
     */
    private static Map<Integer, LogicGraph> currentLogicGraphMap = new HashMap<>();

    private static Map<Integer, JobGraph> currentJobGraphMap = new HashMap<>();

    private static Map<Integer, Integer> currentJobStatus = new HashMap<>();

    private static List<TaskNodeAgent> taskNodeList = new ArrayList<>();

    private static List<DataInterfaceNodeAgent> dataInterfaceNodeList = new ArrayList<>();

    public static boolean startJob(int jobId) {
        //TODO:检查LogicGraph的版本是否有更新，如果有要重新更新各节点的配置信息
        for (TaskNodeAgent ena: taskNodeList) {
            if (!ena.startJob(jobId)) {
                return false;
            }
        }
            return true;
    }

    public static boolean stopJob(int jobId) {
        return false;
    }

    public static boolean restartJob(int jobId) {
        return false;
    }

    public static boolean removeJob(int jobId) {
        return false;
    }

    public static int getJobStatus(int jobId) {
        return 0;
    }

    public static String getJobStatisticInfo(int jobId) {
        return "";
    }

    public static void main(String[] args) {

    }
}
