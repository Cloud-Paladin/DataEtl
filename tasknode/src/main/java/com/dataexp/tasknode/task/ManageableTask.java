package com.dataexp.tasknode.task;

/**
 * 可管理任务接口,定义所有任务必须实现的方法
 * @author: Bing.Li
 * @create: 2019-01-23 22:24
 */
public interface ManageableTask {

    /**
     * 获取任务当前状态
     * @return 任务状态
     */
    public TaskStatus getStatus();

    /**
     * 任务是否能够停止
     * @return
     */
    public boolean canStop();

    /**
     * 停止任务
     */
    public void stop();

    /**
     * 启动任务
     */
    public  void start();

}
