package com.fs.sound;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javazoom.jl.player.Player;

import com.fs.commons.util.GeneralUtility;

public class MP3Player {
	private String filename;
	private Player player;
	private InputStream in;

	// constructor that takes the name of an MP3 file
	public MP3Player(String filename) throws FileNotFoundException {
		this(GeneralUtility.getFileInputStream(filename));
		this.filename = filename;

	}

	public MP3Player(InputStream fileInputStream) {
		this.in = fileInputStream;
	}

	public void close() {
		if (player != null)
			player.close();
	}

	// play the MP3 file to the sound card
	public void play() throws SoundException {
		try {
			BufferedInputStream bis = new BufferedInputStream(in);
			player = new Player(bis);
		} catch (Exception e) {
			throw new SoundException(e);
		}

		// run in new thread to play in background
		new Thread() {
			public void run() {
				try {
					player.play();
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}.start();
	}

	public static void play(String fileName) throws FileNotFoundException, SoundException {
		InputStream inputStream = GeneralUtility.getFileInputStream(fileName);
		play(inputStream);
	}

	public static void play(InputStream inputStream) throws SoundException {
		MP3Player mp3 = new MP3Player(inputStream);
		mp3.play();
	}



}
