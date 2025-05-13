package com.luopc.platform.cloud.service.handler;

import com.luopc.platform.cloud.service.mode.TradeEvent;
import com.luopc.platform.cloud.service.mode.VerificationResult;
import com.luopc.platform.cloud.service.mode.VerificationType;

public interface TradeEventVerifier {

    VerificationResult verify(TradeEvent trade);
    VerificationType getType();

}
