package com.luopc.platform.cloud.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <a href="https://blog.csdn.net/renpeng301/article/details/145369138">...</a>
 */

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableScheduling
public class ChatWithAIApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatWithAIApplication.class, args);
    }
}