package com.luopc.platform.cloud.service.mode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VerificationType {

    FORMAT_CHECK("formatCheck", true),
    STATIC_CHECK("staticCheck", false),
    POSITION_CHECK("formatCheck", false),
    MARGIN_CHECK("formatCheck", false);

    private final String name;
    private final boolean blocking;

}
