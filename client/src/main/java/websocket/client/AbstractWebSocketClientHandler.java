package websocket.client;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractWebSocketClientHandler implements WebSocketHandler {

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info(session.getId());
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getPayload();
			handleTextMessage(session, textMessage, text);
		} else if (message instanceof BinaryMessage) {
			BinaryMessage binaryMessage = (BinaryMessage) message;
			binaryMessage.getPayload().position(0);
			byte[] binary = binaryMessage.getPayload().array();
			handleBinaryMessage(session, binaryMessage, binary);
		} else if (message instanceof PongMessage) {
			handlePongMessage(session, (PongMessage) message);
		} else {
			throw new IllegalStateException("Unexpected WebSocket message type: " + message);
		}
	}

	abstract protected void handleTextMessage(WebSocketSession session, TextMessage message, String text) throws Exception;

	abstract protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message, byte[] binary) throws Exception;

	abstract protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception;

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.error(exception.getMessage(), exception);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info(status.toString());
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

}
