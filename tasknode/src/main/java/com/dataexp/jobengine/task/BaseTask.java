package com.dataexp.jobengine.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 任务基础类
 * @author: Bing.Li
 * @create: 2019-01-23 22:35
 */
public abstract class BaseTask implements ManageableTask,Runnable{

    private static final Logger LOG = LoggerFactory.getLogger(BaseTask.class);

    /**
     * 任务归属的JobId
     */
    protected int jobId;

    /**
     * 任务归属节点Id，注意VertexTask包含多个node
     * 此处代表其入口的nodeId
     */
    protected int rootNodeId;

    /**
     * 任务所用线程池
     */
    protected ThreadPoolExecutor pool;

    /**
     * 线程池大小
     */
    protected int poolSize;

    /**
     * 当前创建线程的序号
     */
    protected AtomicInteger threadSequence;

    /**
     * 状态控制
     */
    protected boolean cancle = false;

    public BaseTask(int jobId, int rootNodeId, int poolSize) {
        this.jobId = jobId;
        this.rootNodeId = rootNodeId;
        this.poolSize = poolSize;
        threadSequence = new AtomicInteger(0);
        this.pool = new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1), genThreadFactory());
    }

    /**
     * 获取任务自定义的线程工厂
     * @return
     */
    public abstract ThreadFactory genThreadFactory();

    @Override
    public TaskStatus getStatus() {
        if (0 == pool.getActiveCount()) {
            return TaskStatus.STOPPED;
        } else if (pool.getActiveCount() < poolSize) {
            return TaskStatus.STOPPING;
        }
        return TaskStatus.RUNNING;
    }

    @Override
    public void stop() {
        pool.shutdown();
    }

    @Override
    public void start() {
        for(int i=0;i<pool.getCorePoolSize();i++) {
            pool.execute(this);
        }
    }
    @Override
    public void pause() {
        //TODO:各任务自己实现
    }

    public boolean isCancle() {
        return cancle;
    }

    public void setCancle(boolean cancle) {
        this.cancle = cancle;
    }

    @Override
    public void prepareCancle() {
        setCancle(true);
    }

    /**
     * 注意,Task的node多于一个的需要覆盖该方法
     * @return
     */
    @Override
    public List<Integer> getNodeList() {
        List<Integer> result = new ArrayList<>();
        result.add(rootNodeId);
        return result;
    }
}

