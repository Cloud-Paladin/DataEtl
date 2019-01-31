package com.dataexp.jobengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务处理节点引擎
 * 1.根据DispatchEngine的调配管理job的JobVertex任务,Source任务,Sink任务以及执行线程数
 * 2.接收DispatchEngine的任务启停控制
 * 3.返回任务状态信息和统计信息
 *
 * @author: Bing.Li
 * @create: 2019-01-23 14:17
 */
public class JobEngine {

    private static final Logger LOG = LoggerFactory.getLogger(JobEngine.class);

    private Map<Integer, JobEnv> jobEnvMap = new ConcurrentHashMap<>();

    /**
     * 创建一个新的任务,从数据库加载任务流元数据
     * 对应DispatchEngine传递过来的jobconfig
     * 初始化并运行一个job任务
     *
     * @param jobId
     * @param version 该流程配置的版本号，防止加载不一致的版本
     * @param
     * @return
     */
    public synchronized boolean createJob(int jobId, int version, JobConfig config) {
        JobEnv jobenv = jobEnvMap.get(jobId);
        if (null != jobenv) {
            return reloadJob(jobId, version, config);
        } else {
            jobenv = createJobEnv(jobId, version, config);
            if (null == jobenv) {
                return false;
            }
            jobEnvMap.put(jobId,jobenv);
            return jobenv.start();
        }
    }

    /**
     * 根据传入的job配置构造一个JobEnv
     *
     * @param jobId
     * @param version
     * @param config
     * @return
     */
    private static JobEnv createJobEnv(int jobId, int version, JobConfig config) {
        //TODO:创建jobenv流程
        return new JobEnv();
    }

    /**
     * 启动一个任务
     * @param jobId
     * @return
     */
    private boolean startJob(int jobId) {
        JobEnv jobenv = jobEnvMap.get(jobId);
        if (null != jobenv) {
            return jobenv.start();
        }
        return false;
    }

    /**
     * 停止job，注意job的停止是异步的，此处仅仅返回能够停止
     * 真正的停止需要调用方定时查询job的任务状态
     *
     * @param jobId
     * @return true为开始停止, false为停止异常
     */
    public boolean stopJob(int jobId) {
        JobEnv jobenv = jobEnvMap.get(jobId);
        if (null != jobenv) {
            //TODO:添加异步线程查看任务是否真正停止成功
            jobenv.stop();
            return true;
        }
        return false;
    }

    /**
     * 暂停job
     * @param jobId
     * @return
     */
    public boolean pauseJob(int jobId) {
        JobEnv jobenv = jobEnvMap.get(jobId);
        if (null != jobenv) {
            jobenv.pause();
            return true;
        }
        return false;
    }

    /**
     * 返回任务执行状态
     *
     * @param jobId
     * @return
     */
    public JobStatus getJobStatus(int jobId) {
        JobEnv jobenv = jobEnvMap.get(jobId);
        if (null != jobenv) {
            //TODO:enum到整形的映射
            return jobenv.getJobStatus();
        }
        return null;
    }

    /**
     * 删除任务
     *
     * @param jobId
     * @return
     */
    public boolean removeJob(int jobId) {
        JobEnv jobenv = jobEnvMap.get(jobId);
        if (null != jobenv) {
            stopJob(jobId);
            //TODO:添加线程异步监控job是否停止然后才能删除该任务
            jobEnvMap.remove(jobId);
        }
        return false;
    }

    /**
     * 通知节点重新载入任务配置运行，注意需要先停止任务才能载入
     *
     * @param jobId
     * @param version 该job流程定义的版本号，防止加载不一致的版本
     * @return
     */
    public boolean reloadJob(int jobId, int version, JobConfig config) {
        if (jobEnvMap.containsKey(jobId)) {
            removeJob(jobId);
            //TODO:在异步执行结束后重新生成job
            JobEnv env = createJobEnv(jobId, version, config);
        }
        return false;
    }

    /**
     * 实时数据流探针,从当前运行的流图中实时获取指定端口抽样数据
     * @param jobId     任务id
     * @param portId    端口号
     * @param recordNum 期望样本数据量
     * @param duration  最大数据收集时间
     * @param key       样本数据在缓存库中存储的key
     * @return
     */
    public boolean pinData(int jobId, int portId, int recordNum, int duration, String key) {
        JobEnv jobenv = jobEnvMap.get(jobId);
        if (null != jobenv) {
            jobenv.pinData(portId, recordNum, duration, key);
        }
        return false;
    }

}
