package com.dataexp.tasknode;

import com.dataexp.tasknode.task.OuterSinkTask;
import com.dataexp.tasknode.task.OuterSourceTask;
import com.dataexp.tasknode.task.VertexTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务处理节点引擎
 * 1.根据DispatchEngine的调配管理job的JobVertex任务,Source任务,Sink任务以及执行线程数
 * 2.接收DispatchEngine的任务启停控制
 * 3.返回任务状态信息和统计信息
 * @author: Bing.Li
 * @create: 2019-01-23 14:17
 */
public class TaskEngine {

    private Map<Integer, Map<String, List<VertexTask>>> vertexThreadList = new HashMap<>();

    private Map<Integer, Map<String, List<OuterSourceTask>>> sourceTaskList = new HashMap<>();

    private Map<Integer, Map<String, List<OuterSinkTask>>> sinkTaskList = new HashMap<>();

    public static void main(String[] args) {


    }
}
