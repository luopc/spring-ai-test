package com.luopc.platform.cloud.service.dis.handler;

import com.lmax.disruptor.EventHandler;
import com.luopc.platform.cloud.service.dis.types.Trade;

public class TradeNameHandler implements EventHandler<Trade>{

    //EventHandler
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(event);
    }

    //WorkHandler
    public void onEvent(Trade event) throws Exception {
        System.err.println("handler 1 : SET NAME");
        Thread.sleep(1000);
        event.setName("H1");
    }

}
