package com.luopc.platform.cloud.service.mode;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CalculationResult {

    private String clientId;

    private boolean breached;

    public List<String> getCalculationTradeList(){
        return new ArrayList<>();
    }
}
