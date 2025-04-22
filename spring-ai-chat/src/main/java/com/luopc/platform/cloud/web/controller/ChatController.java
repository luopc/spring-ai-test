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
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.api.OpenAiImageApi;
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
@RequestMapping("/chat")
@Tag(name = "ChatController", description = "Chat")
public class ChatController {
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    //智能对话的客户端
    private final ChatClient chatClient;
    private final ChatMemory inMemoryChatMemory;
    @Autowired
    private ChatModel chatModel;
    @Autowired
    private OpenAiImageModel openaiImageModel;

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
    @Operation(summary = "Generation", description = "Author @Robin")
    public String generation(@RequestParam(value = "message", defaultValue = "给我讲个笑话") String message) {
        log.info("message:{}", message);
        ChatResponse response = chatModel.call(
                new Prompt(
                        message, // new UserMessage(message)
                        OpenAiChatOptions.builder()
                                .model("deepseek-r1")
                                .temperature(0.4)//让生成文字更有温度
                                .build()
                ));
        //等同于 .content
        return response.getResult().getOutput().getText();
    }


    @GetMapping(value = "/draw")
    public String generation05(
            @RequestParam(value = "message",
                    defaultValue = "画个龙") String message) {
        ImageResponse response = openaiImageModel.call(
                new ImagePrompt(message, // 图片提示词
                        OpenAiImageOptions.builder()
                                .quality("hd") // 图片质量
                                .withModel(OpenAiImageApi.DEFAULT_IMAGE_MODEL) // 图片模型
                                .N(1) // 生成图片数量
                                .height(1024)
                                .width(1024).build())

        );
        return response.getResult().getOutput().getUrl();
    }




}

