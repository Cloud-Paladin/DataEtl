package com.dataexp.jobengine;

import com.dataexp.jobengine.task.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 任务执行环境,管理维护Job的具体task生命周期和状态维护
 * TODO:由于用户前台操作可能产生任务状态切换混乱，也有可能多人同时对一个任务状态进行控制
 * 需要配置任务状态转换表
 * @author: Bing.Li
 * @create: 2019-01-23 21:13
 */
public class JobEnv {

    private static final Logger LOG = LoggerFactory.getLogger(JobEnv.class);

    private List<VertexTask> vertexTaskList = new ArrayList<>();

    private List<OuterSourceTask> outerSourceTaskList = new ArrayList<>();

    private List<OuterSinkTask> outerSinkTaskList = new ArrayList<>();

    private List<ExceptionSinkTask> exceptionSinkTaskList = new ArrayList<>();

    private List<ExceptionSourceTask> exceptionSourceTaskList = new ArrayList<>();

    /**
     * 该job所有task列表
     */
    private List<ManageableTask> taskList = new ArrayList<>();

    /**
     * 该job所有节点id列表
     */
    private List<Integer> nodeIdList = new ArrayList<>();

    /**
     * 该job所有端口id列表
     */
    private List<Integer> portIdList = new ArrayList<>();

    private int jobId;

    private int jobVersion;

    private JobConfig config;

    private JobStatus status;

    public JobEnv() {
    }

    public JobStatus getStatus() {
        return status;
    }

    public int getJobId() {
        return jobId;
    }

    public int getJobVersion() {
        return jobVersion;
    }

    public JobConfig getConfig() {
        return config;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    /**
     * 启动job
     * @return
     */
    public boolean start() {
        for (ManageableTask task : taskList) {
            if (task.getStatus() == TaskStatus.STOPPED || task.getStatus() == TaskStatus.PAUSED ) {
                task.start();
            }
        }
        return true;
    }

    /**
     * 停止job
     *
     * @return
     */
    public boolean stop() {
        //TODO:先停止Source任务,再查看sink任务和vertex任务是否能够被停止,能够停止再停止
        //注意异步完成Future

        return false;
    }

    /**
     * 暂停job
     * @return
     */
    public boolean pause() {
        for (ManageableTask task : taskList) {
            if (task.getStatus() == TaskStatus.RUNNING) {
                task.pause();
            }
        }
        return true;
    }

    /**
     * 返回job当前运行状态
     * @return
     */
    public JobStatus getJobStatus() {
        return status;
    }

    /**
     * 对指定端口开启实时数据探针
     *
     * @param portId
     * @param capacity
     * @param duration
     * @param key
     * @return
     */
    public boolean pinData(int portId, int capacity, int duration, String key) {
        if (!portIdList.contains(portId)) {
            return false;
        }
        QueuePinContainer qc = new QueuePinContainer(capacity);
        for (ManageableTask task : taskList) {
            if (task.getInputPortIdList().contains(portId) || task.getOutputPortIdList().contains(portId)) {
                task.pinData(portId, qc);
            }
            //到达规定时间清空探针
            PinMonitor pm = new PinMonitor(portId, duration);
            pm.startMonitor();
        }
        return true;
    }

    /**
     * 释放指定端口的数据探针
     *
     * @param portId
     */
    public void releasePin(int portId) {
        if (!portIdList.contains(portId)) {
            return;
        }
        for (ManageableTask task : taskList) {
            if (task.getInputPortIdList().contains(portId) || task.getOutputPortIdList().contains(portId)) {
                task.releasePin(portId);
            }
        }
    }

    /**
     * 数据探针监控线程
     */
    public class PinMonitor implements Runnable {

        private int portId;
        private int duration;

        public PinMonitor(int portId, int duration) {
            this.portId = portId;
            this.duration = duration;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(duration * 1000);
                releasePin(portId);
            } catch (InterruptedException e) {
                LOG.error("error occurred in PinMonitor:" + e.getStackTrace().toString());
            }
        }

        /**
         * 启动监控
         */
        public void startMonitor() {
            Thread monitor = new Thread(this);
            monitor.start();
        }
    }


    /**
     * 基于阻塞队列的数据探针容器实现
     */
    public static class QueuePinContainer implements PinContainer {

        private int queueCapacity = 1;
        private ArrayBlockingQueue<String> queue;
        private boolean isFull;

        public QueuePinContainer(int queueCapacity) {
            this.queueCapacity = queueCapacity;
            queue = new ArrayBlockingQueue(queueCapacity);
            isFull = false;
        }

        @Override
        public void collect(String data) {
            /**
             * 抽样数据允许丢弃,只要队列被塞满即可
             */
            if (!isFull) {
                if (!queue.offer(data)) {
                    isFull = true;
                }
            }
        }
    }

}
