package com.luopc.platform.cloud.service.dis.publish;

import java.util.concurrent.CountDownLatch;

import com.lmax.disruptor.dsl.Disruptor;
import com.luopc.platform.cloud.service.dis.types.Trade;

public class TradePublisher implements Runnable {

    private Disruptor<Trade> disruptor;
    private CountDownLatch latch;

    private static int PUBLISH_COUNT = 1;

    public TradePublisher(CountDownLatch latch, Disruptor<Trade> disruptor) {
        this.disruptor = disruptor;
        this.latch = latch;
    }

    public void run() {
        TradeEventTranslator eventTranslator = new TradeEventTranslator();
        for(int i =0; i < PUBLISH_COUNT; i ++){
            //新的提交任务的方式
            disruptor.publishEvent(eventTranslator);
        }
        latch.countDown();
    }

}
