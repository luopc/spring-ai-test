package com.luopc.platform.cloud.service.dis.handler;

import com.lmax.disruptor.EventHandler;
import com.luopc.platform.cloud.service.dis.types.Trade;

import java.util.UUID;

public class TradePriceHandler implements EventHandler<Trade> {

    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        System.err.println("handler 5 : GET PRICE: " +  event.getPrice());
        Thread.sleep(1000);
        event.setPrice(event.getPrice() + 3.0);
    }

}
