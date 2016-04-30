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
package com.fs.commons.util.mime;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Properties;

public class MimeUtil {

	public static final String UNKNOWN_MIME_TYPE = "application/x-unknown-mime-type";

	// the native byte order of the underlying OS. "BIG" or "little" Endian
	private static ByteOrder nativeByteOrder = ByteOrder.nativeOrder();
	private static Properties mimeTypes = new Properties();

	private static ArrayList<MagicMimeEntry> mMagicMimeEntries = new ArrayList<MagicMimeEntry>();

	/**
	 * @param aStringArray
	 */
	private static void addEntry(final ArrayList<String> aStringArray) {
		try {
			final MagicMimeEntry magicEntry = new MagicMimeEntry(aStringArray);
			mMagicMimeEntries.add(magicEntry);
		} catch (final InvalidMagicMimeEntryException e) {
		}
	}

	/**
	 *
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	private static String getMagicMimeType(final byte[] bytes) throws IOException {
		final int len = mMagicMimeEntries.size();
		for (int i = 0; i < len; i++) {
			final MagicMimeEntry me = mMagicMimeEntries.get(i);
			final String mtype = me.getMatch(bytes);
			if (mtype != null) {
				return mtype;
			}
		}
		return null;
	}

	/**
	 *
	 * @param data
	 * @return
	 */
	public static String getMimeType(final byte[] data) {
		String mimeType = null;
		try {
			mimeType = MimeUtil.getMagicMimeType(data);
		} catch (final Exception e) {
		} finally {
			if (mimeType == null) {
				mimeType = UNKNOWN_MIME_TYPE;
			}
		}
		return mimeType;
	}

	/**
	 *
	 * @param mimeTypes
	 * @param magicStream
	 * @throws IOException
	 */
	public static void init(final InputStream mimeTypes, final InputStream magicStream) throws IOException {
		MimeUtil.mimeTypes.load(mimeTypes);
		parse(new InputStreamReader(magicStream));
	}

	/**
	 *
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(final String[] args) throws FileNotFoundException, IOException {
		// init(GeneralUtility.getFileInputStream("/resources/mime-types.properties"),GeneralUtility.getFileInputStream("/resources/magic.mime")
		// );
		// System.out.println(getMimeType(GeneralUtility.readFile(new
		// File("d:/boy2.jpg"))));
	}

	// Parse the magic.mime file
	private static void parse(final Reader r) throws IOException {
		final BufferedReader br = new BufferedReader(r);
		String line;
		final ArrayList<String> sequence = new ArrayList<String>();

		line = br.readLine();
		while (true) {
			if (line == null) {
				break;
			}
			line = line.trim();
			if (line.length() == 0 || line.charAt(0) == '#') {
				line = br.readLine();
				continue;
			}
			sequence.add(line);

			// read the following lines until a line does not begin with '>' or
			// EOF
			while (true) {
				line = br.readLine();
				if (line == null) {
					addEntry(sequence);
					sequence.clear();
					break;
				}
				line = line.trim();
				if (line.length() == 0 || line.charAt(0) == '#') {
					continue;
				}
				if (line.charAt(0) != '>') {
					addEntry(sequence);
					sequence.clear();
					break;
				}
				sequence.add(line);
			}
		}
		if (!sequence.isEmpty()) {
			addEntry(sequence);
		}
	}
}
