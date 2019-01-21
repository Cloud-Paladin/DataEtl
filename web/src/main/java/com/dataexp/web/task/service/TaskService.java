package com.dataexp.web.task.service;

import com.dataexp.web.task.entity.FlowTask;

import java.util.ArrayList;
import java.util.List;

public class TaskService {

    public List<FlowTask> getTaskList(){
       return new ArrayList<FlowTask>();
    }

    public int getTaskStatus(int taskId){
        return 0;
    }

    public boolean stopTask(int taskId) {
        return true;
    }

    public boolean startTask(int taskId) {
        return true;
    }

    public boolean restartTask(int taskId) {
        return true;
    }

}

