package com.luopc.platform.cloud.service.service;

import com.luopc.platform.cloud.service.mode.TradeEvent;
import com.luopc.platform.cloud.service.mode.VerificationResult;
import com.luopc.platform.cloud.service.mode.VerificationType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class TradeVerificationContext {

    private final TradeEvent trade;
    private final Map<VerificationType, VerificationResult> results = new EnumMap<>(VerificationType.class);
    private VerificationType currentStage = VerificationType.FORMAT_CHECK;

    public void updateResult(VerificationType type, VerificationResult result) {
        results.put(type, result);
        currentStage = type;
    }

    public List<VerificationType> getFailedStages() {
        return results.entrySet().stream()
                .filter(e -> !e.getValue().isPassed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
