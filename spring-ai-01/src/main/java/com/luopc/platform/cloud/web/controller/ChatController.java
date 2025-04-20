package com.luopc.platform.cloud.web.controller;

import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "ChatController", description = "Chat")
public class ChatController {
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    //智能对话的客户端
    private final ChatClient chatClient;
    private final ChatMemory inMemoryChatMemory;

    public ChatController(ChatClient.Builder chatClientBuilder, ChatMemory inMemoryChatMemory) {
        this.chatClient = chatClientBuilder.build();
        this.inMemoryChatMemory = inMemoryChatMemory;
    }

    @Operation(summary = "Chat", description = "Author @Robin")
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamChat(@RequestBody ChatRequest request) {
        log.info("message:{}", request);
        //用户id
        String userId = request.userId();
        return chatClient.prompt(request.message())
                .advisors(new MessageChatMemoryAdvisor(inMemoryChatMemory, userId, 10), new SimpleLoggerAdvisor())
                .stream().content().map(content -> ServerSentEvent.builder(content).event("message").build())
                //问题回答结速标识,以便前端消息展示处理
                .concatWithValues(ServerSentEvent.builder("[DONE]").build())
                .onErrorResume(e -> Flux.just(ServerSentEvent.builder("Error: " + e.getMessage()).event("error").build()));
    }

    record ChatRequest(String userId, String message) {

    }


    @GetMapping("/generation")
    @Operation(summary = "Chat", description = "Author @Robin")
    public String generation(@RequestParam(value = "message", defaultValue = "给我讲个笑话") String message) {
        log.info("message:{}", message);
        return this.chatClient.prompt()  // prompt:提示词
                .user(message) // message:用户输入的信息
                .call() // 调用大模型 远程请求大模型
                .content(); // 获取大模型的返回结果
    }



}

