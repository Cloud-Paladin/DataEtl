package com.dataexp.tasknode.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * 任务归属节点Id
     */
    protected int nodeId;

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

    public BaseTask(int nodeId, int jobId, int poolSize) {
        this.nodeId = nodeId;
        this.jobId = jobId;
        this.poolSize = poolSize;
        threadSequence = new AtomicInteger(0);
        this.pool = new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1), genThreadFactory());
    }

    /**
     * 获取子任务自定义的线程工厂
     * @return
     */
    public abstract ThreadFactory genThreadFactory();

    /**
     * 通知子任务准备结束
     */
    public abstract void prepareCancle();

    @Override
    public TaskStatus getStatus() {
        if (0 == pool.getActiveCount()) {
            return TaskStatus.STOPPED;
        } else if (pool.getActiveCount() < poolSize) {
            return TaskStatus.PAUSING;
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
}
