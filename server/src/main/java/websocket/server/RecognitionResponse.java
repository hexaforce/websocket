package websocket.server;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecognitionResponse {
	private Timestamp resultEndTime;
	private boolean _final;
	private float stability;
	private String transcript;
	private float confidence;
}
