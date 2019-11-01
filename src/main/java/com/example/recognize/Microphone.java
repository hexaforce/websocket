package com.example.recognize;

import static java.lang.System.out;

import java.util.concurrent.BlockingQueue;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Microphone implements Runnable {

	final BlockingQueue<byte[]> sharedQueue;

	// SampleRate:16000Hz, SampleSizeInBits: 16, Number of channels: 1, Signed: true, bigEndian: false
	final AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false);

	// Set the system information to read from the microphone audio stream
	final DataLine.Info targetInfo = new Info(TargetDataLine.class, audioFormat);

	// buffer size in bytes
	final int BYTES_PER_BUFFER = 6400;

	@Override
	public void run() {
		try {
			if (!AudioSystem.isLineSupported(targetInfo)) {
				out.println("Microphone not supported");
				return;
			}
			TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
			targetDataLine.open(audioFormat);
			targetDataLine.start();
			byte[] data = new byte[BYTES_PER_BUFFER];
			while (targetDataLine.isOpen()) {
				try {
					int numBytesRead = targetDataLine.read(data, 0, data.length);
					if ((numBytesRead <= 0) && (targetDataLine.isOpen())) {
						continue;
					}
					sharedQueue.put(data.clone());
				} catch (InterruptedException e) {
					out.println("Microphone input buffering interrupted : " + e.getMessage());
				}
			}
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}

}
