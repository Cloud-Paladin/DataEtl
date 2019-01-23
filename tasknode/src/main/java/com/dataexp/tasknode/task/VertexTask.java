package com.dataexp.tasknode.task;

import com.dataexp.common.metadata.FieldType;
import com.dataexp.common.metadata.InnerMsg;
import com.dataexp.tasknode.task.operation.BaseOperation;
import com.dataexp.tasknode.task.operation.FilterOperation;
import com.dataexp.tasknode.task.operation.SinkFunction;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @description: JobVertex对应的任务
 * @author: Bing.Li
 * @create: 2019-01-23 14:17
 **/
public class VertexTask implements Runnable{

    public ArrayBlockingQueue<InnerMsg> testQueue;

    public BaseOperation head;

    public VertexTask() {

    }

    @Override
    public void run() {

        while(true) {

            try {
                InnerMsg msg = testQueue.take();
                head.processMsg(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {

        ArrayBlockingQueue<InnerMsg> source = new ArrayBlockingQueue(100);
        ArrayBlockingQueue<InnerMsg> target = new ArrayBlockingQueue(100);

        TestSource ts = new TestSource();
        ts.testQueue = source;
        Thread t1 = new Thread(ts);
        t1.start();

        VertexTask vt = new VertexTask();
        vt.testQueue = source;
        FilterOperation fo = new FilterOperation(1,1,new ArrayList<FieldType>());
        SinkFunction so = new SinkFunction(target);
        fo.addNextOperation(so);
        vt.head = fo;
        Thread t2 = new Thread(vt);
        t2.start();

        System.out.println("End");
    }

}

class TestSource implements Runnable{

    public ArrayBlockingQueue<InnerMsg> testQueue;
    @Override
    public void run() {
        while(true) {
            InnerMsg im = new InnerMsg();
            im.setMsgContent("hello world");
            try {
                testQueue.put(im);
                System.out.println("insert msg now");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


