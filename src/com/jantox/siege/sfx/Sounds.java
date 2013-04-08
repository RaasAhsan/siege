package com.jantox.siege.sfx;

import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;


public class Sounds implements Runnable {

	public static final int SAMPLE_RATE = 44100;

	public static Sounds sound = new Sounds();

	public static boolean muted = false;
	private ArrayList<Sound> sounds;

	private SourceDataLine dataline = null;

	public Sounds() {
		sounds = new ArrayList<Sound>();
		try {
			AudioFormat aformat = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);
			dataline = AudioSystem.getSourceDataLine(aformat);
			dataline.open(aformat, SAMPLE_RATE / 10);
			dataline.start();

			Thread thread = new Thread(this);
			thread.setDaemon(true);
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addSound(Sound sound) {
		synchronized (sounds) {
			sounds.add(sound);
		}
	}

	public static void mute() {
		muted = !muted;
	}

	public static void play(Sound sound) {
		Sounds.sound.addSound(sound);
	}

	@Override
	public void run() {
		int bufferSize = 4096;
		int[] buffer = new int[4096];
		byte[] outBuffer = new byte[4096];
		while (true) {
			while (dataline.available() < bufferSize) {
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
				}
			}

			int toRead = dataline.available();
			if (toRead > bufferSize)
				toRead = bufferSize;

			synchronized (sounds) {
				for (int i = 0; i < sounds.size(); i++) {
					if (!sounds.get(i).read(buffer, toRead)) {
						sounds.remove(i--);
					}
				}
			}

			int val = 0;
			for (int i = 0; i < toRead; i++) {
				if (!muted) {
					val = buffer[i];
					if (val < -128)
						val = -128;
					if (val > 127)
						val = 127;
				}

				buffer[i] = 0;
				outBuffer[i] = (byte) val;
			}
			dataline.write(outBuffer, 0, toRead);
		}
	}

}
