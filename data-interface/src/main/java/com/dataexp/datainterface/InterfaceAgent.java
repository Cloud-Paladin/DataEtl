package com.dataexp.datainterface;

import com.dataexp.datainterface.task.input.InputTask;
import com.dataexp.datainterface.task.output.OutputTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 实现datainterface和外界的通信
 * @author: Bing.Li
 * @create: 2019-02-01
 */
public class InterfaceAgent {
     private static final Logger LOG = LoggerFactory.getLogger(InterfaceAgent.class);

     private Map<Integer, Map<Integer, InputTask>> inputTaskMap  = new ConcurrentHashMap<>();
     private Map<Integer, Map<Integer, OutputTask>> outputTaskMap = new ConcurrentHashMap<>();

}
