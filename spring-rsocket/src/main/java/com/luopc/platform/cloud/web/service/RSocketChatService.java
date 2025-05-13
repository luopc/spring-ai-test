package com.luopc.platform.cloud.web.service;

import com.luopc.platform.cloud.web.model.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class RSocketChatService {

    private final Sinks.Many<Message> messageSink;

    public RSocketChatService() {
        this.messageSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void sendMessage(Message message) {
        messageSink.tryEmitNext(message);
    }

    public Flux<Message> getMessageStream() {
        return messageSink.asFlux();
    }

}
