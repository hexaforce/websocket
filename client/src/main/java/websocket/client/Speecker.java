package websocket.client;

import java.io.File;
import java.io.FileInputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class Speecker implements Runnable {

	private final String uuid;

	@Override
	public void run() {
		
		try {

			File file = new File(uuid);
			FileInputStream fileInputStream = new FileInputStream(file);
			AudioInputStream audioInputStream = new AudioInputStream(fileInputStream, Microphone.audioFormat, file.length());

			Clip clip = AudioSystem.getClip();
			clip.loop(1);
			clip.open(audioInputStream);
			clip.start();

			Microphone.PLAY = true;
			while (clip.isRunning()) {}
			Microphone.PLAY = false;

			log.info("[DELETE] {}", file.getName());
			file.delete();

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
	}

}
