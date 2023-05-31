package com.green.greenbook.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebSocketHandler webSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Websocket에 접속하기 위한 endpoint를 "ws/chat"으로 설정
        // 도메인이 다른 서버에서도 접속 가능하도록 CORS: setAllowedOrigins("*") 설정
        // ws://localhost:8080/ws/chat 으로 접속
        registry.addHandler(webSocketHandler, "/ws/chat")
            .setAllowedOrigins("*");
    }
}