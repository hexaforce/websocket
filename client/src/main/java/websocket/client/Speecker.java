package websocket.client;

import java.io.File;
import java.io.FileInputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Speecker implements Runnable {

	private final String uuid;

	@Override
	public void run() {

		System.out.println(uuid);

		try {

			File file = new File(uuid);

			FileInputStream fileInputStream = new FileInputStream(file);

			AudioInputStream audioInputStream = new AudioInputStream(fileInputStream, Microphone.audioFormat, file.length());

			Clip clip = AudioSystem.getClip();

			clip.open(audioInputStream);

			clip.start();

			Thread.sleep(3000);

			file.delete();

		} catch (Exception e) {

			System.err.println(e.getMessage());

		}
	}

}
