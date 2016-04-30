/*
 * Copyright 2002-2016 Jalal Kiswani.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fs.sound;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.fs.commons.util.GeneralUtility;

import javazoom.jl.player.Player;

public class MP3Player {
	public static void play(final InputStream inputStream) throws SoundException {
		final MP3Player mp3 = new MP3Player(inputStream);
		mp3.play();
	}

	public static void play(final String fileName) throws FileNotFoundException, SoundException {
		final InputStream inputStream = GeneralUtility.getFileInputStream(fileName);
		play(inputStream);
	}

	private String filename;

	private Player player;

	private final InputStream in;

	public MP3Player(final InputStream fileInputStream) {
		this.in = fileInputStream;
	}

	// constructor that takes the name of an MP3 file
	public MP3Player(final String filename) throws FileNotFoundException {
		this(GeneralUtility.getFileInputStream(filename));
		this.filename = filename;

	}

	public void close() {
		if (this.player != null) {
			this.player.close();
		}
	}

	// play the MP3 file to the sound card
	public void play() throws SoundException {
		try {
			final BufferedInputStream bis = new BufferedInputStream(this.in);
			this.player = new Player(bis);
		} catch (final Exception e) {
			throw new SoundException(e);
		}

		// run in new thread to play in background
		new Thread() {
			@Override
			public void run() {
				try {
					MP3Player.this.player.play();
				} catch (final Exception e) {
					System.out.println(e);
				}
			}
		}.start();
	}

}
