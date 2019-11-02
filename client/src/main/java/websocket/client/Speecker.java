package websocket.client;

import java.io.File;
import java.io.FileInputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Speecker implements Runnable {

	private final String uuid;
	private final int bufferedSize;

	@Override
	public void run() {
		try {
			AudioInputStream audioInputStream = new AudioInputStream(new FileInputStream(new File(uuid)), Microphone.audioFormat, bufferedSize);
			Line line = AudioSystem.getLine(Microphone.targetInfo);
			Clip clip = (Clip) line;
			clip.open(audioInputStream);
			clip.start();
			Thread.sleep(3000);
//			file.delete();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

}
