package com.luopc.platform.cloud.service.handler.impl;

import com.luopc.platform.cloud.service.handler.TradeEventVerifier;
import com.luopc.platform.cloud.service.mode.ClientInfo;
import com.luopc.platform.cloud.service.mode.TradeEvent;
import com.luopc.platform.cloud.service.mode.VerificationResult;
import com.luopc.platform.cloud.service.mode.VerificationType;
import com.luopc.platform.cloud.service.service.ClientService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class StaticConfigVerifier implements TradeEventVerifier {

    private final ClientService clientService;

    @Override
    public VerificationType getType() {
        return VerificationType.CONTRACT_CHECK;
    }

    @Override
    public VerificationResult verify(TradeEvent trade) {
        ClientInfo clientInfo = clientService.getClientInfo(trade.getClientId());

        // 使用伪造数据时的模拟逻辑
        if (!clientInfo.getAllowedTypes().contains(trade.getTradeType())) {
            return VerificationResult.fail("Trade type not allowed");
        }

        return VerificationResult.pass();
    }
}
