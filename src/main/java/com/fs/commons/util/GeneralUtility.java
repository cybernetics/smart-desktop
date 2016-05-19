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
package com.fs.commons.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.fs.commons.application.exceptions.DatabaseDownException;
import com.fs.commons.application.exceptions.ServerDownException;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.DaoFactory;
import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.Record;
import com.jk.exceptions.handler.JKExceptionUtil;
import com.jk.logging.JKLogger;
import com.jk.resources.JKResourceLoaderFactory;
import com.jk.security.JKEncDec;
import com.lowagie.text.pdf.codec.Base64;

class FakeRunnable implements Runnable {
	public FakeRunnable(final Object object) {
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000000);
			} catch (final InterruptedException e) {
			}
		}
	}

}

public class GeneralUtility {
	/**
	 *
	 * @author Administrator
	 *
	 */
	static class HashComparator implements Comparator<Object> {
		private final boolean asc;

		public HashComparator(final boolean asc) {
			this.asc = asc;
		}

		@Override
		public int compare(final Object obj1, final Object obj2) {
			int result = 0;
			final Map.Entry e1 = (Map.Entry) obj1;
			final Map.Entry e2 = (Map.Entry) obj2;// Sort based on values.
			final Comparable<Comparable<Comparable>> value1 = (Comparable<Comparable<Comparable>>) e1.getValue();
			final Comparable<Comparable> value2 = (Comparable<Comparable>) e2.getValue();
			if (value1.compareTo(value2) == 0) {
				final Comparable<Comparable<?>> key1 = (Comparable<Comparable<?>>) e1.getKey();
				final Comparable<?> key2 = (Comparable<?>) e2.getKey();
				// Sort String in an alphabetical order
				result = key1.compareTo(key2);
			} else {
				if (this.asc) {
					result = value1.compareTo(value2);
				} else {
					result = value2.compareTo(value1);
				}
			}
			return result;
		}
	}

	private static final String USER_LOCAL_PATH = System.getProperty("user.home") + System.getProperty("file.separator") + "final-solutions";
	private static Hashtable<String, ImageIcon> icons = new Hashtable<String, ImageIcon>();

	public static Locale Locale = new Locale("EN", "US");

	static SecretKeySpec spec = new SecretKeySpec("123456781234567812345678".getBytes(), "DESEDE");

	// ////////////////////////
	private static String FILE_READ_HARDDISK_SERIAL = "diskid32.exe";

	/**
	 * @description : to add autocommit false in the first on the sql file and
	 *              commit in the end , because im using random access file , to
	 *              make operation faster , i faced many problems in it like the
	 *              random access have not append method it over writes on the
	 *              existing text ,so i replace the first statement of the sql
	 *              which is comment file in enters then write the auto commit
	 *
	 * @author Mohamed Kiswani
	 * @since 28-1-2010
	 * @throws IOException
	 *
	 */
	public static void addAutocommitFalse(final String path) throws IOException {
		final File file = new File(path);
		writeOnFile(file, "\n", 0);
		writeOnFile(file, "\n", 0);
		writeOnFile(file, "\n", 0);
		writeOnFile(file, "SET AUTOCOMMIT=0;\n\n", 0);
		writeOnFile(file, "\nCOMMIT;", file.length());

	}

	/**
	 *
	 * @param bytes
	 * @param path
	 * @throws IOException
	 */
	public static void appedDataToFile(final byte[] bytes, final String path) throws IOException {
		final FileOutputStream appendedFile = new FileOutputStream(path, true);
		appendedFile.write(bytes);
		appendedFile.close();

	}

	/**
	 *
	 * @param databaseName
	 * @param databaseHost
	 * @param databasePort
	 * @throws DatabaseDownException
	 */
	public static void checkDatabaseServer(final String databaseName, final String databaseHost, final int databasePort)
			throws DatabaseDownException {
		try {
			checkServer(databaseHost, databasePort);
		} catch (final ServerDownException e) {
			throw new DatabaseDownException((Exception) e.getCause(), databaseName, databaseHost, databasePort);
		}
	}

	// ////////////////////////////////////////////////////////////////////
	public static File checkFolderPath(final String path, final boolean create) {
		final File file = new File(path);
		if (!file.exists()) {
			if (create) {
				file.mkdir();
			}
			return null;
		}
		return file;
	}

	public static void checkServer(final String host, final int port) throws ServerDownException {
		try {
			new Socket(host, port);
		} catch (final IOException ex) {
			throw new ServerDownException(ex, host, port);
		}
	}

	/**
	 *
	 */
	public static void clearTempFiles() {
		final String userFolderPath = getUserFolderPath(false);
		final File file = new File(userFolderPath);
		deleteDir(file);
	}

	public static <T> T cloneBean(final Object bean) {
		try {
			return (T) BeanUtils.cloneBean(bean);
		} catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	public static Time convertDateToTime(final Date date) {
		final Time time = new Time(date.getTime());
		time.setSeconds(0);
		return time;
	}

	/**
	 *
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public static Object copy(final Object source) {
		try {

			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(source);
			final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			final ObjectInputStream ois = new ObjectInputStream(bais);
			final Object deepCopy = ois.readObject();
			return deepCopy;
		} catch (final Exception e) {
			// ExceptionUtil.handle(e);
			System.err.println("Failed to copy object of class : " + source.getClass().getName() + " / " + e.getMessage());
			e.printStackTrace();
			return null;// unreachable
		}
	}

	/**
	 *
	 * @param aString
	 *            String
	 */
	public static void copyToClipboard(final String aString) {
		final StringSelection stringSelection = new StringSelection(aString);
		final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, stringSelection);
	}

	/**
	 * @param className
	 * @return
	 */
	public static Object createClass(final String className) {
		try {
			return Class.forName(className).newInstance();
		} catch (final Exception e) {
			JKExceptionUtil.handle(e);
			return null;// unreachable
		}
	}

	/**
	 *
	 * @param fileName
	 */
	public static void createDirectory(final String fileName) {
		final File file = new File(fileName);
		if (!file.exists()) {
			file.mkdir();
		}
	}

	public static void createFile(final File file) throws IOException {
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
	}

	/**
	 *
	 * @param iconName
	 * @return
	 */
	public static Icon createIcon(final String iconName) {
		if (iconName == null || iconName.equals("")) {
			return null;
		}
		final URL iconURL = GeneralUtility.getIconURL(iconName);
		if (iconURL == null) {
			System.err.println("Icon " + iconName + " not found , URL : " + iconURL);
			return null;
		}
		// if (icons.get(iconName) == null) {
		// icons.put(iconName, new ImageIcon(iconURL));
		// }
		// return icons.get(iconName);
		return new ImageIcon(iconURL);
	}

	/**
	 * @param ext
	 * @return
	 * @throws IOException
	 */
	public static File createTempFile(final String ext) throws IOException {
		final File file = File.createTempFile("fs-", "." + ext);
		return file;
	}

	/**
	 *
	 * @param source
	 * @return
	 */
	public static String decode(final String source) {
		return decodeFromBase64(source);
	}

	/**
	 *
	 * @param string
	 * @return
	 */
	public static String decodeFromBase64(final String string) {
		return new String(Base64.decode(string));
	}

	/**
	 *
	 * @param str
	 *            String
	 * @return String
	 * @throws DecryptionException
	 */
	public static String decrypt(final String str) {
		return JKEncDec.decrypt(str);
	}

	/**
	 *
	 * @param dir
	 * @return
	 */
	public static boolean deleteDir(final File dir) {
		if (dir.isDirectory()) {
			final String[] children = dir.list();
			for (final String element : children) {
				final boolean success = deleteDir(new File(dir, element));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		if (!dir.delete()) {
			dir.deleteOnExit();
		}
		return true;
	}

	public static void deleteFile(final String fileName) {
		final File file = new File(fileName);
		file.delete();
	}

	/**
	 *
	 * @param source
	 * @return
	 */
	public static String encode(final String source) {
		return encodeInToBase64(source);
	}

	/**
	 *
	 * @param string
	 * @return
	 */
	public static String encodeInToBase64(final String string) {
		return Base64.encodeBytes(string.getBytes());
	}

	/**
	 *
	 * @param str
	 *            String
	 * @return String
	 * @throws EncryptionException
	 */
	public static String encrypt(final String str) {
		return JKEncDec.encrypt(str);
	}

	// ////////////////////////////////////////////////////////////////////////
	public static boolean equals(final Object firstObject, final Object secondObject) {
		if (firstObject == secondObject && secondObject == null) {
			return true;
		}
		if (firstObject == null && secondObject != null) {
			return false;
		}
		return firstObject.equals(secondObject);
	}

	/**
	 *
	 * @param fileName
	 *            String
	 * @return
	 * @throws IOException
	 */
	public static Process executeFile(final String fileName) throws IOException {
		final String command = "cmd /c \"" + fileName + "\"";
		JKLogger.info(command);
		return Runtime.getRuntime().exec(command);
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	public static String fixPropertyName(final String name) {
		return TextUtil.fixPropertyName(name);
	}

	/**
	 * @return
	 */
	public static String getApplicationPath() {
		/**
		 * @todo this is used to get the current class path including the
		 *       package structure , it should
		 */
		final Class<Integer> c = Integer.class;
		final String className = c.getName();
		final String resourceName = "/" + className.replace('.', '/') + ".class";
		final URL location = c.getResource(resourceName);
		final File file = new File(location.getFile());
		return file.getAbsolutePath();
	}

	public static String getExtension(final String fileName, final boolean withPoint) {
		return TextUtil.getExtension(fileName, withPoint);
	}

	/**
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream getFileInputStream(final String fileName) {
		return JKResourceLoaderFactory.getResourceLoader().getResourceAsStream(fileName);
	}

	/**
	 *
	 * @return
	 */
	public static String getFileSeparator() {
		return System.getProperty("file.separator");
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
		// if (getFileInputStream(fileName) != null) {
		in = new BufferedReader(new InputStreamReader(getFileInputStream(fileName))); // Class.class.getResourceAsStream(fileName)));
		// } else {
		// in = new BufferedReader(new FileReader(new File(fileName)));
		// }

		final StringBuffer buf = new StringBuffer();
		String str;
		while ((str = in.readLine()) != null) {
			buf.append(str + "\n");
		}
		return new String(buf.toString().getBytes(), "UTF-8");
	}

	public static URI getFileURI(final String path) {
		try {
			return new URI(Integer.class.getResource(path).toString());
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 *
	 * @return
	 * @throws IOException
	 */
	public static String getHardDiskSerialNumber() throws IOException {
		BufferedReader buffer = null;
		String serial = null;
		try {
			// File file = new File(FILE_READ_HARDDISK_SERIAL);
			final Process process = Runtime.getRuntime().exec(FILE_READ_HARDDISK_SERIAL);
			buffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
			final String[] lines = new String[24];
			String line;
			int i = 0;
			while ((line = buffer.readLine()) != null) {
				lines[i] = line;
				i++;
			}
			final String myStr = lines[4];
			final int loc = myStr.indexOf(":");
			serial = myStr.substring(loc + 1).trim();
		}

		finally {
			if (buffer != null) {
				buffer.close();
			}
		}
		return serial;
	}

	/**
	 *
	 * @param iconName
	 * @return
	 */
	public static ImageIcon getIcon(final String iconName) {
		return (ImageIcon) createIcon(iconName);
	}

	/**
	 *
	 * @param fileName
	 *            String
	 * @return InputStream
	 */
	public static URL getIconURL(final String fileName) {
		// System.out.println("Trying to load icon : " + fileName);
		final URL url = getURL("/resources/icons/" + fileName);
		return url;
	}

	/**
	 * Use this if last modified in File didnt work , possible in compressed
	 * file But it seem that it checks for the modification time for the hall
	 * compressed file it worth checking
	 *
	 * @param resourceName
	 * @return
	 */
	public static long getLastModified(final String resourceName) {
		try {
			final URL url = Integer.class.getResource(resourceName);
			if (url == null) {
				return -1;
			}
			final URLConnection con = url.openConnection();
			final long lastModified = con.getLastModified();
			con.getInputStream().close();
			return lastModified;
		} catch (final IOException e) {
			throw new RuntimeException(e);
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
		} catch (final UnknownHostException e) {
			System.err.println("Unable to get host name");
			return "";
		}
	}

	/**
	 * list Classes inside a given package
	 *
	 * @author Jon Peck http://jonpeck.com (adapted from
	 *         http://www.javaworld.com/javaworld/javatips/jw-javatip113.html)
	 * @param pckgname
	 *            String name of a Package, EG "java.lang"
	 * @return Class[] classes inside the root of the given package
	 * @throws ClassNotFoundException
	 * @throws ClassNotFoundException
	 *             if the Package is invalid
	 */
	public static String[] getPAckageContents(final String pckgname) throws ClassNotFoundException {
		// ArrayList classes = new ArrayList();
		// Get a File object for the package
		File directory = null;
		try {
			directory = new File(Thread.currentThread().getContextClassLoader().getResource('/' + pckgname.replace('.', '/')).getFile());
		} catch (final NullPointerException x) {
			throw new ClassNotFoundException(pckgname + " does not appear to be a valid package");
		}
		if (directory.exists()) {
			// Get the list of the files contained in the package
			final String[] files = directory.list();
			return files;
		}
		return null;
		// for (int i = 0; i < files.length; i++) {
		// we are only interested in .class files
		// if (files.endsWith(".class")) {
		// // removes the .class extension
		// classes.add(Class.forName(pckgname + '.' + files.substring(0,
		// files.length() - 6)));
		// }
		// }
		// } else {
		// throw new ClassNotFoundException(pckgname + " does not appear to be a
		// valid package");
		// }
		// Class[] classesA = new Class[classes.size()];
		// classes.toArray(classesA);
		// return classesA;
	}

	public static Object getPropertyValue(final Object instance, final String fieldName) {
		try {
			return PropertyUtils.getProperty(instance, fieldName);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param fileName
	 *            String
	 * @return String
	 */
	public static InputStream getReportFileAsStream(final String fileName) {
		return getFileInputStream("/resources/reports/" + fileName);
	}

	// ////////////////////////////////////////////////////////////////////
	public static String getReportsOutPath() {
		final String path = GeneralUtility.getUserFolderPath(true) + "reports";
		checkFolderPath(path, true);
		return path;
	}

	/**
	 *
	 * @param fileName
	 *            String
	 * @return String
	 * @throws IOException
	 */
	public static String getSqlFile(final String fileName) {
		try {
			// String sql = loadSqlFromDatabase(fileName);
			// if (sql != null) {
			// return sql;
			// } else {
			return getFileText("/resources/sql/" + fileName);
			// }
		} catch (final Exception ex) {
			JKExceptionUtil.handle(ex);
			return null;
		}
	}

	/**
	 *
	 * @param fileName
	 *            String
	 * @return InputStream
	 */
	public static URL getURL(final String fileName) {
		final URL resourceUrl = JKResourceLoaderFactory.getResourceLoader().getResourceUrl(fileName);
		if (resourceUrl == null) {
			System.err.println("Unable to load resource : " + fileName);
		}
		return resourceUrl;
	}

	// ////////////////////////////////////////////////////////////////////
	public static String getUserFolderPath(final boolean appendFileSeprator) {
		String path = USER_LOCAL_PATH;
		checkFolderPath(path, true);// to create the folder if not exist
		if (appendFileSeprator) {
			path += System.getProperty("file.separator");
		}
		return path;
	}

	public static boolean isDebugMode() {
		final String debug = System.getProperty("fs.debug");
		return Boolean.valueOf(debug);
	}

	public static boolean isDouble(final String txt) {
		return TextUtil.isDouble(txt);
	}

	public static boolean isEmpty(final String txt) {
		return txt == null || TextUtil.isEmpty(txt);
	}

	// //////////////////////////////////////////////////////////////////////
	public static boolean isFileExist(final String fileName) {
		final File file = new File(fileName);
		return file.exists();

	}

	public static boolean isFloat(final String txt) {
		return TextUtil.isFloat(txt);
	}

	public static boolean isInteger(final String txt) {
		return TextUtil.isInteger(txt);
	}

	public static boolean isNumber(final String txt) {
		return isInteger(txt) || isFloat(txt) || isDouble(txt);
	}

	/**
	 * @param fileName
	 * @return
	 * @throws JKDataAccessException
	 */
	public static String loadSqlFromDatabase(final String fileName) {
		try {
			final DynamicDao dynamicDao = DaoFactory.createDynamicDao("conf_queries");
			final List<Record> list = dynamicDao.findByFieldValue("query_name", fileName);
			if (list.size() > 0) {
				return list.get(0).getFieldValueAsString("query_text");
			}
		} catch (final Exception e) {
			JKExceptionUtil.handle(e);
		}
		return null;
	}

	// /////////////////////////////////////////////////////////////////////////////
	// @auother : Mohamed Kiswani
	// return arrays of files and folder in selected file
	// ///////////////////////////////////////////////////////////////////////////
	public static ArrayList<ArrayList<String>> lstFolderContents(final String path) {
		final File folder = new File(path);
		final File[] listOfFiles = folder.listFiles();
		final ArrayList<ArrayList<String>> folderContents = new ArrayList<ArrayList<String>>();
		final ArrayList<String> files = new ArrayList<String>();
		final ArrayList<String> folders = new ArrayList<String>();
		for (final File listOfFile : listOfFiles) {
			if (listOfFile.isFile()) {
				files.add(listOfFile.getName());
			} else if (listOfFile.isDirectory()) {
				folders.add(listOfFile.getName());
			}
		}
		folderContents.add(files);
		folderContents.add(folders);
		return folderContents;

	}

	// /////////////////////////////////////////////////////////////////////////////
	// @auother : Mohamed Kiswani
	// /////////////////////////////////////////////////////////////////////////////
	public static ArrayList<File> lstFolderFilesByDate(final String updateFolder, final long date) {
		final ArrayList<String> namesOfFiles = lstFolderContents(updateFolder).get(0);
		final ArrayList<File> files = new ArrayList<File>();
		for (final String fileName : namesOfFiles) {
			final File file = new File(updateFolder + "/" + fileName);
			final String selectedDate = DateTimeUtil.getFormatedDate(date);
			final String fileDate = DateTimeUtil.getFormatedDate(file.lastModified());
			if (fileDate.equals(selectedDate)) {
				files.add(file);
			}
		}
		return files;
	}

	public static void main(final String[] args) throws IOException {
		System.out.println(decode("ZnNAdW5pX3Bhc3N3b3Jk"));
	}

	public static void moveFile(final String filePath, final String destination) {
		final File file = new File(filePath);
		if (file.exists()) {
			final File dir = new File(destination);
			dir.mkdir();
			file.renameTo(new File(dir, file.getName()));
		}
	}

	/**
	 * Get the String residing on the clipboard.
	 *
	 * @return any text found on the Clipboard; if none found, return an empty
	 *         String.
	 */
	public static String pasteFromClipBoard() {
		String result = "";
		final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		// odd: the Object param of getContents is not currently used
		final Transferable contents = clipboard.getContents(null);
		final boolean hasTransferableText = contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				result = (String) contents.getTransferData(DataFlavor.stringFlavor);
			} catch (final UnsupportedFlavorException ex) {
				// highly unlikely since we are using a standard DataFlavor
				ex.printStackTrace();
			} catch (final IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static void printStackTrace() {
		final Throwable t = new Throwable();
		final StackTraceElement trace[] = t.getStackTrace();
		for (final StackTraceElement element : trace) {
			System.err.println(element.getClassName() + "." + element.getMethodName());
		}

	}

	/**
	 *
	 * @param file
	 *            File
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] readFile(final File file) throws IOException {
		return readStream(new FileInputStream(file));
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
	public static Properties readPropertyStream(final InputStream in) {
		if (in == null) {
			return new Properties();
		}
		try {
			final Properties prop = new Properties();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			// BufferedReader reader = new BufferedReader(new
			// FileReader(LABLES_FILE_NAME));
			String line;
			while ((line = reader.readLine()) != null) {
				line = new String(line.getBytes(), "Cp1256");
				final int lastIndexOfEqual = line.indexOf('=');
				// String[] arr = line.trim().split("=");
				if (lastIndexOfEqual != -1 && lastIndexOfEqual != line.length()) {
					final String key = line.substring(0, lastIndexOfEqual).trim();
					final String value = line.substring(lastIndexOfEqual + 1).trim();
					prop.setProperty(CollectionUtil.fixPropertyKey(key), value);
				}
			}
			reader.close();
			return prop;
		} catch (final IOException e) {
			JKExceptionUtil.handle(e);
			return null;
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *
	 * @param inStream
	 *            InputStream
	 * @return String
	 * @throws IOException
	 */
	public static byte[] readStream(final InputStream inStream) throws IOException {
		DataInputStream in = null;
		try {
			in = new DataInputStream(inStream);
			final int size = in.available();
			final byte arr[] = new byte[size];
			in.readFully(arr);
			return arr;
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	public static String removeExtension(final String fileName) {
		return TextUtil.removeExtension(fileName);
	}

	public static String removeExtraSpaces(final String str) {
		return str.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
	}

	/**
	 *
	 * @param runnable
	 */
	public static void run(final Runnable runnable) {
		final Thread thread = new Thread(runnable);
		thread.start();

	}

	public static void setPeopertyValue(final Object source, final String fieldName, final Object value) {
		try {
			PropertyUtils.setProperty(source, fieldName, value);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			JKExceptionUtil.handle(e);
		}
	}

	/**
	 *
	 * @param file
	 * @param title
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static void showPdfFile(final File file, final String title) throws IOException, MalformedURLException {
		// JKWebBrowser browser=new JKWebBrowser(file.toURL());
		// SwingUtility.showPanelFrame(browser, title);
		// GeneralUtility.executeFile(file.getAbsolutePath());
		showPdfFile(file, title, false);
	}

	/**
	 *
	 * @param file
	 * @param title
	 * @param deleteOnExit
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static void showPdfFile(final File file, final String title, final boolean deleteOnExit) throws MalformedURLException, IOException {
		GeneralUtility.executeFile(file.getAbsolutePath());
		if (deleteOnExit) {
			file.deleteOnExit();
		}
	}

	/**
	 *
	 * @param i
	 */
	public static void sleep(final int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (final InterruptedException e) {
		}
	}

	/**
	 *
	 * @param hash
	 * @param b
	 * @return
	 */
	public static ArrayList<Entry> sortHashTable(final Hashtable<?, ?> hash, final boolean asc) {
		// Put keys and values in to an arraylist using entryset
		final ArrayList<Entry> myArrayList = new ArrayList(hash.entrySet());
		// Sort the values based on values first and then keys.
		Collections.sort(myArrayList, new HashComparator(asc));
		return myArrayList;
	}

	/**
	 *
	 * @param server
	 */
	public static void startFakeThread(final ServerSocket server) {
		final Thread thread = new Thread(new FakeRunnable(server));
		thread.start();
	}

	// ////////////////////////////////////////////////////////////////////
	public static Object toObject(final String xml) {
		// XStream x = createXStream();
		// return x.fromXML(xml);
		// try {
		final ByteArrayInputStream out = new ByteArrayInputStream(xml.getBytes());
		final XMLDecoder encoder = new XMLDecoder(out);
		final Object object = encoder.readObject();
		//
		encoder.close();
		return object;
		// } catch (Exception e) {
		// System.err.println("Failed to decode object : \n" + xml);
		// return null;
		// }
		// return null;
	}

	/**
	 *
	 * @param list
	 * @param separtor
	 * @return
	 */
	public static String toString(final List<?> list, final String separtor) {
		return TextUtil.toString(list, separtor);
	}

	// ////////////////////////////////////////////////////////////////////
	public static String toXml(final Object obj) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();

		// XStream x = createXStream();
		// String xml = x.toXML(obj);
		// return xml;
		final XMLEncoder e = new XMLEncoder(out);
		e.setExceptionListener(new XmlEncoderExceptionListener());
		// e.setPersistenceDelegate(Object.class, new MyPersistenceDelegate());
		e.writeObject(obj);
		e.close();
		return out.toString();
		// return null;
	}

	public static File writeDataToFile(final byte[] data, final File file) throws FileNotFoundException, IOException {
		return writeDataToFile(data, file, false);
	}

	/**
	 *
	 * @param data
	 * @param file
	 * @param append
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static File writeDataToFile(final byte[] data, final File file, final boolean append) throws FileNotFoundException, IOException {
		final FileOutputStream out = new FileOutputStream(file, append);
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

	// ////////////////////////////////////////////////////////////////////////

	/**
	 *
	 * @return File
	 * @param data
	 *            String
	 * @throws IOException
	 */
	public static File writeDataToTempFile(final String data, final String ext) throws IOException {
		final File file = createTempFile(ext);
		final PrintWriter out = new PrintWriter(new FileOutputStream(file));
		out.print(data);
		out.close();
		return file;
	}

	/**
	 * @author Mohamde Kiswani
	 * @since 28-1-2010
	 * @param string
	 * @param path
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void writeLog(final String string, final String path) throws FileNotFoundException, IOException {
		appedDataToFile((DateTimeUtil.getCurrentTime(string) + "\n").getBytes(), path);
	}

	/**
	 * @description : to write on file by using random access ssechanism
	 * @author Mohamde Kiswani
	 * @since 28-1-2010
	 * @param file
	 * @param string
	 * @param i
	 * @throws IOException
	 */
	public static void writeOnFile(final File file, final String string, final long lineIndex) throws IOException {
		final RandomAccessFile rand = new RandomAccessFile(file, "rw");
		rand.seek(lineIndex); // Seek to lineIndex of file
		rand.writeBytes(string); // Write end of file
		rand.close();
	}
}

class MyPersistenceDelegate extends DefaultPersistenceDelegate {
	@Override
	protected void initialize(final Class<?> type, final Object oldInstance, final Object newInstance, final Encoder out) {
		System.out.println("....Processing :" + type + "," + oldInstance + "," + newInstance);
		super.initialize(type, oldInstance, newInstance, out);
	}
}

// ////////////////////////////////////////////////////////////////////
class XmlEncoderExceptionListener implements ExceptionListener {

	PrintStream out;

	public XmlEncoderExceptionListener() {
		try {
			this.out = new PrintStream(new FileOutputStream(GeneralUtility.getUserFolderPath(true) + "gui.log", true));
		} catch (final Exception e) {
			System.err.println("unable to creat gui.log file for encoder exceptions , default logging will be used");
		}
	}

	@Override
	public void exceptionThrown(final Exception e) {
		e.printStackTrace(this.out == null ? System.err : this.out);
	}

}