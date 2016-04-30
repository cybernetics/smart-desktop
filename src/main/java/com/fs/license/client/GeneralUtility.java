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
package com.fs.license.client;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Properties;

public class GeneralUtility {
	/**
	 *
	 * @param fileName
	 *            String
	 * @return
	 * @throws IOException
	 */
	public static Process executeFile(final String fileName) throws IOException {
		return Runtime.getRuntime().exec("cmd /c \"" + fileName + "\"");
	}

	/**
	 *
	 * @return
	 */
	public static String getApplicationPath() {
		final Class<Integer> c = Integer.class;
		final String className = c.getName();
		final String resourceName = "/" + className.replace('.', '/') + ".class";
		final URL location = c.getResource(resourceName);
		final File file = new File(location.getFile());
		return file.getAbsolutePath();
	}

	/**
	 *
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream getFileInputStream(final String fileName) throws FileNotFoundException {
		if (Integer.class.getResource(fileName) != null) {
			final InputStream in = Integer.class.getResourceAsStream(fileName);
			if (in == null) {
				throw new FileNotFoundException(fileName);
			}
			return in;
		}
		return new FileInputStream(fileName);
	}

	/**
	 *
	 * @return java.lang.String
	 * @throws IOException
	 * @param fileName
	 *            String
	 */
	public static String getFileText(final String fileName) throws IOException {
		BufferedReader in;
		if (Class.class.getResource(fileName) != null) {
			in = new BufferedReader(new InputStreamReader(Class.class.getResourceAsStream(fileName)));
		} else {
			in = new BufferedReader(new FileReader(new File(fileName)));
		}

		final StringBuffer buf = new StringBuffer();
		String str;
		while ((str = in.readLine()) != null) {
			buf.append(str + "\n");
		}
		in.close();
		return new String(buf.toString().getBytes(), "UTF-8");
	}

	/**
	 *
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getLocalHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (final UnknownHostException e) {
			// Unable to get host name
			System.err.println(new HashUtil().deHash("85-110-97-98-108-101-32-116-111-32-103-101-116-32-104-111-115-116-32-110-97-109-101-"));
			return "";
		}
	}

	/**
	 *
	 * @param fileName
	 *            String
	 * @return InputStream
	 */
	public static URL getURL(final String fileName) {
		return Integer.class.getResource(fileName);
	}

	public static void main(final String[] args) {
		// System.out.println(System.getProperty("user.dir"));
	}

	/**
	 *
	 * @param host
	 * @param port
	 * @return
	 */
	public static boolean pingServer(final String host, final int port) {
		boolean result = true;
		try {
			new Socket(host, port);
		} catch (final UnknownHostException ex) {
			result = false;
			ex.printStackTrace();
		} catch (final IOException ex) {
			result = false;
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 *
	 * @param file
	 *            File
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] readFile(final File file) throws IOException {
		return readStream(new FileInputStream(file), (int) file.length());
	}

	/**
	 *
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(final InputStream in) throws IOException {
		try {
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			return out.toByteArray();
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	/**
	 *
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static Properties readPropertyStream(final InputStream in) throws IOException {
		try {
			final Properties prop = new Properties();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			// BufferedReader reader = new BufferedReader(new
			// FileReader(LABLES_FILE_NAME));
			String line;
			while ((line = reader.readLine()) != null) {
				line = new String(line.getBytes());
				final int lastIndexOfEqual = line.lastIndexOf('=');
				// String[] arr = line.trim().split("=");
				if (lastIndexOfEqual != -1 && lastIndexOfEqual != line.length()) {
					prop.setProperty(line.substring(0, lastIndexOfEqual).trim().toUpperCase(), line.substring(lastIndexOfEqual + 1).trim());
				}
			}
			reader.close();
			return prop;
		} catch (final IOException e) {
			throw e;
		}
	}

	/**
	 *
	 * @param inStream
	 *            InputStream
	 * @return String
	 * @throws IOException
	 */
	public static byte[] readStream(final InputStream inStream, final int size) throws IOException {
		DataInputStream in = null;
		try {
			in = new DataInputStream(inStream);
			final byte arr[] = new byte[size];
			in.readFully(arr);
			return arr;
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	/**
	 *
	 * @param data
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static File writeDataToFile(final byte[] data, final File file) throws FileNotFoundException, IOException {
		final FileOutputStream out = new FileOutputStream(file);
		out.write(data);
		out.close();
		return file;
	}

	/**
	 *
	 * @return File
	 * @param data
	 *            String
	 * @param suffix
	 *            String
	 * @throws IOException
	 */
	public static File writeDataToTempFile(final byte[] data, final String suffix) throws IOException {
		final File file = File.createTempFile("fs-", suffix);
		return writeDataToFile(data, file);
	}

	/**
	 *
	 * @return File
	 * @param data
	 *            String
	 * @throws IOException
	 */
	public static File writeDataToTempFile(final String data, final String ext) throws IOException {
		final File file = File.createTempFile("jksoft", "." + ext);
		final PrintWriter out = new PrintWriter(new FileOutputStream(file));
		out.print(data);
		out.close();
		return file;
	}

}