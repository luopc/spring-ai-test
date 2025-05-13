package com.luopc.platform.cloud.service.dis;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.luopc.platform.cloud.service.dis.handler.TradeIdHandler;
import com.luopc.platform.cloud.service.dis.handler.TradeInfoPrintHandler;
import com.luopc.platform.cloud.service.dis.handler.TradeNameHandler;
import com.luopc.platform.cloud.service.dis.publish.TradePublisher;
import com.luopc.platform.cloud.service.dis.types.Trade;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class MainTestWithOrder {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {

        //构建一个线程池用于提交任务
        ExecutorService es1 = Executors.newSingleThreadExecutor();
        ThreadFactory es2 = Executors.defaultThreadFactory();
        //1 构建Disruptor
        Disruptor<Trade> disruptor = new Disruptor<Trade>(
                new EventFactory<Trade>() {
                    public Trade newInstance() {
                        return new Trade();
                    }
                },
                1024 * 1024, es2,
                ProducerType.SINGLE,
                new BusySpinWaitStrategy());

        //2 把消费者设置到Disruptor中 handleEventsWith
        //2.1 串行操作：
        disruptor
                .handleEventsWith(new TradeNameHandler())
                .handleEventsWith(new TradeIdHandler())
                .handleEventsWith(new TradeInfoPrintHandler());

        //3 启动disruptor
        RingBuffer<Trade> ringBuffer = disruptor.start();

        CountDownLatch latch = new CountDownLatch(1);

        long begin = System.currentTimeMillis();

        es1.submit(new TradePublisher(latch, disruptor));

        latch.await();  //进行向下

        disruptor.shutdown();
        es1.shutdown();
        System.err.println("总耗时: " + (System.currentTimeMillis() - begin));

    }

}
