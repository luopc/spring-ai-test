package com.luopc.platform.cloud.web.config;

import io.rsocket.transport.netty.server.TcpServerTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.util.SimpleRouteMatcher;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;

@Configuration
public class RSocketConfig {

    /**
     * 配置RSocket消息处理器
     *
     * @param strategies RSocket策略
     * @return RSocket消息处理器实例
     */
    @Bean
    public RSocketMessageHandler rsocketMessageHandler(RSocketStrategies strategies) {
        RSocketMessageHandler handler = new RSocketMessageHandler();
        handler.setRSocketStrategies(strategies); // 设置RSocket策略
        // 设置路由模式 "chat.*"
//        handler.setRouteMatcher(new PathPatternRouteMatcher("chat.*", new SimpleRouteMatcher()))
//                .acceptMimeType(org.springframework.util.MimeTypeUtils.APPLICATION_JSON); // 设置支持的消息类型
        return handler;
    }

    /**
     * 配置TCP服务器传输方式
     *
     * @return TCP服务器传输实例
     */
    @Bean
    public TcpServerTransport tcpServerTransport() {
        return TcpServerTransport.create(7000); // 监听7000端口
    }
}