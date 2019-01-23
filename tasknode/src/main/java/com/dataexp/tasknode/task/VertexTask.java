package com.dataexp.tasknode.task;

import com.dataexp.common.metadata.FieldType;
import com.dataexp.common.metadata.InnerMsg;
import com.dataexp.tasknode.task.operation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * JobVertex对应的任务
 * @author: Bing.Li
 * @create: 2019-01-23 14:17
 */
public class VertexTask implements Runnable{

    private static final Logger LOG = LoggerFactory.getLogger(VertexTask.class);

    /**
     * VertexTask的消息输入队列
     */
    private ArrayBlockingQueue<InnerMsg> testQueue;

    /**
     * VertexTask的根操作
     */
    private BaseOperation rootOperation;

    /**
     * 任务所用线程池
     */
    private ThreadPoolExecutor pool;

    /**
     * 线程池大小
     */
    private int poolSize;

    /**
     * 当前创建线程的序号
     */
    private AtomicInteger threadSequence;

    public VertexTask(ArrayBlockingQueue<InnerMsg> testQueue, BaseOperation rootOperation, int poolSize) {
        this.testQueue = testQueue;
        this.rootOperation = rootOperation;
        this.poolSize = poolSize;
        threadSequence = new AtomicInteger(0);
        this.pool = new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("JobVertex nodeId:" + rootOperation.getNodeId() +" portId:"+ rootOperation.getInputPortId()+" sequence:" + threadSequence.incrementAndGet());
                return t;
            }
        });
    }

    public ArrayBlockingQueue<InnerMsg> getTestQueue() {
        return testQueue;
    }

    public void setTestQueue(ArrayBlockingQueue<InnerMsg> testQueue) {
        this.testQueue = testQueue;
    }

    public BaseOperation getRootOperation() {
        return rootOperation;
    }

    public void setRootOperation(BaseOperation rootOperation) {
        this.rootOperation = rootOperation;
    }

    public ThreadPoolExecutor getPool() {
        return pool;
    }

    /**
     * 启动任务
     */
    public void startJob(){
        for(int i=0;i<pool.getCorePoolSize();i++) {
            pool.execute(this);
        }
    }

    @Override
    public void run() {

        while(true) {
            try {
                InnerMsg msg = testQueue.take();
                System.out.println(Thread.currentThread().getName()+":" + msg);
                rootOperation.processMsg(msg);
            } catch (InterruptedException e) {
               LOG.error(e.getStackTrace().toString());
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



        SinkFunction so = new SinkFunction(target);
        List<OperationFunction> ol = new ArrayList<>();
        ol.add(so);
        OutputConfig oc = new OutputConfig(2,ol,new ArrayList<>());
        FilterOperation fo = new FilterOperation(1,1,new ArrayList<>(),oc);
        fo.addNextOperation(so);
        VertexTask vt = new VertexTask(source, fo, 4);
//        Thread t2 = new Thread(vt);
//        t2.start();
        vt.startJob();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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


