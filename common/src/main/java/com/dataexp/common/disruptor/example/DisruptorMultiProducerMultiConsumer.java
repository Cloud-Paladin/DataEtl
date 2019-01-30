package com.dataexp.common.disruptor.example;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Bing.Li
 * @create: 2019-01-25
 */
public class DisruptorMultiProducerMultiConsumer {
    public static void main(String[] args) throws Exception {
        //TODO:实现的太麻烦，不知道为什么这样实现
        //1 创建RingBuffer
        RingBuffer<OrderEvent> ringBuffer = RingBuffer.create(
                ProducerType.MULTI,
                new EventFactory<OrderEvent>() {
                    @Override
                    public OrderEvent newInstance() {
                        return new OrderEvent();
                    }
                },
                1024 * 1024,
                new BlockingWaitStrategy());
        //2 通过Ringbuffer创建一个屏障
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        //3 创建多个消费者
        Consumer[] consumers = new Consumer[10];
        for (int i = 0; i < 10; i++) {
            consumers[i] = new Consumer("C" + i);
        }

        //4 构建多消费者工作池
        WorkerPool<OrderEvent> workerPool = new WorkerPool<OrderEvent>(
                ringBuffer,
                sequenceBarrier,
                new EventExceptionHandler(),
                consumers);
        //5 设置多个消费者的sequence序号 用于单独统计消费进度, 并且设置到ringbuffer中
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());

        //6 启动
        ExecutorService es = Executors.newFixedThreadPool(5);
        workerPool.start(es);

        int produceNum = 10;
        final CountDownLatch latch = new CountDownLatch(produceNum);
        //7 生产者产生数据
        for (int i = 0; i < produceNum; i++) {
            new Thread(new Producer(ringBuffer, latch)).start();
        }
        latch.await();
        Thread.sleep(1000);
        System.err.println("任务总数:" + consumers[2].getCount());
    }

    static class EventExceptionHandler implements ExceptionHandler<OrderEvent> {
        public void handleEventException(Throwable ex, long sequence, OrderEvent event) {
        }

        public void handleOnStartException(Throwable ex) {
        }

        public void handleOnShutdownException(Throwable ex) {
        }
    }

}

class OrderEvent {
    private String id;
    private String name;
    private long value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}

class Consumer implements WorkHandler<OrderEvent> {

    private static AtomicInteger count = new AtomicInteger(0);

    private String cid;

    private Random random = new Random();

    public Consumer(String cid) {
        this.cid = cid;
    }

    @Override
    public void onEvent(OrderEvent event) throws Exception {
        Thread.sleep(1 * random.nextInt(5));
        System.err.println("当前消费者: " + this.cid + ", 消费信息ID: " + event.getId());
        count.incrementAndGet();
    }

    public int getCount() {
        return count.get();
    }
}


class Producer implements Runnable {

    private RingBuffer<OrderEvent> ringBuffer;

    private CountDownLatch latch;

    private static AtomicInteger seq = new AtomicInteger(0);

    public Producer(RingBuffer<OrderEvent> ringBuffer, CountDownLatch latch) {
        this.ringBuffer = ringBuffer;
        this.latch = latch;
    }


    @Override
    public void run() {
        for (int i = 0; i < 2; i++) {
            this.sendData(Integer.toString(seq.incrementAndGet()));
        }
        latch.countDown();
    }


    private void sendData(String uuid) {
        long sequence = ringBuffer.next();
        try {
            OrderEvent orderEvent = ringBuffer.get(sequence);
            orderEvent.setId(uuid);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}

