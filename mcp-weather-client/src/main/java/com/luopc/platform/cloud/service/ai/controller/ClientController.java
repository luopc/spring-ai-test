package com.luopc.platform.cloud.service.ai.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ClientController {

    @Autowired
    private ChatClient chatClient;

    @RequestMapping("/chat")
    public String chat(@RequestParam(value = "msg", defaultValue = "今天天气如何？") String msg) {
        log.info("收到消息: " + msg);
        String response = chatClient.prompt()
                .user(msg)
                .call()
                .content();
        System.out.println("响应结果: " + response);
        return response;
    }

}
