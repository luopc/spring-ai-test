package com.luopc.platform.cloud.service.subscriber;

import com.luopc.platform.cloud.service.mode.TradeEvent;
import com.luopc.platform.cloud.service.service.VerificationEngine;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ClientUpdateEventHandler {

    private final VerificationEngine verificationEngine;

    public void handleContractUpdate(String clientId) {
        List<TradeEvent> affectedTrades = findAffectedTrades(clientId);
        affectedTrades.forEach(verificationEngine::processTrade);
    }

    private List<TradeEvent> findAffectedTrades(String clientId) {
        return new ArrayList<>();
    }

}
