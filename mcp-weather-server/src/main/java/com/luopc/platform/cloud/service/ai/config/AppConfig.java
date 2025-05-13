package com.luopc.platform.cloud.service.ai.config;


import com.luopc.platform.cloud.service.ai.resource.McpServiceResource;
import com.luopc.platform.cloud.service.ai.service.WeatherService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ToolCallbackProvider weatherTools(McpServiceResource mcpServiceResource) {
        return MethodToolCallbackProvider.builder().toolObjects(mcpServiceResource).build();
    }

}
