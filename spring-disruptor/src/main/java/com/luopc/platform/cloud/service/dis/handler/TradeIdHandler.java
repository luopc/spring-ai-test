package com.luopc.platform.cloud.service.dis.handler;

import com.lmax.disruptor.EventHandler;
import com.luopc.platform.cloud.service.dis.types.Trade;

import java.util.UUID;

public class TradeIdHandler implements EventHandler<Trade> {

    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        System.err.println("handler 2 : SET ID");
        Thread.sleep(2000);
        event.setId(UUID.randomUUID().toString());
    }

}
