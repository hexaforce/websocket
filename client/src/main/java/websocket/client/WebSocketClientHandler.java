package websocket.client;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketClientHandler extends AbstractWebSocketClientHandler {

	public final static String PATH = "/ws/binary";

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		log.info("handleTextMessage: {}", message.getPayload());
	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
		byte[] buffer = new byte[message.getPayloadLength()];
		message.getPayload().get(buffer);
		log.info("handleBinaryMessage: {}", buffer.length);
	}

	@Override
	protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
		log.info("handlePongMessage");
	}

}
