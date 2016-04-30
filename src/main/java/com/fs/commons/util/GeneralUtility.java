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
import com.fs.commons.application.util.ResourceLoaderFactory;
import com.fs.commons.dao.dynamic.DaoFactory;
import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.exception.ExceptionUtil;
import com.fs.commons.logging.Logger;
import com.fs.commons.security.EncDecImpl;
import com.fs.commons.security.exceptions.DecryptionException;
import com.fs.commons.security.exceptions.EncryptionException;
import com.lowagie.text.pdf.codec.Base64;

public class GeneralUtility {
	private static final String USER_LOCAL_PATH = System.getProperty("user.home") + System.getProperty("file.separator") + "final-solutions";
	private static Hashtable<String, ImageIcon> icons = new Hashtable<String, ImageIcon>();
	public static Locale Locale = new Locale("EN", "US");

	static SecretKeySpec spec = new SecretKeySpec("123456781234567812345678".getBytes(), "DESEDE");

	static EncDecImpl encDecUtil = new EncDecImpl(spec);

	/**
	 * 
	 * @param file
	 *            File
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] readFile(File file) throws IOException {
		return readStream(new FileInputStream(file));
	}

	/**
	 * 
	 * @param inStream
	 *            InputStream
	 * @return String
	 * @throws IOException
	 */
	public static byte[] readStream(InputStream inStream) throws IOException {
		DataInputStream in = null;
		try {
			in = new DataInputStream(inStream);
			int size = in.available();
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
		// if (getFileInputStream(fileName) != null) {
		in = new BufferedReader(new InputStreamReader(getFileInputStream(fileName))); // Class.class.getResourceAsStream(fileName)));
		// } else {
		// in = new BufferedReader(new FileReader(new File(fileName)));
		// }

		StringBuffer buf = new StringBuffer();
		String str;
		while ((str = in.readLine()) != null) {
			buf.append(str + "\n");
		}
		return new String(buf.toString().getBytes(), "UTF-8");
	}

	/**
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream getFileInputStream(String fileName) throws FileNotFoundException {
		if (ResourceLoaderFactory.getResourceLoaderImp().getResourceAsStream(fileName) != null) {
			InputStream in = ResourceLoaderFactory.getResourceLoaderImp().getResourceAsStream(fileName);
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
	 * @return String
	 * @throws IOException
	 */
	public static String getSqlFile(String fileName) {
		try {
			// String sql = loadSqlFromDatabase(fileName);
			// if (sql != null) {
			// return sql;
			// } else {
			return getFileText("/resources/sql/" + fileName);
			// }
		} catch (Exception ex) {
			ExceptionUtil.handleException(ex);
			return null;
		}
	}

	/**
	 * @param fileName
	 * @return
	 * @throws DaoException
	 */
	public static String loadSqlFromDatabase(String fileName) {
		try {
			DynamicDao dynamicDao = DaoFactory.createDynamicDao("conf_queries");
			List<Record> list = dynamicDao.findByFieldValue("query_name", fileName);
			if (list.size() > 0) {
				return list.get(0).getFieldValueAsString("query_text");
			}
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
		return null;
	}

	/**
	 * @param fileName
	 *            String
	 * @return String
	 */
	public static InputStream getReportFileAsStream(String fileName) {
		try {
			return getFileInputStream("/resources/reports/" + fileName);
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	/**
	 * 
	 * @param fileName
	 *            String
	 * @return InputStream
	 */
	public static URL getIconURL(String fileName) {
		// System.out.println("Trying to load icon : " + fileName);
		URL url = getURL("/resources/icons/" + fileName);
		return url;
	}

	/**
	 * 
	 * @param iconName
	 * @return
	 */
	public static ImageIcon getIcon(String iconName) {
		return (ImageIcon) createIcon(iconName);
	}

	/**
	 * 
	 * @param fileName
	 *            String
	 * @return InputStream
	 */
	public static URL getURL(String fileName) {
		URL resourceUrl = ResourceLoaderFactory.getResourceLoaderImp().getResourceUrl(fileName);
		if (resourceUrl == null) {
			System.err.println("Unable to load resource : " + fileName);
		}
		return resourceUrl;
	}

	/**
	 * 
	 * @return File
	 * @param data
	 *            String
	 * @throws IOException
	 */
	public static File writeDataToTempFile(String data, String ext) throws IOException {
		File file = createTempFile(ext);
		PrintWriter out = new PrintWriter(new FileOutputStream(file));
		out.print(data);
		out.close();
		return file;
	}

	/**
	 * @param ext
	 * @return
	 * @throws IOException
	 */
	public static File createTempFile(String ext) throws IOException {
		File file = File.createTempFile("fs-", "." + ext);
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

	public static File writeDataToFile(byte[] data, File file) throws FileNotFoundException, IOException {
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
	public static File writeDataToFile(byte[] data, File file, boolean append) throws FileNotFoundException, IOException {
		FileOutputStream out = new FileOutputStream(file, append);
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
		String command = "cmd /c \"" + fileName + "\"";
		Logger.info(command);
		return Runtime.getRuntime().exec(command);
	}

	/**
	 * 
	 * @param str
	 *            String
	 * @return String
	 * @throws EncryptionException
	 */
	public static String encrypt(String str) throws EncryptionException {
		return new String(encDecUtil.encrypt(str.getBytes()));
	}

	/**
	 * 
	 * @param str
	 *            String
	 * @return String
	 * @throws DecryptionException
	 */
	public static String decrypt(String str) throws DecryptionException {
		return new String(encDecUtil.decrypt(str.getBytes()));
	}

	/**
	 * 
	 * @param aString
	 *            String
	 */
	public static void copyToClipboard(String aString) {
		StringSelection stringSelection = new StringSelection(aString);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, stringSelection);
	}

	/**
	 * Get the String residing on the clipboard.
	 * 
	 * @return any text found on the Clipboard; if none found, return an empty
	 *         String.
	 */
	public static String pasteFromClipBoard() {
		String result = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		// odd: the Object param of getContents is not currently used
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				result = (String) contents.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException ex) {
				// highly unlikely since we are using a standard DataFlavor
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
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
			Process process = Runtime.getRuntime().exec(FILE_READ_HARDDISK_SERIAL);
			buffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String[] lines = new String[24];
			String line;
			int i = 0;
			while ((line = buffer.readLine()) != null) {
				lines[i] = line;
				i++;
			}
			String myStr = lines[4];
			int loc = myStr.indexOf(":");
			serial = myStr.substring(loc + 1).trim();
		}

		finally {
			if (buffer != null) {
				buffer.close();
			}
		}
		return serial;
	}

	public static void checkServer(String host, int port) throws ServerDownException {
		try {
			new Socket(host, port);
		} catch (IOException ex) {
			throw new ServerDownException(ex, host, port);
		}
	}

	/**
	 * @return
	 */
	public static String getApplicationPath() {
		/**
		 * @todo this is used to get the current class path including the
		 *       package structure , it should
		 */
		Class<Integer> c = Integer.class;
		String className = c.getName();
		String resourceName = "/" + className.replace('.', '/') + ".class";
		URL location = c.getResource(resourceName);
		File file = new File(location.getFile());
		return file.getAbsolutePath();
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
	public static String[] getPAckageContents(String pckgname) throws ClassNotFoundException {
		// ArrayList classes = new ArrayList();
		// Get a File object for the package
		File directory = null;
		try {
			directory = new File(Thread.currentThread().getContextClassLoader().getResource('/' + pckgname.replace('.', '/')).getFile());
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(pckgname + " does not appear to be a valid package");
		}
		if (directory.exists()) {
			// Get the list of the files contained in the package
			String[] files = directory.list();
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

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String encodeInToBase64(String string) {
		return Base64.encodeBytes(string.getBytes());
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String decodeFromBase64(String string) {
		return new String(Base64.decode(string));
	}

	public static Time convertDateToTime(Date date) {
		Time time = new Time(date.getTime());
		time.setSeconds(0);
		return time;
	}

	// ////////////////////////
	private static String FILE_READ_HARDDISK_SERIAL = "diskid32.exe";

	public static void printStackTrace() {
		Throwable t = new Throwable();
		StackTraceElement trace[] = t.getStackTrace();
		for (int i = 0; i < trace.length; i++) {
			System.err.println(trace[i].getClassName() + "." + trace[i].getMethodName());
		}

	}

	/**
	 * 
	 * @param server
	 */
	public static void startFakeThread(ServerSocket server) {
		Thread thread = new Thread(new FakeRunnable(server));
		thread.start();
	}

	/**
	 * 
	 * @param file
	 * @param title
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static void showPdfFile(File file, String title) throws IOException, MalformedURLException {
		// JKWebBrowser browser=new JKWebBrowser(file.toURL());
		// SwingUtility.showPanelFrame(browser, title);
		// GeneralUtility.executeFile(file.getAbsolutePath());
		showPdfFile(file, title, false);
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
				line = new String(line.getBytes(), "Cp1256");
				int lastIndexOfEqual = line.indexOf('=');
				// String[] arr = line.trim().split("=");
				if (lastIndexOfEqual != -1 && lastIndexOfEqual != line.length()) {
					String key = line.substring(0, lastIndexOfEqual).trim();
					String value = line.substring(lastIndexOfEqual + 1).trim();
					prop.setProperty(CollectionUtil.fixPropertyKey(key), value);
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
	 * @param iconName
	 * @return
	 */
	public static Icon createIcon(String iconName) {
		if (iconName == null || iconName.equals("")) {
			return null;
		}
		URL iconURL = GeneralUtility.getIconURL(iconName);
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
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getLocalHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			System.err.println("Unable to get host name");
			return "";
		}
	}

	public static void main(String[] args) throws IOException, EncryptionException {
		System.out.println(decode("ZnNAdW5pX3Bhc3N3b3Jk"));
	}

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
	public static void addAutocommitFalse(String path) throws IOException {
		File file = new File(path);
		writeOnFile(file, "\n", 0);
		writeOnFile(file, "\n", 0);
		writeOnFile(file, "\n", 0);
		writeOnFile(file, "SET AUTOCOMMIT=0;\n\n", 0);
		writeOnFile(file, "\nCOMMIT;", file.length());

	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	public static String encode(String source) {
		return encodeInToBase64(source);
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	public static String decode(String source) {
		return decodeFromBase64(source);
	}

	/**
	 * 
	 * @param i
	 */
	public static void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * 
	 * @param runnable
	 */
	public static void run(Runnable runnable) {
		Thread thread = new Thread(runnable);
		thread.start();

	}

	/**
	 * 
	 * @param file
	 * @param title
	 * @param deleteOnExit
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static void showPdfFile(File file, String title, boolean deleteOnExit) throws MalformedURLException, IOException {
		GeneralUtility.executeFile(file.getAbsolutePath());
		if (deleteOnExit) {
			file.deleteOnExit();
		}
	}

	public static String removeExtraSpaces(String str) {
		return str.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
	}

	/**
	 * 
	 * @param hash
	 * @param b
	 * @return
	 */
	public static ArrayList<Entry> sortHashTable(Hashtable<?, ?> hash, boolean asc) {
		// Put keys and values in to an arraylist using entryset
		ArrayList<Entry> myArrayList = new ArrayList(hash.entrySet());
		// Sort the values based on values first and then keys.
		Collections.sort(myArrayList, new HashComparator(asc));
		return myArrayList;
	}

	/**
	 * 
	 * @author Administrator
	 * 
	 */
	static class HashComparator implements Comparator<Object> {
		private final boolean asc;

		public HashComparator(boolean asc) {
			this.asc = asc;
		}

		public int compare(Object obj1, Object obj2) {
			int result = 0;
			Map.Entry e1 = (Map.Entry) obj1;
			Map.Entry e2 = (Map.Entry) obj2;// Sort based on values.
			Comparable<Comparable<Comparable>> value1 = (Comparable<Comparable<Comparable>>) e1.getValue();
			Comparable<Comparable> value2 = (Comparable<Comparable>) e2.getValue();
			if (value1.compareTo(value2) == 0) {
				Comparable<Comparable<?>> key1 = (Comparable<Comparable<?>>) e1.getKey();
				Comparable<?> key2 = (Comparable<?>) e2.getKey();
				// Sort String in an alphabetical order
				result = key1.compareTo(key2);
			} else {
				if (asc) {
					result = value1.compareTo(value2);
				} else {
					result = value2.compareTo(value1);
				}
			}
			return result;
		}
	}

	/**
	 * @param className
	 * @return
	 */
	public static Object createClass(String className) {
		try {
			return Class.forName(className).newInstance();
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
			return null;// unreachable
		}
	}

	/**
	 * 
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public static Object copy(Object source) {
		try {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(source);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object deepCopy = ois.readObject();
			return deepCopy;
		} catch (Exception e) {
			// ExceptionUtil.handleException(e);
			System.err.println("Failed to copy object of class : " + source.getClass().getName() + " / " + e.getMessage());
			e.printStackTrace();
			return null;// unreachable
		}
	}

	public static boolean isDebugMode() {
		String debug = System.getProperty("fs.debug");
		return Boolean.valueOf(debug);
	}

	public static URI getFileURI(String path) {
		try {
			return new URI(Integer.class.getResource(path).toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param fileName
	 */
	public static void createDirectory(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			file.mkdir();
		}
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
	public static void writeOnFile(File file, String string, long lineIndex) throws IOException {
		RandomAccessFile rand = new RandomAccessFile(file, "rw");
		rand.seek(lineIndex); // Seek to lineIndex of file
		rand.writeBytes(string); // Write end of file
		rand.close();
	}

	/**
	 * @author Mohamde Kiswani
	 * @since 28-1-2010
	 * @param string
	 * @param path
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void writeLog(String string, String path) throws FileNotFoundException, IOException {
		appedDataToFile((DateTimeUtil.getCurrentTime(string) + "\n").getBytes(), path);
	}

	/**
	 * 
	 * @param bytes
	 * @param path
	 * @throws IOException
	 */
	public static void appedDataToFile(byte[] bytes, String path) throws IOException {
		FileOutputStream appendedFile = new FileOutputStream(path, true);
		appendedFile.write(bytes);
		appendedFile.close();

	}

	public static void deleteFile(String fileName) {
		File file = new File(fileName);
		file.delete();
	}

	// //////////////////////////////////////////////////////////////////////
	public static boolean isFileExist(String fileName) {
		File file = new File(fileName);
		return file.exists();

	}

	// /////////////////////////////////////////////////////////////////////////////
	// @auother : Mohamed Kiswani
	// return arrays of files and folder in selected file
	// ///////////////////////////////////////////////////////////////////////////
	public static ArrayList<ArrayList<String>> lstFolderContents(String path) {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		ArrayList<ArrayList<String>> folderContents = new ArrayList<ArrayList<String>>();
		ArrayList<String> files = new ArrayList<String>();
		ArrayList<String> folders = new ArrayList<String>();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				files.add(listOfFiles[i].getName());
			} else if (listOfFiles[i].isDirectory()) {
				folders.add(listOfFiles[i].getName());
			}
		}
		folderContents.add(files);
		folderContents.add(folders);
		return folderContents;

	}

	// /////////////////////////////////////////////////////////////////////////////
	// @auother : Mohamed Kiswani
	// /////////////////////////////////////////////////////////////////////////////
	public static ArrayList<File> lstFolderFilesByDate(String updateFolder, long date) {
		ArrayList<String> namesOfFiles = lstFolderContents(updateFolder).get(0);
		ArrayList<File> files = new ArrayList<File>();
		for (String fileName : namesOfFiles) {
			File file = new File(updateFolder + "/" + fileName);
			String selectedDate = DateTimeUtil.getFormatedDate(date);
			String fileDate = DateTimeUtil.getFormatedDate(file.lastModified());
			if (fileDate.equals(selectedDate)) {
				files.add(file);
			}
		}
		return files;
	}

	public static void moveFile(String filePath, String destination) {
		File file = new File(filePath);
		if (file.exists()) {
			File dir = new File(destination);
			dir.mkdir();
			file.renameTo(new File(dir, file.getName()));
		}
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static String fixPropertyName(String name) {
		return TextUtil.fixPropertyName(name);
	}

	/**
	 * Use this if last modified in File didnt work , possible in compressed
	 * file But it seem that it checks for the modification time for the hall
	 * compressed file it worth checking
	 * 
	 * @param resourceName
	 * @return
	 */
	public static long getLastModified(String resourceName) {
		try {
			URL url = Integer.class.getResource(resourceName);
			if (url == null) {
				return -1;
			}
			URLConnection con = url.openConnection();
			long lastModified = con.getLastModified();
			con.getInputStream().close();
			return lastModified;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param list
	 * @param separtor
	 * @return
	 */
	public static String toString(List<?> list, String separtor) {
		return TextUtil.toString(list, separtor);
	}

	public static boolean isEmpty(String txt) {
		return txt == null || TextUtil.isEmpty(txt);
	}

	public static boolean isInteger(String txt) {
		return TextUtil.isInteger(txt);
	}

	public static boolean isNumber(String txt) {
		return isInteger(txt) || isFloat(txt) || isDouble(txt);
	}

	public static boolean isFloat(String txt) {
		return TextUtil.isFloat(txt);
	}

	public static boolean isDouble(String txt) {
		return TextUtil.isDouble(txt);
	}

	public static String removeExtension(String fileName) {
		return TextUtil.removeExtension(fileName);
	}

	public static String getExtension(String fileName, boolean withPoint) {
		return TextUtil.getExtension(fileName, withPoint);
	}

	public static void createFile(File file) throws IOException {
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
	}

	// ////////////////////////////////////////////////////////////////////
	public static String getUserFolderPath(boolean appendFileSeprator) {
		String path = USER_LOCAL_PATH;
		checkFolderPath(path, true);// to create the folder if not exist
		if (appendFileSeprator) {
			path += System.getProperty("file.separator");
		}
		return path;
	}

	// ////////////////////////////////////////////////////////////////////
	public static File checkFolderPath(String path, boolean create) {
		File file = new File(path);
		if (!file.exists()) {
			if (create) {
				file.mkdir();
			}
			return null;
		}
		return file;
	}

	// ////////////////////////////////////////////////////////////////////
	public static String toXml(Object obj) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		// XStream x = createXStream();
		// String xml = x.toXML(obj);
		// return xml;
		XMLEncoder e = new XMLEncoder(out);
		e.setExceptionListener(new XmlEncoderExceptionListener());
		// e.setPersistenceDelegate(Object.class, new MyPersistenceDelegate());
		e.writeObject(obj);
		e.close();
		return out.toString();
		// return null;
	}

	// ////////////////////////////////////////////////////////////////////
	public static Object toObject(String xml) {
		// XStream x = createXStream();
		// return x.fromXML(xml);
		// try {
		ByteArrayInputStream out = new ByteArrayInputStream(xml.getBytes());
		XMLDecoder encoder = new XMLDecoder(out);
		Object object = encoder.readObject();
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
	 */
	public static void clearTempFiles() {
		String userFolderPath = getUserFolderPath(false);
		File file = new File(userFolderPath);
		deleteDir(file);
	}

	/**
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
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

	// ////////////////////////////////////////////////////////////////////
	public static String getReportsOutPath() {
		String path = GeneralUtility.getUserFolderPath(true) + "reports";
		checkFolderPath(path, true);
		return path;
	}

	// ////////////////////////////////////////////////////////////////////////
	public static boolean equals(Object firstObject, Object secondObject) {
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
	 * @return
	 */
	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}

	/**
	 * 
	 * @param databaseName
	 * @param databaseHost
	 * @param databasePort
	 * @throws DatabaseDownException
	 */
	public static void checkDatabaseServer(String databaseName, String databaseHost, int databasePort) throws DatabaseDownException {
		try {
			checkServer(databaseHost, databasePort);
		} catch (ServerDownException e) {
			throw new DatabaseDownException((Exception) e.getCause(), databaseName, databaseHost, databasePort);
		}
	}

	// ////////////////////////////////////////////////////////////////////////

	public static Object getPropertyValue(Object instance, String fieldName) {
		try {
			return PropertyUtils.getProperty(instance, fieldName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void setPeopertyValue(Object source, String fieldName, Object value) {
		try {
			PropertyUtils.setProperty(source, fieldName, value);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			ExceptionUtil.handleException(e);
		}
	}

	public static <T> T cloneBean(Object bean) {
		try {
			return (T) BeanUtils.cloneBean(bean);
		} catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
}

class FakeRunnable implements Runnable {
	public FakeRunnable(Object object) {
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(1000000);
			} catch (InterruptedException e) {
			}
		}
	}

}

class MyPersistenceDelegate extends DefaultPersistenceDelegate {
	@Override
	protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder out) {
		System.out.println("....Processing :" + type + "," + oldInstance + "," + newInstance);
		super.initialize(type, oldInstance, newInstance, out);
	}
}

// ////////////////////////////////////////////////////////////////////
class XmlEncoderExceptionListener implements ExceptionListener {

	PrintStream out;

	public XmlEncoderExceptionListener() {
		try {
			out = new PrintStream(new FileOutputStream(GeneralUtility.getUserFolderPath(true) + "gui.log", true));
		} catch (Exception e) {
			System.err.println("unable to creat gui.log file for encoder exceptions , default logging will be used");
		}
	}

	@Override
	public void exceptionThrown(Exception e) {
		e.printStackTrace(out == null ? System.err : out);
	}

}