package com.luopc.platform.cloud.service.ai.config;


import com.luopc.platform.cloud.service.ai.resource.McpServiceResource;
import com.luopc.platform.cloud.service.ai.service.WeatherService;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    public ToolCallbackProvider weatherTools(McpServiceResource mcpServiceResource) {
        return MethodToolCallbackProvider.builder().toolObjects(mcpServiceResource).build();
    }


    public List<McpServerFeatures.SyncPromptSpecification> myCompletions() {

        return List.of();
    }
}
