package com.dataexp.tasknode;

import com.dataexp.tasknode.task.SinkTask;
import com.dataexp.tasknode.task.SourceTask;
import com.dataexp.tasknode.task.VertexTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:  任务处理节点引擎
 * @author: Bing.Li
 * @create: 2019-01-23 14:17
 **/
public class TaskEngine {

    private Map<Integer, Map<String, List<VertexTask>>> vertexThreadList = new HashMap<>();

    private Map<Integer, Map<String, List<SourceTask>>> sourceTaskList = new HashMap<>();

    private Map<Integer, Map<String, List<SinkTask>>> sinkTaskList = new HashMap<>();


    public static void main(String[] args) {
    }
}
