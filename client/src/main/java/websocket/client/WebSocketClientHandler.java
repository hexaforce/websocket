package websocket.client;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketClientHandler extends AbstractWebSocketClientHandler {

	public final static String PATH = "/ws/binary";

	private BufferedOutputStream2 bufferedOutputStream;
	
	private String uuid = "";
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		RecognitionResponse response = new Gson().fromJson(message.getPayload(), RecognitionResponse.class);
		if (response.is_final()) {
			int bufferedSize = bufferedOutputStream.size();
			bufferedOutputStream.flush();
			new Thread(new Speecker(uuid, bufferedSize)).run();
			bufferedOutputStream.close();
			this.bufferedOutputStream = null;
			log.info("Message: {}", message.getPayload());
		}
	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
		message.getPayload().position(0);
		byte[] buffer = message.getPayload().array();
		
		if (bufferedOutputStream == null) {
			this.uuid = UUID.randomUUID().toString();
			this.bufferedOutputStream = new BufferedOutputStream2(new FileOutputStream(uuid));
		} else {
			this.bufferedOutputStream.write(buffer);
		}
	}

	@Override
	protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
		log.info("handlePongMessage");
	}
	
	public class BufferedOutputStream2 extends BufferedOutputStream{

		public BufferedOutputStream2(OutputStream out) {
			super(out);
		}
		public int size() {
			return super.buf.length;
		}
		
	}

}
