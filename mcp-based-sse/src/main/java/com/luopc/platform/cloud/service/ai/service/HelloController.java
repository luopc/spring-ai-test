package com.luopc.platform.cloud.service.ai.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class HelloController {

    @Tool(description = "say hello")
    public String hello() {
        return "hello, devops";
    }

    @Tool(description = "say hello to someone")
    public String helloTo(@ToolParam(description = "name of the guy you want to say hello to") String name) {
        return "Hello, " + name;
    }
}
