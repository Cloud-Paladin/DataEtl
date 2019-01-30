package com.dataexp.common.disruptor.example;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Bing.Li
 * @create: 2019-01-25
 */
public class DisruptorExecModels {

    /**
     * 并行计算实现,c1,c2互相不依赖
     * <br/>
     * p --> c11
     *   --> c21
     */
    public static void parallel(Disruptor<LongEvent> disruptor){
        disruptor.handleEventsWith(new C11EventHandler(),new C21EventHandler());
        disruptor.start();
    }


    /**
     * 串行依次执行
     * <br/>
     * p --> c11 --> c21
     * @param disruptor
     */
    public static void serial(Disruptor<LongEvent> disruptor){
        disruptor.handleEventsWith(new C11EventHandler()).then(new C21EventHandler());
        disruptor.start();
    }

    /**
     * 菱形方式执行
     * <br/>
     *   --> c11
     * p          --> c21
     *   --> c12
     * @param disruptor
     */
    public static void diamond(Disruptor<LongEvent> disruptor){
        disruptor.handleEventsWith(new C11EventHandler(),new C12EventHandler()).then(new C21EventHandler());
        disruptor.start();
    }


    /**
     * 链式并行计算
     * <br/>
     *   --> c11 --> c12
     * p
     *   --> c21 --> c22
     * @param disruptor
     */
    public static void chain(Disruptor<LongEvent> disruptor){
        disruptor.handleEventsWith(new C11EventHandler()).then(new C12EventHandler());
        disruptor.handleEventsWith(new C21EventHandler()).then(new C22EventHandler());
        disruptor.start();
    }

    /**
     * 并行计算实现,c1,c2互相不依赖,同时C1，C2分别有2个实例
     * <br/>
     * p --> c11
     *   --> c21
     */
    public static void parallelWithPool(Disruptor<LongEvent> disruptor){
        disruptor.handleEventsWithWorkerPool(new C11EventHandler(),new C11EventHandler());
//        disruptor.handleEventsWithWorkerPool(new C21EventHandler(),new C21EventHandler());
        disruptor.start();
    }
    /**
     * 串行依次执行,同时C11，C21分别有2个实例
     * <br/>
     * p --> c11 --> c21
     * @param disruptor
     */
    public static void serialWithPool(Disruptor<LongEvent> disruptor){
        disruptor.handleEventsWithWorkerPool(new C11EventHandler(),new C11EventHandler()).then(new C21EventHandler(),new C21EventHandler());
        disruptor.start();
    }


    public static void main(String[] args) {
        int bufferSize = 1024*1024;//环形队列长度，必须是2的N次方
        EventFactory<LongEvent> eventFactory = new LongEventFactory();
        /**
         * 定义Disruptor，基于单生产者，阻塞策略
         */
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(eventFactory,bufferSize, Executors.defaultThreadFactory(),ProducerType.SINGLE,new BlockingWaitStrategy());
        /////////////////////////////////////////////////////////////////////
        //这里是调用各种不同方法的地方.
        //TODO:实测各种链式处理不起作用
//        parallel(disruptor);
//        serial(disruptor);
//        diamond(disruptor);
//        chain(disruptor);

/**     上面的实例，每一种消费者都只有一个实例，如果想多个实例形成一个线程池并发处理多个任务怎么办？
 *      如果使用disruptor.handleEventWith(new C11EventHandler(),new C11EventHandler(),...)这种，会造成重复消费同一个数据，不是我们想要的。
 *      我们想要的是同一个类的实例消费不同的数据，怎么办？
 *       - 首先，消费者类需要实现WorkHandler接口，而不是EventHandler接口。为了方便，我们同时实现了这两个接口。
 *       - 其次，disruptor调用handleEventsWithWorkerPool方法，而不是handleEventsWith方法
 *       - 最后，实例化多个事件消费类
*/

        parallelWithPool(disruptor);
//        serialWithPool(disruptor);

        System.out.println("end");

        /////////////////////////////////////////////////////////////////////
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        /**
         * 输入10
         */
        ringBuffer.publishEvent(new LongEventTranslator(),1L);
        ringBuffer.publishEvent(new LongEventTranslator(),2L);
        ringBuffer.publishEvent(new LongEventTranslator(),3L);
        ringBuffer.publishEvent(new LongEventTranslator(),4L);
        ringBuffer.publishEvent(new LongEventTranslator(),5L);
        ringBuffer.publishEvent(new LongEventTranslator(),6L);
    }
}

class LongEvent {
    private Long number;

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }
}

class LongEventFactory implements EventFactory<LongEvent> {
    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}


class LongEventTranslator implements EventTranslatorOneArg<LongEvent, Long> {
    @Override
    public void translateTo(LongEvent event, long sequence, Long arg0) {
        event.setNumber(arg0);
    }
}

/**
 * 该消费者执行将数值+10的操作。可以看到该消费者同时实现了EventHandler和WorkHandler两个接口。
 * 如果不需要池化，只需要实现EventHandler类即可。如果需要池化，只需要实现WorkHandler类即可。
 * 本例为了能够同时讲解池化和非池化的实现，因此同时实现了两个类，当然，也没啥问题。
 *
 */
class C11EventHandler implements EventHandler<LongEvent>, WorkHandler<LongEvent> {
    public static final AtomicInteger seq = new AtomicInteger(1);
    public final String name;

    public C11EventHandler() {
        name = "C11:" + seq.getAndIncrement();
    }

    public String getName() {
        return name;
    }

    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        long number = event.getNumber();
        number += 10;
        System.out.println(System.currentTimeMillis()+": "+ name + " consumer input number="+ event.getNumber() +" finished.number=" + number);
        event.setNumber(number);
    }

    @Override
    public void onEvent(LongEvent event) throws Exception {
        long number = event.getNumber();
        number += 10;
        System.out.println(System.currentTimeMillis()+": "+ name + " consumer input number="+ event.getNumber() +" finished.number=" + number);
        event.setNumber(number);
    }
}

/**
 * 该消费者类执行将数值乘以10的操作。
 */
class C12EventHandler implements EventHandler<LongEvent>,WorkHandler<LongEvent> {

    public static final AtomicInteger seq = new AtomicInteger(1);
    public final String name;

    public C12EventHandler() {
        name = "C12:" + seq.getAndIncrement();
    }

    public String getName() {
        return name;
    }

    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        long number = event.getNumber();
        number *= 10;
        System.out.println(System.currentTimeMillis()+": "+ name + " consumer input number="+ event.getNumber() +" finished.number=" + number);
        event.setNumber(number);
    }

    @Override
    public void onEvent(LongEvent event) throws Exception {
        long number = event.getNumber();
        number *= 10;
        System.out.println(System.currentTimeMillis()+": "+ name + " consumer input number="+ event.getNumber() +" finished.number=" + number);
        event.setNumber(number);
    }
}

/**
 * 该消费者类负责将数值+20.
 */
class C21EventHandler implements EventHandler<LongEvent>,WorkHandler<LongEvent> {
    public static final AtomicInteger seq = new AtomicInteger(1);
    public final String name;

    public C21EventHandler() {
        name = "C21:" + seq.getAndIncrement();
    }

    public String getName() {
        return name;
    }

    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        long number = event.getNumber();
        number += 20;
        System.out.println(System.currentTimeMillis()+": "+ name + " consumer input number="+ event.getNumber() +" finished.number=" + number);
        event.setNumber(number);
    }

    @Override
    public void onEvent(LongEvent event) throws Exception {
        long number = event.getNumber();
        number += 20;
        System.out.println(System.currentTimeMillis()+": "+ name + " consumer input number="+ event.getNumber() +" finished.number=" + number);
        event.setNumber(number);
    }
}

/**
 * 该消费者类负责将数值*20
 */
class C22EventHandler implements EventHandler<LongEvent>,WorkHandler<LongEvent> {
    public static final AtomicInteger seq = new AtomicInteger(1);
    public final String name;

    public C22EventHandler() {
        name = "C21:" + seq.getAndIncrement();
    }

    public String getName() {
        return name;
    }
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        long number = event.getNumber();
        number *= 20;
        System.out.println(System.currentTimeMillis()+": "+ name + " consumer input number="+ event.getNumber() +" finished.number=" + number);
        event.setNumber(number);
    }

    @Override
    public void onEvent(LongEvent event) throws Exception {
        long number = event.getNumber();
        number *= 20;
        System.out.println(System.currentTimeMillis()+": "+ name + " consumer input number="+ event.getNumber() +" finished.number=" + number);
        event.setNumber(number);
    }
}