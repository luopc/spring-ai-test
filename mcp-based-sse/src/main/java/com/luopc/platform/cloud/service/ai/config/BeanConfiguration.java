package com.luopc.platform.cloud.service.ai.config;

import com.luopc.platform.cloud.service.ai.service.HelloController;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;

public class BeanConfiguration {


    @Bean
    public ToolCallbackProvider toolCallbackProvider(HelloController helloController) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(helloController)
                .build();
    }
}
