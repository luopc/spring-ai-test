package com.luopc.platform.cloud.service.ai.config;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    private final ChatMemory chatMemory = MessageWindowChatMemory.builder().chatMemoryRepository(new InMemoryChatMemoryRepository()).build();

    @Bean
    public ChatClient chatClient(ChatModel chatModel, ToolCallbackProvider tools) {
        return ChatClient
                .builder(chatModel)
//                .defaultSystem("你是一个专业的金融分析师，精通各类金融工具的运用。你的回答应该简洁明了，并以JSON格式呈现。")
//                .defaultAdvisors(new PromptChatMemoryAdvisor(chatMemory))
                .defaultToolCallbacks(tools.getToolCallbacks())
                .build();
    }
}
