package com.example.websocket.text;

import java.io.IOException;

import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TextWebSocketHandler extends AbstractTextWebSocketHandler {

	public final static String PATH = "/ws/text";

	public void unicast(TextMessage message) {

	}

	public void multicast(TextMessage message) {

	}

	public void broadcast(TextMessage message) {
		sessions.parallelStream().forEach(session -> {
			try {
				session.sendMessage(message);
				session.getPrincipal();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		});
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String value = defaultMapper.writeValueAsString(message);
		log.info("value:{}", value);
	}

	@Override
	protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
		String value = defaultMapper.writeValueAsString(message);
		log.info("value:{}", value);
	}

}
