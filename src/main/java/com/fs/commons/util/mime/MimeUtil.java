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
	 * 
	 * @param mimeTypes
	 * @param magicStream
	 * @throws IOException
	 */
	public static void init(InputStream mimeTypes, InputStream magicStream)throws IOException {
		MimeUtil.mimeTypes.load(mimeTypes);
		parse(new InputStreamReader(magicStream));
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	public static String getMimeType(byte[] data) {
		String mimeType = null;
		try {
			mimeType = MimeUtil.getMagicMimeType(data);
		} catch (Exception e) {
		} finally {
			if (mimeType == null) {
				mimeType = UNKNOWN_MIME_TYPE;
			}
		}
		return mimeType;
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	private static String getMagicMimeType(byte[] bytes) throws IOException {
		int len = mMagicMimeEntries.size();
		for (int i = 0; i < len; i++) {
			MagicMimeEntry me = (MagicMimeEntry) mMagicMimeEntries.get(i);
			String mtype = me.getMatch(bytes);
			if (mtype != null) {
				return mtype;
			}
		}
		return null;
	}

	// Parse the magic.mime file
	private static void parse(Reader r) throws IOException {
		BufferedReader br = new BufferedReader(r);
		String line;
		ArrayList<String> sequence = new ArrayList<String>();

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

	/**
	 * @param aStringArray
	 */
	private static void addEntry(ArrayList<String> aStringArray) {
		try {
			MagicMimeEntry magicEntry = new MagicMimeEntry(aStringArray);
			mMagicMimeEntries.add(magicEntry);
		} catch (InvalidMagicMimeEntryException e) {
		}
	}
	
	/**
	 * 
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
//		init(GeneralUtility.getFileInputStream("/resources/mime-types.properties"),GeneralUtility.getFileInputStream("/resources/magic.mime") );
//		System.out.println(getMimeType(GeneralUtility.readFile(new File("d:/boy2.jpg"))));		
	}
}
