package websocket.client;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecognitionResponse {
	private String resultEndTime;
	private boolean _final;
//	private float stability;
	private String transcript;
//	private float confidence;
	private float score;
}
