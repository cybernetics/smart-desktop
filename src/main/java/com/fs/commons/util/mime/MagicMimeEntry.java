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

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Vector;

import com.fs.commons.logging.Logger;

/*
 * A single MagicMime entry from the magic.mime file. This entry can contain
 * subentries; so it recursivelyincludes itself, if subentries are found.
 * Basically this class represents a node in a simple n-ary tree
 *
 * TODO:
 *  o   More commenting
 *  o   Testing lelong, leshort, byte
 *  o   Method stringWithEscapeSubstitutions to support more escape sequences
 *  o   Its a problem if the content has spaces (eg., "#!\ /bin/bash"). This needs
 *      to be fixed
 *  o   Is any operation other equality on the contents supported?
 *      there are entries in the magic file where what seemed like a greater
 *      than operator is supported. eg.,
 *      ">85     byte&0x01       >0      \b, zoomed"
 *      but such entries are commented out in magic.mime file.
 *
 */

public class MagicMimeEntry {

	public static final int STRING_TYPE = 1;
	public static final int BELONG_TYPE = 2;
	public static final int SHORT_TYPE = 3;
	public static final int LELONG_TYPE = 4;
	public static final int BESHORT_TYPE = 5;
	public static final int LESHORT_TYPE = 6;
	public static final int BYTE_TYPE = 7;
	public static final int UNKNOWN_TYPE = 20;

	/*
	 * when bytes are read from the magic.mime file, the readers in java will
	 * read escape sequences as regular bytes. That is, a sequence like \040
	 * (represengint ' ' - space character) will be read as a backslash followed
	 * by a zero, four and zero -- 4 different bytes and not a single byte
	 * representing space. This method parses the string and converts the
	 * sequence of bytes representing escape sequence to a single byte
	 * 
	 * NOTE: not all regular escape sequences are added yet. add them, if you
	 * don't find one here
	 */
	private static String stringWithEscapeSubstitutions(final String s) {
		final StringBuffer ret = new StringBuffer();
		final int len = s.length();
		int indx = 0;
		int c;
		while (indx < len) {
			c = s.charAt(indx);
			if (c == '\n') {
				break;
			}

			if (c == '\\') {
				indx++;
				if (indx >= len) {
					ret.append((char) c);
					break;
				}

				int cn = s.charAt(indx);

				if (cn == '\\') {
					ret.append('\\');
				} else if (cn == ' ') {
					ret.append(' ');
				} else if (cn == 't') {
					ret.append('\t');
				} else if (cn == 'n') {
					ret.append('\n');
				} else if (cn == 'r') {
					ret.append('\r');
				} else if (cn >= '\60' && cn <= '\67') {
					int escape = cn - '0';
					indx++;
					if (indx >= len) {
						ret.append((char) escape);
						break;
					}
					cn = s.charAt(indx);
					if (cn >= '\60' && cn <= '\67') {
						escape = escape << 3;
						escape = escape | cn - '0';

						indx++;
						if (indx >= len) {
							ret.append((char) escape);
							break;
						}
						cn = s.charAt(indx);
						if (cn >= '\60' && cn <= '\67') {
							escape = escape << 3;
							escape = escape | cn - '0';
						} else {
							indx--;
						}
					} else {
						indx--;
					}
					ret.append((char) escape);
				} else {
					ret.append((char) cn);
				}
			} else {
				ret.append((char) c);
			}
			indx++;
		}
		return new String(ret);
	}

	private final ArrayList<MagicMimeEntry> subEntries = new ArrayList<MagicMimeEntry>();
	int checkBytesFrom;
	int type;
	String typeStr;
	String content;
	String mimeType;
	String mimeEnc;

	MagicMimeEntry parent;

	boolean isBetween;

	public MagicMimeEntry(final ArrayList<?> entries) throws InvalidMagicMimeEntryException {

		this(0, null, entries);
	}

	private MagicMimeEntry(final int level, final MagicMimeEntry parent, final ArrayList<?> entries) throws InvalidMagicMimeEntryException {

		if (entries == null || entries.size() == 0) {
			return;
		}
		try {
			addEntry((String) entries.get(0));
		} catch (final Exception e) {
			throw new InvalidMagicMimeEntryException(entries);
		}
		entries.remove(0);
		this.parent = parent;
		if (parent != null) {
			parent.subEntries.add(this);
		}

		while (entries.size() > 0) {
			final int thisLevel = howManyGreaterThans((String) entries.get(0));
			if (thisLevel > level) {
				new MagicMimeEntry(thisLevel, this, entries);
			} else {
				break;
			}
		}
	}

	// There are problems with the magic.mime file. It seems that some of the
	// fields
	// are space deliniated and not tab deliniated as defined in the spec.
	// We will attempt to handle the case for space deliniation here so that we
	// can parse
	// as much of the file as possible. Currently about 70 entries are incorrect
	void addEntry(final String aLine) {
		final String trimmed = aLine.replaceAll("^>*", "");
		String[] tokens = trimmed.split("\t");

		// Now strip the empty entries
		final Vector<String> v = new Vector<String>();
		for (int i = 0; i < tokens.length; i++) {
			if (!"".equals(tokens[i])) {
				v.add(tokens[i]);
			}
		}
		tokens = new String[v.size()];
		tokens = v.toArray(tokens);

		if (tokens.length > 0) {
			final String tok = tokens[0].trim();
			try {
				if (tok.startsWith("0x")) {
					this.checkBytesFrom = Integer.parseInt(tok.substring(2), 16);
				} else {
					this.checkBytesFrom = Integer.parseInt(tok);
				}
			} catch (final NumberFormatException e) {
				// We could have a space delinitaed entry so lets try to handle
				// this anyway
				addEntry(trimmed.replaceAll("  ", "\t"));
				return;
			}
		}
		if (tokens.length > 1) {
			this.typeStr = tokens[1].trim();
			this.type = getType(this.typeStr);
		}
		if (tokens.length > 2) {
			// We don't trim the content
			this.content = ltrim(tokens[2]);
			this.content = stringWithEscapeSubstitutions(this.content);
		}
		if (tokens.length > 3) {
			this.mimeType = tokens[3].trim();
		}
		if (tokens.length > 4) {
			this.mimeEnc = tokens[4].trim();
		}
	}

	public int getCheckBytesFrom() {
		return this.checkBytesFrom;
	}

	public String getContent() {
		return this.content;
	}

	public String getMatch(final byte[] content) throws IOException {
		final ByteBuffer buf = readBuffer(content);
		if (buf == null) {
			return null;
		}
		buf.position(0);
		final boolean matches = match(buf);
		if (matches) {
			final int subLen = this.subEntries.size();
			final String myMimeType = getMimeType();
			if (subLen > 0) {
				String mtype = null;
				for (int k = 0; k < subLen; k++) {
					final MagicMimeEntry me = this.subEntries.get(k);
					mtype = me.getMatch(content);
					if (mtype != null) {
						return mtype;
					}
				}
				if (myMimeType != null) {
					return myMimeType;
				}
			} else {
				return myMimeType;
			}
		}

		return null;
	}

	public String getMimeType() {
		return this.mimeType;
	}

	public int getType() {
		return this.type;
	}

	private int getType(final String tok) {
		if (tok.startsWith("string")) {
			return STRING_TYPE;
		} else if (tok.startsWith("belong")) {
			return BELONG_TYPE;
		} else if (tok.equals("short")) {
			return SHORT_TYPE;
		} else if (tok.startsWith("lelong")) {
			return LELONG_TYPE;
		} else if (tok.startsWith("beshort")) {
			return BESHORT_TYPE;
		} else if (tok.startsWith("leshort")) {
			return LESHORT_TYPE;
		} else if (tok.equals("byte")) {
			return BYTE_TYPE;
		}

		return UNKNOWN_TYPE;
	}

	private int howManyGreaterThans(final String aLine) {
		if (aLine == null) {
			return -1;
		}
		int i = 0;
		final int len = aLine.length();
		while (i < len) {
			if (aLine.charAt(i) == '>') {
				i++;
			} else {
				break;
			}
		}
		return i;
	}

	private String ltrim(final String s) {
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != ' ') {
				return s.substring(i);
			}
		}
		return s;
	}

	private boolean match(final ByteBuffer buf) throws IOException {
		boolean matches = true;
		switch (getType()) {
		case MagicMimeEntry.STRING_TYPE: {
			matches = matchString(buf);
			break;
		}

		case MagicMimeEntry.SHORT_TYPE: {
			matches = matchShort(buf, ByteOrder.BIG_ENDIAN, false, (short) 0xFF);
			break;
		}

		case MagicMimeEntry.LESHORT_TYPE:
		case MagicMimeEntry.BESHORT_TYPE: {
			ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
			if (getType() == MagicMimeEntry.LESHORT_TYPE) {
				byteOrder = ByteOrder.LITTLE_ENDIAN;
			}
			boolean needMask = false;
			short sMask = 0xFF;
			final int indx = this.typeStr.indexOf('&');
			if (indx >= 0) {
				sMask = (short) Integer.parseInt(this.typeStr.substring(indx + 3), 16);
				needMask = true;
			} else if (getContent().startsWith("&")) {
				sMask = (short) Integer.parseInt(getContent().substring(3), 16);
				needMask = true;
			}
			matches = matchShort(buf, byteOrder, needMask, sMask);
			break;
		}

		case MagicMimeEntry.LELONG_TYPE:
		case MagicMimeEntry.BELONG_TYPE: {
			ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
			if (getType() == MagicMimeEntry.LELONG_TYPE) {
				byteOrder = ByteOrder.LITTLE_ENDIAN;
			}
			boolean needMask = false;
			long lMask = 0xFFFFFFFF;
			final int indx = this.typeStr.indexOf('&');
			if (indx >= 0) {
				lMask = Long.parseLong(this.typeStr.substring(indx + 3), 16);
				needMask = true;
			} else if (getContent().startsWith("&")) {
				lMask = Long.parseLong(getContent().substring(3), 16);
				needMask = true;
			}
			matches = matchLong(buf, byteOrder, needMask, lMask);
			break;
		}

		case MagicMimeEntry.BYTE_TYPE: {
			matches = matchByte(buf);
		}

		default: {
			matches = false;
			break;
		}
		}
		return matches;
	}

	// public String getMatch(RandomAccessFile raf) throws IOException {
	// ByteBuffer buf = readBuffer(raf);
	// if (buf == null)
	// return null;
	// boolean matches = match(buf);
	// if (matches) {
	// String myMimeType = getMimeType();
	// if (subEntries.size() > 0) {
	// String mtype = null;
	// for (int i=0; i<subEntries.size(); i++) {
	// MagicMimeEntry me = (MagicMimeEntry) subEntries.get(i);
	// mtype = me.getMatch(raf);
	// if (mtype != null) {
	// return mtype;
	// }
	// }
	// if (myMimeType != null) {
	// return myMimeType;
	// }
	// } else {
	// return myMimeType;
	// }
	// }
	//
	// return null;
	// }

	private boolean matchByte(final ByteBuffer bbuf) throws IOException {
		final byte b = bbuf.get(0);
		return b == getContent().charAt(0);
	}

	private boolean matchLong(final ByteBuffer bbuf, final ByteOrder bo, final boolean needMask, final long lMask) throws IOException {
		bbuf.order(bo);
		long got;
		final String testContent = getContent();
		if (testContent.startsWith("0x")) {
			got = Long.parseLong(testContent.substring(2), 16);
		} else if (testContent.startsWith("&")) {
			got = Long.parseLong(testContent.substring(3), 16);
		} else {
			got = Long.parseLong(testContent);
		}

		long found = bbuf.getInt();

		if (needMask) {
			found = (short) (found & lMask);
		}

		if (got != found) {
			return false;
		}

		return true;
	}

	/*
	 * private methods used for matching differet types
	 */

	private boolean matchShort(final ByteBuffer bbuf, final ByteOrder bo, final boolean needMask, final short sMask) throws IOException {
		bbuf.order(bo);
		short got;
		final String testContent = getContent();
		if (testContent.startsWith("0x")) {
			got = (short) Integer.parseInt(testContent.substring(2), 16);
		} else if (testContent.startsWith("&")) {
			got = (short) Integer.parseInt(testContent.substring(3), 16);
		} else {
			got = (short) Integer.parseInt(testContent);
		}

		short found = bbuf.getShort();

		if (needMask) {
			found = (short) (found & sMask);
		}

		if (got != found) {
			return false;
		}

		return true;
	}

	private boolean matchString(final ByteBuffer bbuf) throws IOException {
		if (this.isBetween) {
			final String buffer = new String(bbuf.array());
			if (buffer.contains(getContent())) {
				return true;
			}
			return false;
		}
		final int read = getContent().length();
		for (int j = 0; j < read; j++) {
			if ((bbuf.get(j) & 0xFF) != getContent().charAt(j)) {
				return false;
			}
		}
		return true;
	}

	/*
	 * private methods for reading to local buffer
	 */
	private ByteBuffer readBuffer(final byte[] content) throws IOException {
		final int startPos = getCheckBytesFrom();
		if (startPos > content.length) {
			return null;
		}

		ByteBuffer buf;
		switch (getType()) {
		case MagicMimeEntry.STRING_TYPE: {
			final int len = getContent().length();
			buf = ByteBuffer.allocate(len + 1);
			buf.put(content, startPos, len);
			break;
		}

		case MagicMimeEntry.SHORT_TYPE:
		case MagicMimeEntry.LESHORT_TYPE:
		case MagicMimeEntry.BESHORT_TYPE: {
			buf = ByteBuffer.allocate(2);
			buf.put(content, startPos, 2);
			break;
		}

		case MagicMimeEntry.LELONG_TYPE:
		case MagicMimeEntry.BELONG_TYPE: {
			buf = ByteBuffer.allocate(4);
			buf.put(content, startPos, 4);
			break;
		}

		case MagicMimeEntry.BYTE_TYPE: {
			buf = ByteBuffer.allocate(1);
			buf.put(buf.array(), startPos, 1);
		}

		default: {
			buf = null;
			break;
		}
		}
		return buf;
	}

	@SuppressWarnings("unused")
	private ByteBuffer readBuffer(final RandomAccessFile raf) throws IOException {
		final int startPos = getCheckBytesFrom();
		if (startPos > raf.length()) {
			return null;
		}
		raf.seek(startPos);
		ByteBuffer buf;
		switch (getType()) {
		case MagicMimeEntry.STRING_TYPE: {
			int len = 0;
			// Lets check if its a between test
			final int index = this.typeStr.indexOf(">");
			if (index != -1) {
				len = Integer.parseInt(this.typeStr.substring(index + 1, this.typeStr.length() - 1));
				this.isBetween = true;
			} else {
				len = getContent().length();
			}
			buf = ByteBuffer.allocate(len + 1);
			raf.read(buf.array(), 0, len);
			break;
		}

		case MagicMimeEntry.SHORT_TYPE:
		case MagicMimeEntry.LESHORT_TYPE:
		case MagicMimeEntry.BESHORT_TYPE: {
			buf = ByteBuffer.allocate(2);
			raf.read(buf.array(), 0, 2);
			break;
		}

		case MagicMimeEntry.LELONG_TYPE:
		case MagicMimeEntry.BELONG_TYPE: {
			buf = ByteBuffer.allocate(4);
			raf.read(buf.array(), 0, 4);
			break;
		}

		case MagicMimeEntry.BYTE_TYPE: {
			buf = ByteBuffer.allocate(1);
			raf.read(buf.array(), 0, 1);
		}

		default: {
			buf = null;
			break;
		}
		}
		return buf;
	}

	@Override
	public String toString() {
		return "MimeMagicType: " + this.checkBytesFrom + ", " + this.type + ", " + this.content + ", " + this.mimeType + ", " + this.mimeEnc;
	}

	public void traverseAndPrint(final String tabs) {
		Logger.info(tabs + toString());
		final int len = this.subEntries.size();
		for (int i = 0; i < len; i++) {
			final MagicMimeEntry me = this.subEntries.get(i);
			me.traverseAndPrint(tabs + "\t");
		}
	}
}
