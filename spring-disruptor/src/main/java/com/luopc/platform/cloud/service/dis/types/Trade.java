package com.luopc.platform.cloud.service.dis.types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Disruptor中的 Event
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trade {

    private String id;
    private String name;
    private double price;
    private AtomicInteger count = new AtomicInteger(0);


}
