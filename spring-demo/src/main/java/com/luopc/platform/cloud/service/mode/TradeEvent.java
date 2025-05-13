package com.luopc.platform.cloud.service.mode;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TradeEvent {

    private String tradeId;
    private String tradeType;
    private String tradeStatus;
    private String tradeTime;
    private String strike;
    private BigDecimal amount;
    private String currency;
    private String clientId;

}
