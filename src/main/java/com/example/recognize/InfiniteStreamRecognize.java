package com.example.recognize;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.StreamingRecognitionConfig;
import com.google.cloud.speech.v1.StreamingRecognitionResult;
import com.google.cloud.speech.v1.StreamingRecognizeRequest;
import com.google.cloud.speech.v1.StreamingRecognizeResponse;
import com.google.protobuf.ByteString;

public class InfiniteStreamRecognize {

	// ~5 minutes(1 second time lag)
	private static final int STREAMING_LIMIT = 290000;

	private static volatile BlockingQueue<byte[]> sharedQueue = new LinkedBlockingQueue<byte[]>();

	private final Thread microphone = new Thread(new Microphone(sharedQueue));

	public static void main(String[] args) throws Exception {
		new InfiniteStreamRecognize().recognize();
	}

	private final RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder().setEncoding(RecognitionConfig.AudioEncoding.LINEAR16).setLanguageCode("ja-JP").setSampleRateHertz(16000).build();

	private final StreamingRecognitionConfig streamingRecognitionConfig = StreamingRecognitionConfig.newBuilder().setConfig(recognitionConfig).setInterimResults(true).build();

	private StreamController referenceToStreamController;
	private final ResponseObserver<StreamingRecognizeResponse> responseObserver = new ResponseObserver<StreamingRecognizeResponse>() {

		@Override
		public void onStart(StreamController controller) {
			referenceToStreamController = controller;
		}

		@Override
		public void onResponse(StreamingRecognizeResponse response) {

			for (StreamingRecognitionResult result : response.getResultsList()) {
				result.getAllFields().forEach((key, value) -> {
					System.out.println(key + " : " + value);
				});

				for (SpeechRecognitionAlternative alternative : result.getAlternativesList()) {
					alternative.getAllFields().forEach((key, value) -> {
						System.out.println(key + " : " + value);
					});
				}
			}

		}

		@Override
		public void onError(Throwable t) {
			t.printStackTrace();
		}

		@Override
		public void onComplete() {

		}

	};

	// The first request in a streaming call has to be a config
	private StreamingRecognizeRequest request = StreamingRecognizeRequest.newBuilder().setStreamingConfig(streamingRecognitionConfig).build();

	public void recognize() throws Exception {

		try (SpeechClient client = SpeechClient.create()) {

			ClientStream<StreamingRecognizeRequest> clientStream = client.streamingRecognizeCallable().splitCall(responseObserver);
			clientStream.send(request);

			microphone.start();
			long startTime = System.currentTimeMillis();

			while (true) {

				if (System.currentTimeMillis() - startTime >= STREAMING_LIMIT) {

					clientStream.closeSend();

					// remove Observer
					referenceToStreamController.cancel();

					clientStream = client.streamingRecognizeCallable().splitCall(responseObserver);

					request = StreamingRecognizeRequest.newBuilder().setStreamingConfig(streamingRecognitionConfig).build();

				} else {

					ByteString tempByteString = ByteString.copyFrom(sharedQueue.take());

					request = StreamingRecognizeRequest.newBuilder().setAudioContent(tempByteString).build();

				}

				clientStream.send(request);

			}

		}

	}

}
