package com.luopc.platform.cloud.service.service;

import com.luopc.platform.cloud.service.handler.impl.PositionVerifier;
import com.luopc.platform.cloud.service.handler.TradeEventVerifier;
import com.luopc.platform.cloud.service.mode.CalculationResult;
import com.luopc.platform.cloud.service.mode.TradeEvent;
import com.luopc.platform.cloud.service.mode.VerificationResult;
import com.luopc.platform.cloud.service.mode.VerificationType;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class VerificationEngine {

    private final Map<VerificationType, TradeEventVerifier> verifiers;
    private final Map<String, TradeVerificationContext> activeVerifications = new ConcurrentHashMap<>();

    // 初始化时按顺序组织校验流程
    private final List<VerificationType> verificationFlow = List.of(
            VerificationType.FORMAT_CHECK,
            VerificationType.STATIC_CHECK,
            VerificationType.POSITION_CHECK,
            VerificationType.MARGIN_CHECK
    );

    public void processTrade(TradeEvent trade) {
        TradeVerificationContext context = new TradeVerificationContext(trade);
        for (VerificationType verificationType : verificationFlow) {
            TradeEventVerifier tradeEventVerifier = verifiers.get(verificationType);
            VerificationResult result = tradeEventVerifier.verify(trade);
            context.updateResult(verificationType, result);
            if(!result.isPass() && verificationType.isBlocking()){
                break;
            }
        }
        finalizeVerification(context);
    }

    public void handleCalculationResult(CalculationResult result) {
        PositionVerifier positionVerifier = (PositionVerifier) verifiers.get(VerificationType.POSITION_CHECK);
        positionVerifier.handleCalculationResult(result);
        List<VerificationResult> verificationResults = positionVerifier.handleCalculationResult(result);
        verificationResults.forEach(rs -> {
            TradeVerificationContext context = activeVerifications.get(rs.getTradeId());
            context.updateResult(VerificationType.POSITION_CHECK, rs);
            finalizeVerification(context);
        });
    }


    private void finalizeVerification(TradeVerificationContext context) {
       TradeEvent tradeEvent =  context.getTrade();
       if(context.isAllPassed()){
           // send approve message
       } else {
           // handle reject message
           handleExceptionResult(context);
       }
    }


    private void handleExceptionResult(TradeVerificationContext context) {


    }


}
