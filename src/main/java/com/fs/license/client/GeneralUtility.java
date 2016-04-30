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
	 * @param file
	 *            File
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] readFile(File file) throws IOException {
		return readStream(new FileInputStream(file),(int)file.length());
	}

	/**
	 * 
	 * @param inStream
	 *            InputStream
	 * @return String
	 * @throws IOException
	 */
	public static byte[] readStream(InputStream inStream, int size) throws IOException {
		DataInputStream in = null;
		try {
			in = new DataInputStream(inStream);
			byte arr[] = new byte[size];
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
	 * @return java.lang.String
	 * @throws IOException
	 * @param fileName
	 *            String
	 */
	public static String getFileText(String fileName) throws IOException {
		BufferedReader in;
		if (Class.class.getResource(fileName) != null) {
			in = new BufferedReader(new InputStreamReader(Class.class.getResourceAsStream(fileName)));
		} else {
			in = new BufferedReader(new FileReader(new File(fileName)));
		}

		StringBuffer buf = new StringBuffer();
		String str;
		while ((str = in.readLine()) != null) {
			buf.append(str + "\n");
		}
		in.close();
		return new String(buf.toString().getBytes(), "UTF-8");
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream getFileInputStream(String fileName) throws FileNotFoundException {
		if (Integer.class.getResource(fileName) != null) {
			InputStream in = Integer.class.getResourceAsStream(fileName);
			if (in == null) {
				throw new FileNotFoundException(fileName);
			}
			return in;
		}
		return new FileInputStream(fileName);
	}

	/**
	 * 
	 * @param fileName
	 *            String
	 * @return InputStream
	 */
	public static URL getURL(String fileName) {
		return Integer.class.getResource(fileName);
	}

	/**
	 * 
	 * @return File
	 * @param data
	 *            String
	 * @throws IOException
	 */
	public static File writeDataToTempFile(String data, String ext) throws IOException {
		File file = File.createTempFile("jksoft", "." + ext);
		PrintWriter out = new PrintWriter(new FileOutputStream(file));
		out.print(data);
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
	public static File writeDataToTempFile(byte[] data, String suffix) throws IOException {
		File file = File.createTempFile("fs-", suffix);
		return writeDataToFile(data, file);
	}

	/**
	 * 
	 * @param data
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static File writeDataToFile(byte[] data, File file) throws FileNotFoundException, IOException {
		FileOutputStream out = new FileOutputStream(file);
		out.write(data);
		out.close();
		return file;
	}

	/**
	 * 
	 * @param fileName
	 *            String
	 * @return
	 * @throws IOException
	 */
	public static Process executeFile(String fileName) throws IOException {
		return Runtime.getRuntime().exec("cmd /c \"" + fileName + "\"");
	}


	/**
	 * 
	 * @param host
	 * @param port
	 * @return
	 */
	public static boolean pingServer(String host, int port) {
		boolean result = true;
		try {
			new Socket(host, port);
		} catch (UnknownHostException ex) {
			result = false;
			ex.printStackTrace();
		} catch (IOException ex) {
			result = false;
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @return
	 */
	public static String getApplicationPath() {
		Class<Integer> c = Integer.class;
		String className = c.getName();
		String resourceName = "/" + className.replace('.', '/') + ".class";
		URL location = c.getResource(resourceName);
		File file = new File(location.getFile());
		return file.getAbsolutePath();
	}
	
	public static void main(String[] args) {
		//System.out.println(System.getProperty("user.dir"));
	}

	/**
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream in) throws IOException {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
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
	public static Properties readPropertyStream(InputStream in) throws IOException {
		try {
			Properties prop = new Properties();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			// BufferedReader reader = new BufferedReader(new
			// FileReader(LABLES_FILE_NAME));
			String line;
			while ((line = reader.readLine()) != null) {
				line = new String(line.getBytes());
				int lastIndexOfEqual = line.lastIndexOf('=');
				// String[] arr = line.trim().split("=");
				if (lastIndexOfEqual != -1 && lastIndexOfEqual != line.length()) {
					prop.setProperty(line.substring(0, lastIndexOfEqual).trim().toUpperCase(), line.substring(lastIndexOfEqual + 1).trim());
				}
			}
			reader.close();
			return prop;
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getLocalHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			//Unable to get host name
			System.err.println(new HashUtil().deHash("85-110-97-98-108-101-32-116-111-32-103-101-116-32-104-111-115-116-32-110-97-109-101-"));
			return "";
		}
	}
	
}