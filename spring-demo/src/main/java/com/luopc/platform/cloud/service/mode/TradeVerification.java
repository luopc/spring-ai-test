package com.luopc.platform.cloud.service.mode;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class TradeVerification {

    private String tradeId;
    private Map<VerificationType, VerificationResult> results;
    private VerificationStatus status;
    private List<ExceptionRecord> exceptions;
    private LocalDateTime expireTime;

}
