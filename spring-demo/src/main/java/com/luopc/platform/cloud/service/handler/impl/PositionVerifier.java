package com.luopc.platform.cloud.service.handler.impl;

import com.luopc.platform.cloud.service.handler.TradeEventVerifier;
import com.luopc.platform.cloud.service.mode.CalculationResult;
import com.luopc.platform.cloud.service.mode.TradeEvent;
import com.luopc.platform.cloud.service.mode.VerificationResult;
import com.luopc.platform.cloud.service.mode.VerificationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PositionVerifier implements TradeEventVerifier {

    private final Map<String, CalculationResult> cacheResults = new ConcurrentHashMap<>();
    private final Map<String, TradeEvent> tradeEventCache = new ConcurrentHashMap<>();

    @Override
    public VerificationResult verify(TradeEvent trade) {
        if (cacheResults.containsKey(trade.getClientId())) {
            CalculationResult result = cacheResults.get(trade.getClientId());
            List<String> calTradeList = result.getCalculationTradeList();
            if (calTradeList.contains(trade.getTradeId())) {
                return VerificationResult.pass(trade.getTradeId());
            }
        }
        tradeEventCache.put(trade.getTradeId(), trade);
        return VerificationResult.hold(trade.getTradeId());
    }


    public List<VerificationResult> handleCalculationResult(CalculationResult calculationResult) {
        cacheResults.put(calculationResult.getClientId(), calculationResult);
        List<VerificationResult> verificationResults = new ArrayList<>();
        List<String> calTradeList =  calculationResult.getCalculationTradeList();
        calTradeList.forEach(tradeId -> {
            TradeEvent tradeEvent = tradeEventCache.get(tradeId);

        });
        return verificationResults;
    }
}
