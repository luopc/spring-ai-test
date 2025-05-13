package com.luopc.platform.cloud.service.dis.publish;

import com.lmax.disruptor.EventTranslator;
import com.luopc.platform.cloud.service.dis.types.Trade;

import java.util.Random;

public class TradeEventTranslator implements EventTranslator<Trade> {

    private final Random random = new Random();

    public void translateTo(Trade event, long sequence) {
        this.generateTrade(event);
    }

    private void generateTrade(Trade event) {
        event.setPrice(random.nextDouble() * 9999);
    }

}
