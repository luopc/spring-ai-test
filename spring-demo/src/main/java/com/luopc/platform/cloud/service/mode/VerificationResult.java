package com.luopc.platform.cloud.service.mode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationResult {

    private boolean passed;
    private String reason;

    public static VerificationResult pass() {
        return new VerificationResult(true, "Passed");
    }

    public static VerificationResult fail(String reason) {
        return new VerificationResult(false, reason);
    }

}
