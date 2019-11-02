package websocket.server;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.OutOfRangeException;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.StreamingRecognitionConfig;
import com.google.cloud.speech.v1.StreamingRecognitionResult;
import com.google.cloud.speech.v1.StreamingRecognizeRequest;
import com.google.cloud.speech.v1.StreamingRecognizeResponse;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InfiniteStreamRecognize implements Runnable {

	private final WebSocketSession session;

	public InfiniteStreamRecognize(WebSocketSession session) {
		this.session = session;
	}

	public static volatile BlockingQueue<ByteString> sharedQueue = new LinkedBlockingQueue<ByteString>();

	// ~5 minutes(1 second time lag)
	private static final int STREAMING_LIMIT = 290000;
	private final RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder().setEncoding(RecognitionConfig.AudioEncoding.LINEAR16).setLanguageCode("ja-JP").setSampleRateHertz(16000).build();

	private final StreamingRecognitionConfig streamingRecognitionConfig = StreamingRecognitionConfig.newBuilder().setConfig(recognitionConfig).setInterimResults(true).build();

	private StreamController referenceToStreamController;
	private final ResponseObserver<StreamingRecognizeResponse> responseObserver = new ResponseObserver<StreamingRecognizeResponse>() {

		@Override
		public void onStart(StreamController controller) {
			log.info("onStart");
			referenceToStreamController = controller;
		}

		@Override
		public void onResponse(StreamingRecognizeResponse response) {

			for (StreamingRecognitionResult result : response.getResultsList()) {

				float stability = result.getStability();

				long end = ((result.getResultEndTime().getSeconds() * 1000) + (result.getResultEndTime().getNanos() / 1000000));
				String resultEndTime = new Timestamp(end).toString().replace("1970-01-01 09:", "");

				boolean _final = result.getIsFinal();

				for (SpeechRecognitionAlternative alternative : result.getAlternativesList()) {

					String transcript = alternative.getTranscript();

					float confidence = alternative.getConfidence();

					float score = stability + confidence;

					RecognitionResponse jsonElement = RecognitionResponse.builder().resultEndTime(resultEndTime)._final(_final).transcript(transcript).score(score).build();

					try {
						log.info(jsonElement.toString());
						if (session.isOpen())
							session.sendMessage(new TextMessage(new Gson().toJson(jsonElement), true));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}

		@Override
		public void onError(Throwable t) {
			log.info("onError");
			if (t instanceof OutOfRangeException) {
				log.warn(t.getMessage());
			} else {
				t.printStackTrace();
			}
		}

		@Override
		public void onComplete() {
			log.info("onComplete");
		}

	};

	// The first request in a streaming call has to be a config
	private StreamingRecognizeRequest request = StreamingRecognizeRequest.newBuilder().setStreamingConfig(streamingRecognitionConfig).build();

	@Override
	public void run() {

		try (SpeechClient client = SpeechClient.create()) {
			ClientStream<StreamingRecognizeRequest> clientStream = client.streamingRecognizeCallable().splitCall(responseObserver);
			clientStream.send(request);
			long startTime = System.currentTimeMillis();
			while (true) {
				if (System.currentTimeMillis() - startTime >= STREAMING_LIMIT) {
					clientStream.closeSend();
					referenceToStreamController.cancel();// remove Observer
					clientStream = client.streamingRecognizeCallable().splitCall(responseObserver);
					request = StreamingRecognizeRequest.newBuilder().setStreamingConfig(streamingRecognitionConfig).build();
				} else {
					request = StreamingRecognizeRequest.newBuilder().setAudioContent(sharedQueue.take()).build();
				}
				clientStream.send(request);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
