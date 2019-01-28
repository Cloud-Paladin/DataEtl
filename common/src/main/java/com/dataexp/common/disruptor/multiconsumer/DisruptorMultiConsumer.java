package com.dataexp.common.disruptor.multiconsumer;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: Bing.Li
 * @create: 2019-01-25
 */
public class DisruptorMultiConsumer {

    public static void main(String[] args) throws InterruptedException {
        ThreadFactory producerFactory = Executors.defaultThreadFactory();
        // 创建缓冲池
        LongEventFactory eventFactory = new LongEventFactory();

        // 创建bufferSize ,也就是RingBuffer大小，必须是2的N次方
        int ringBufferSize = 1024 * 1024;
        Disruptor<LongEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, producerFactory,
                ProducerType.MULTI, new BlockingWaitStrategy());
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        // 创建10个消费者来处理同一个生产者发的消息(这10个消费者不重复消费消息)
        Consumer[] consumers = new Consumer[10];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer();
        }
        disruptor.handleEventsWithWorkerPool(consumers);

        disruptor.start();

        //多个生产者,ProducerType设置为MULTI,如果设置为SINGLE有的消息会丢弃
        for (int i = 0; i < 500; i++) {
            new Thread(new LongEventProducer(ringBuffer)).start();
        }
        Thread.sleep(2000);
        disruptor.shutdown();
        System.out.println(Consumer.count);
    }
}

class LongEventProducer implements Runnable {
    private final RingBuffer<LongEvent> ringBuffer;
    private static AtomicLong seq = new AtomicLong(0);

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            long sequence = ringBuffer.next();  // Grab the next sequence
            try {
                LongEvent event = ringBuffer.get(sequence); // Get the entry in the Disruptor
                // for the sequence
                event.set(seq.incrementAndGet());  // Fill with data
                ringBuffer.publish(sequence);
            } catch (Exception e) {
            }
        }
    }
}

/**
 * @author wuweifeng wrote on 2018/4/8.
 */
class Consumer implements WorkHandler<LongEvent> {
    public static AtomicInteger count = new AtomicInteger(0);

    @Override
    public void onEvent(LongEvent longEvent) throws Exception {
        System.out.println(Thread.currentThread().getName() + "消费者消费了消息：" + longEvent.getNumber());
        count.incrementAndGet();
    }
}

class LongEvent {
    private Long number;

    public Long getNumber() {
        return number;
    }

    public void set(Long number) {
        this.number = number;
    }
}

class LongEventFactory implements EventFactory<LongEvent> {
    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
