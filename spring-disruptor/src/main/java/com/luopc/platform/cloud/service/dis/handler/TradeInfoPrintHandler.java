package com.luopc.platform.cloud.service.dis.handler;

import com.lmax.disruptor.EventHandler;
import com.luopc.platform.cloud.service.dis.types.Trade;

import java.util.UUID;

public class TradeInfoPrintHandler implements EventHandler<Trade> {

    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        System.err.println("handler 3 : NAME: "
                + event.getName()
                + ", ID: "
                + event.getId()
                + ", PRICE: "
                + event.getPrice()
                + " INSTANCE : " + event);
    }

}
