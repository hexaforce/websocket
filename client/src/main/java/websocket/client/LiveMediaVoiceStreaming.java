package websocket.client;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class LiveMediaVoiceStreaming {

	public void liveMediaVoiceStreaming(AudioInputStream stream) {
		stream = validStream(stream);
		AudioFormat format = stream.getFormat();
		int bufferSize = (int) (stream.getFrameLength() * format.getFrameSize());
		SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, format, bufferSize);
		try {
			play((SourceDataLine) AudioSystem.getLine(info), stream);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void play(SourceDataLine line, AudioInputStream stream) throws LineUnavailableException, IOException {
		line.open(stream.getFormat());
		line.start();
		int numRead = 0;
		byte[] buf = new byte[line.getBufferSize()];
		while ((numRead = stream.read(buf, 0, buf.length)) >= 0) {
			int offset = 0;
			while (offset < numRead) {
				offset += line.write(buf, offset, numRead - offset);
			}
		}
		line.drain();
		line.stop();
	}

	private AudioInputStream validStream(AudioInputStream stream) {
		if (stream.getFormat().getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
			AudioFormat format = stream.getFormat();
			float sampleRate = format.getSampleRate();
			int sampleSizeInBits = format.getSampleSizeInBits() * 2;
			int channels = format.getChannels();
			int frameSize = format.getFrameSize() * 2;
			float frameRate = format.getFrameRate();
			boolean bigEndian = false;
			format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, bigEndian);
			stream = AudioSystem.getAudioInputStream(format, stream);
		}
		return stream;
	}

}