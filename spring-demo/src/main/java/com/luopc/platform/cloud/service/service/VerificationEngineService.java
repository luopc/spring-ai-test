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
public class VerificationEngineService {

    private final List<TradeEventVerifier> verifiers;
    private final Map<String, TradeVerificationContext> activeVerifications = new ConcurrentHashMap<>();

    // 初始化时按顺序组织校验流程
    private final List<VerificationType> verificationFlow = List.of(
            VerificationType.FORMAT_CHECK,
            VerificationType.CONTRACT_CHECK,
            VerificationType.POSITION_CHECK,
            VerificationType.MARGIN_CHECK
    );

    public void processTrade(TradeEvent trade) {
        TradeVerificationContext context = new TradeVerificationContext(trade);
        activeVerifications.put(trade.getTradeId(), context);

        executeNextStep(context);
    }

    public void resumeVerification(String tradeId, CalculationResult result) {
        TradeVerificationContext context = activeVerifications.get(tradeId);
        if (context == null) return;

        PositionVerifier positionVerifier = (PositionVerifier) getVerifier(VerificationType.POSITION_CHECK);
        positionVerifier.handleCalculationResult(result);
        VerificationResult resultWrapper = positionVerifier.resumeVerification(tradeId);

        context.updateResult(VerificationType.POSITION_CHECK, resultWrapper);
        executeNextStep(context);
    }

    private void executeNextStep(TradeVerificationContext context) {
        VerificationType currentStage = context.getCurrentStage();
        VerificationType nextStage = determineNextStage(currentStage);

        while (nextStage != null) {
            TradeEventVerifier verifier = getVerifier(nextStage);
            VerificationResult result = verifier.verify(context.getTrade());

            context.updateResult(nextStage, result);

            if (!result.isPassed() && isBlockingStage(nextStage)) {
                handleExceptionTrade(context, nextStage, result);
                break;
            }

            nextStage = determineNextStage(nextStage);
        }

        if (isFinalStage(nextStage)) {
            finalizeVerification(context);
        }
    }

    private void finalizeVerification(TradeVerificationContext context) {

    }

    private boolean isFinalStage(VerificationType nextStage) {
        return verificationFlow.contains(nextStage);
    }

    private boolean isBlockingStage(VerificationType nextStage) {
        return verificationFlow.contains(nextStage);
    }

    private VerificationType determineNextStage(VerificationType nextStage) {
        int index = verificationFlow.indexOf(nextStage) + 1;
        return verificationFlow.get(index);
    }

    private void handleExceptionTrade(TradeVerificationContext context, VerificationType nextStage, VerificationResult result) {


    }

    private TradeEventVerifier getVerifier(VerificationType type) {
        return verifiers.stream()
                .filter(v -> v.getType() == type)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Verifier not found: " + type));
    }

}
