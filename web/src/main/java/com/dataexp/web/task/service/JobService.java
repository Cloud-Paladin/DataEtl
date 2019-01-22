package com.dataexp.web.task.service;

import com.dataexp.dispatch.DispatchEngine;
import com.dataexp.web.task.entity.FlowJob;
import java.util.ArrayList;
import java.util.List;

public class JobService {

    public List<FlowJob> getJobList(){
       return new ArrayList<FlowJob>();
    }

    public int getJobStatus(int jobId){
        return 0;
    }

    public boolean stopJob(int jobId) {
        return true;
    }

    public boolean startJob(int jobId) {
        if (1 == getJobStatus(jobId)) {
            return false;
        }
        //TODO:远程通信，通知dispatch-engine启动任务
        return DispatchEngine.startJob(jobId);
    }

    public boolean restartJob(int jobId) {
        return true;
    }

}

