package com.luopc.platform.cloud.service.mode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationResult {

    private String tradeId;
    private VerificationStatus status;
    private String reason;

    public static VerificationResult pass(String tradeId) {
        return new VerificationResult(tradeId, VerificationStatus.PASSED, "Passed");
    }

    public static VerificationResult fail(String tradeId, String reason) {
        return new VerificationResult(tradeId, VerificationStatus.EXCEPTION, reason);
    }

    public static VerificationResult hold(String tradeId) {
        return new VerificationResult(tradeId, VerificationStatus.ON_HOLD, "");
    }

    public boolean isPass(){
        return status == VerificationStatus.PASSED;
    }
}
