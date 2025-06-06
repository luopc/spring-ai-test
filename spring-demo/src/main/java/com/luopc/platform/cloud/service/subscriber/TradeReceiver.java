package com.luopc.platform.cloud.service.subscriber;

import com.luopc.platform.cloud.service.mode.TradeEvent;
import com.luopc.platform.cloud.service.service.VerificationEngine;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TradeReceiver {

    private VerificationEngine verificationEngine;

    public void processTrade(TradeEvent event) {
        verificationEngine.processTrade(event);
    }

}
