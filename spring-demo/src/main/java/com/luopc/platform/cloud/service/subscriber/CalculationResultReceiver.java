package com.luopc.platform.cloud.service.subscriber;

import com.luopc.platform.cloud.service.mode.CalculationResult;
import com.luopc.platform.cloud.service.mode.TradeEvent;
import com.luopc.platform.cloud.service.service.VerificationEngine;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CalculationResultReceiver {

    private final VerificationEngine verificationEngine;

    public void processCalculationResult(CalculationResult result) {
        verificationEngine.handleCalculationResult(result);
    }

}
