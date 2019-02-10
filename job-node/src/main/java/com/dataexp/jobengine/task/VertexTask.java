package com.dataexp.jobengine.task;

import com.dataexp.common.metadata.InnerMsg;
import com.dataexp.jobengine.operation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * JobVertex对应的任务
 * @author: Bing.Li
 * @create: 2019-01-23 14:17
 */
public class VertexTask extends BaseTask{

    private static final Logger LOG = LoggerFactory.getLogger(VertexTask.class);

    /**
     * VertexTask的消息输入队列
     */
    private ArrayBlockingQueue<InnerMsg> soourceQueue;

    /**
     * VertexTask的根操作
     */
    private BaseOperation rootOperation;

    /**
     * 当前任务存在探针列表
     * key：探针端口号
     * value：探针容器
     */
    private Map<Integer, PinContainer> currentPinMap = new HashMap<>();


    public VertexTask(int jobId, int rootNodeId, int poolSize, ArrayBlockingQueue<InnerMsg> soourceQueue, BaseOperation rootOperation) {
        super(jobId, rootNodeId, poolSize);
        this.soourceQueue = soourceQueue;
        this.rootOperation = rootOperation;
    }

    @Override
    public ThreadFactory genThreadFactory() {
        ThreadFactory result = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("VertexTask rootNodeId:" + rootOperation.getNodeId() +" portId:"+ rootOperation.getInputPortId()+" sequence:" + threadSequence.incrementAndGet());
                return t;
            }
        };
        return result;
    }

    public ArrayBlockingQueue<InnerMsg> getSoourceQueue() {
        return soourceQueue;
    }

    public void setSoourceQueue(ArrayBlockingQueue<InnerMsg> soourceQueue) {
        this.soourceQueue = soourceQueue;
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

    @Override
    public void run() {

        while(!cancle) {
            try {
                InnerMsg msg = soourceQueue.take();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("get message:{}",msg.getMsgContent());
                }
                rootOperation.processMsg(msg);
            } catch (InterruptedException e) {
               LOG.error("error occcured:", e);;
            }
        }
    }

    @Override
    public boolean canStop() {
        return soourceQueue.isEmpty();
    }

    @Override
    public List<Integer> getInputPortIdList() {
        return null;
    }

    @Override
    public List<Integer> getOutputPortIdList() {
        return null;
    }

    @Override
    public void pinData(int portId, PinContainer container) {

    }

    @Override
    public List<Integer> pinPortIdList() {
        List<Integer> result = new ArrayList<>();
        for (int key : currentPinMap.keySet()) {
            result.add(key);
        }
        return result;
    }

    @Override
    public void releasePin(int portId) {
        if (currentPinMap.containsKey(portId)) {
            //TODO:通知组件停止端口探针

        }
    }

    @Override
    public void clearPin() {
        for (int portId : currentPinMap.keySet()) {
            releasePin(portId);
        }
    }

    static class TestSource implements Runnable{

        public ArrayBlockingQueue<InnerMsg> testQueue;
        @Override
        public void run() {
            int i=0;
            while(true) {
                InnerMsg im = new InnerMsg();
                im.setMsgContent(++i+":hello world");
                try {
                    testQueue.put(im);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
//        ArrayBlockingQueue<InnerMsg> source = new ArrayBlockingQueue(100);
//        ArrayBlockingQueue<InnerMsg> target = new ArrayBlockingQueue(100);
//
//        TestSource ts = new TestSource();
//        ts.testQueue = source;
//        Thread t1 = new Thread(ts);
//        t1.start();
//
//        SinkFunction so = new SinkFunction(target);
//        List<OperationFunction> ol = new ArrayList<>();
//        ol.add(so);
//        OutputConfig oc = new OutputConfig(2,ol,new ArrayList<>());
//        FilterOperation fo = new FilterOperation(1,1,new ArrayList<>(),oc);
//        VertexTask vt = new VertexTask(1,1,4,source, fo);
//        OuterSinkTask osk = new OuterSinkTask(1,2,2,target,null,3);
//        vt.start();
//        osk.start();
//
//        try {
//            Thread.sleep(10000);
//            osk.pinData(3, new PinContainer() {
//                @Override
//                public void collect(String data) {
//                    System.out.println("PIN Data:"+data);
//                }
//            });
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("End");
    }
}

