package com.dataexp.graph.job;

import com.dataexp.jobengine.operation.BaseOperation;
import com.dataexp.jobengine.task.ExceptionSinkTask;
import com.dataexp.jobengine.task.ExceptionSourceTask;
import com.dataexp.jobengine.task.OuterSinkTask;
import com.dataexp.jobengine.task.OuterSourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Job执行拓扑图，由JobGraph和JobConfig生成执行的JobEnv
 * @author: Bing.Li
 * @create: 2019-02-01
 */
public class JobGraph {

     private static final Logger LOG = LoggerFactory.getLogger(JobGraph.class);

     private int jobId;

     /**
      * 中间临时chain，key为输入端口id，value为入口operation
      */
     private Map<Integer, BaseOperation> tmpOperationChain = new HashMap<>();

     /**
      * 最终chain
      */
     private Map<Integer, BaseOperation> finalOperationChain = new HashMap<>();

     private List<OuterSourceTask> outerSourceTaskList = new ArrayList<>();

     private List<OuterSinkTask> outerSinkTaskList = new ArrayList<>();

     private List<ExceptionSinkTask> exceptionSinkTaskList = new ArrayList<>();

     private List<ExceptionSourceTask> exceptionSourceTaskList = new ArrayList<>();


     public JobGraph() {
     }

     public int getJobId() {
          return jobId;
     }

     public void setJobId(int jobId) {
          this.jobId = jobId;
     }

     public Map<Integer, BaseOperation> getTmpOperationChain() {
          return tmpOperationChain;
     }

     public void setTmpOperationChain(Map<Integer, BaseOperation> tmpOperationChain) {
          this.tmpOperationChain = tmpOperationChain;
     }

     public Map<Integer, BaseOperation> getFinalOperationChain() {
          return finalOperationChain;
     }

     public void setFinalOperationChain(Map<Integer, BaseOperation> finalOperationChain) {
          this.finalOperationChain = finalOperationChain;
     }

     public List<OuterSourceTask> getOuterSourceTaskList() {
          return outerSourceTaskList;
     }

     public void setOuterSourceTaskList(List<OuterSourceTask> outerSourceTaskList) {
          this.outerSourceTaskList = outerSourceTaskList;
     }

     public List<OuterSinkTask> getOuterSinkTaskList() {
          return outerSinkTaskList;
     }

     public void setOuterSinkTaskList(List<OuterSinkTask> outerSinkTaskList) {
          this.outerSinkTaskList = outerSinkTaskList;
     }

     public List<ExceptionSinkTask> getExceptionSinkTaskList() {
          return exceptionSinkTaskList;
     }

     public void setExceptionSinkTaskList(List<ExceptionSinkTask> exceptionSinkTaskList) {
          this.exceptionSinkTaskList = exceptionSinkTaskList;
     }

     public List<ExceptionSourceTask> getExceptionSourceTaskList() {
          return exceptionSourceTaskList;
     }

     public void setExceptionSourceTaskList(List<ExceptionSourceTask> exceptionSourceTaskList) {
          this.exceptionSourceTaskList = exceptionSourceTaskList;
     }
}
