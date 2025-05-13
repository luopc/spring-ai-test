package com.luopc.platform.cloud.service.handler.impl;

import com.luopc.platform.cloud.service.handler.TradeEventVerifier;
import com.luopc.platform.cloud.service.mode.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PositionVerifier implements TradeEventVerifier {

    private final Map<String, CalculationResult> pendingResults = new ConcurrentHashMap<>();

    @Override
    public VerificationType getType() {
        return VerificationType.POSITION_CHECK;
    }

    @Override
    public VerificationResult verify(TradeEvent trade) {
        return VerificationResult.fail("Waiting for position calculation");
    }

    public void handleCalculationResult(CalculationResult result) {
        pendingResults.put(result.getTradeId(), result);
    }

    public VerificationResult resumeVerification(String tradeId) {
        CalculationResult result = pendingResults.get(tradeId);
        if (result == null) {
            return VerificationResult.fail("No calculation result received");
        }
        return result.isBreached() ?
                VerificationResult.fail("Position limit exceeded") :
                VerificationResult.pass();
    }
}
