package websocket.server;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.protobuf.ByteString;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BinaryWebSocketHandler extends AbstractBinaryWebSocketHandler {

	public final static String PATH = "/ws/binary";

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message, byte[] binary) throws Exception {
		session.sendMessage(message);
		InfiniteStreamRecognize.sharedQueue.put(ByteString.copyFrom(binary));
	}

	@Override
	protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
		log.info("handlePongMessage");
	}

}
