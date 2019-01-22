package com.dataexp.tasknode.task;


import com.dataexp.tasknode.task.operation.BaseOperation;
import com.dataexp.tasknode.task.operation.SourceOperation;

import java.util.ArrayList;
import java.util.List;

public class FakeVertex {

    private SourceOperation so;

    private List<BaseOperation> operationList = new ArrayList<>();
   //根JobVertex的信息，初始化处理树Chain上的每一级operation

    public FakeVertex() {

    }

    public SourceOperation getSo() {
        return so;
    }

    public void setSo(SourceOperation so) {
        this.so = so;
    }

    public List<BaseOperation> getOperationList() {
        return operationList;
    }

    public void addOpertion(BaseOperation op) {
        operationList.add(op);
    }
}
