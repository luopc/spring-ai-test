package com.luopc.platform.cloud.web.controller;

import com.luopc.platform.cloud.web.model.Message;
import com.luopc.platform.cloud.web.service.RSocketChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.FluxSink;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
public class ChatController {

    // 存储所有连接客户端的FluxSink实例，用于广播消息
    private final ConcurrentHashMap<String, FluxSink<Message>> clients = new ConcurrentHashMap<>();

    private RSocketChatService rSocketChatService;

    /**
     * 处理发送消息请求
     *
     * @param message 要发送的消息对象
     * @return 返回空Mono表示操作完成
     */
    @MessageMapping("chat.sendMessage")
    public Mono<Void> sendMessage(Message message) {
        log.info("Received message: {}", message); // 记录接收到的消息
        // 将消息广播给所有已连接的客户端
        clients.values().forEach(sink -> sink.next(message));
        return Mono.empty(); // 操作完成
    }

    /**
     * 处理客户端连接请求
     *
     * @param username 客户端用户名
     * @return 返回一个Flux流，包含来自服务器和其他客户端的消息
     */
    @MessageMapping("chat.connect")
    public Flux<Message> connect(String username) {
        log.info("User connected: {}", username); // 记录用户连接事件
        // 创建一个新的Flux流，并将其存储在clients集合中
        return Flux.create(sink -> clients.put(username, sink))
                .doOnCancel(() -> { // 当客户端断开连接时执行的操作
                    log.info("User disconnected: {}", username); // 记录用户断开连接事件
                    clients.remove(username); // 从clients集合中移除该用户的sink
                })
                .mergeWith(Flux.interval(Duration.ofSeconds(1)) // 合并一个定时消息流
                        .map(tick -> new Message("Server", "Ping"))); // 发送心跳消息
    }

    @MessageMapping("chat.stream")
    public Flux<Message> chat(Message message) {
        rSocketChatService.sendMessage(message);
        return rSocketChatService.getMessageStream();
    }
}
