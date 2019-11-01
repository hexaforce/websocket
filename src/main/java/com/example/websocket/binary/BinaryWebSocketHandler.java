package com.example.websocket.binary;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.WebSocketSession;

public class BinaryWebSocketHandler extends AbstractBinaryWebSocketHandler {

	public final static String PATH = "/ws/binary";

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
		
	}

	@Override
	protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
		
	}

}
