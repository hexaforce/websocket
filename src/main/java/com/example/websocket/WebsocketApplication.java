package com.example.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.example.websocket.binary.BinaryWebSocketHandler;
import com.example.websocket.text.TextWebSocketHandler;

@SpringBootApplication
public class WebsocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsocketApplication.class, args);
	}
	
	@Configuration
	@EnableWebSocket
	public class WebSocketConfig implements WebSocketConfigurer {

		@Override
		public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
			registry.addHandler(textWebSocketHandler(), TextWebSocketHandler.PATH);
			registry.addHandler(binaryWebSocketHandler(), BinaryWebSocketHandler.PATH);
		}

	}
	
	@Bean
	public TextWebSocketHandler textWebSocketHandler() {
		return new TextWebSocketHandler();
	}

	@Bean
	public BinaryWebSocketHandler binaryWebSocketHandler() {
		return new BinaryWebSocketHandler();
	}
	
}
