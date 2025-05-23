package com.luopc.platform.cloud.service.handler.impl;

import com.luopc.platform.cloud.service.handler.TradeEventVerifier;
import com.luopc.platform.cloud.service.mode.TradeEvent;
import com.luopc.platform.cloud.service.mode.VerificationResult;
import com.luopc.platform.cloud.service.mode.VerificationType;

public class TradeFormatVerifier implements TradeEventVerifier {


    @Override
    public VerificationResult verify(TradeEvent trade) {
        if (trade.getTradeId() == null || trade.getTradeId().isEmpty()) {
            return VerificationResult.fail(trade.getTradeId(), "Invalid trade ID");
        }

        // 模拟伪造数据逻辑
        if (trade.getCurrency() == null || trade.getCurrency().isEmpty()) {
            return VerificationResult.fail(trade.getTradeId(), "Trade currency error");
        }

        return VerificationResult.pass(trade.getTradeId());
    }
}
