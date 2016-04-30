package com.fs.commons.application.config;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import com.fs.commons.util.CollectionUtil;
import com.fs.commons.util.GeneralUtility;
import com.lowagie.text.pdf.codec.Base64;

public class CommonsConfigManager {

	private Properties prop = new Properties();
	// static Base64 encDec = Base64. new Base64();
	private String fileName;

	/**
	 * 
	 */
	public CommonsConfigManager() {
	}

	/**
	 * @throws IOException
	 * @throws FileNotFoundException
	 * 
	 */
	public CommonsConfigManager(String fileName) throws FileNotFoundException, IOException {
		this(new FileInputStream(fileName));
		this.fileName = fileName;
	}

	/**
	 * 
	 * @param inputStream
	 * @throws IOException
	 */
	public CommonsConfigManager(InputStream inputStream) throws IOException {
		load(inputStream);
	}

	/**
	 * 
	 * @param name
	 * @param value
	 * @param encoded
	 */
	public void setProperty(String name, String value, boolean encoded) {
		if (encoded) {
			value = Base64.encodeBytes(value.getBytes());
		}
		prop.setProperty(fixKey(name), value);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	private String fixKey(String name) {
		return CollectionUtil.fixPropertyKey(name);
	}

	/**
	 * 
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public String getProperty(String name, String defaultValue) {
		return getProperty(CollectionUtil.fixPropertyKey(name), defaultValue, false);
	}

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setProperty(String name, String value) {
		setProperty(name, value, false);
	}

	/**
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public String getProperty(String name, String defaultValue, boolean encoded) {
		String value = prop.getProperty(fixKey(name));
		if (value != null) {
			if (encoded && isReadEncoded()) {
				byte[] decode = Base64.decode(value);
				if (decode != null) {
					value = new String(decode);
				} else {
					// the encoded failed , return the value it self without
					// encoded
					// value=null;
				}
			}
			return value;
		}
		return defaultValue;
	}

	/**
	 * 
	 * @return
	 */
	protected boolean isReadEncoded() {
		return Boolean.parseBoolean(prop.getProperty("encoded", "true"));
	}

	/**
	 * 
	 * @param fileName
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void load(String fileName) throws FileNotFoundException, IOException {
		load(new BufferedInputStream(new FileInputStream(fileName)));
	}

	/**
	 * 
	 * @param inStream
	 * @throws IOException
	 */
	public void load(InputStream inStream) throws IOException {
		BufferedInputStream in=new BufferedInputStream(inStream);
		try {
			in.mark(0);
			prop.loadFromXML(in);
			CollectionUtil.fixPropertiesKeys(prop);
			// Note : the keys is not fixed if in xml format
			
		} catch (InvalidPropertiesFormatException e) {
			// not xml , try to load normal properties file
			in.reset();
			prop = GeneralUtility.readPropertyStream(in);
		}
		System.getProperties().putAll(prop);
		System.out.println(System.getProperties().toString().replaceAll(",", "\n"));
	}

	/**
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public void storeToXML() throws IOException {
		if (fileName != null) {
			storeToXML(fileName);
		} else {
			throw new RuntimeException("File name has not been set");
		}
	}

	/**
	 * 
	 * @param os
	 * @param comment
	 * @param encoding
	 * @throws IOException
	 */
	public void storeToXML(String fileName) throws IOException {
		storeToXML(new FileOutputStream(fileName));
	}

	/**
	 * 
	 * @param os
	 * @param comment
	 * @param encoding
	 * @throws IOException
	 */
	public void storeToXML(OutputStream os) throws IOException {
		prop.storeToXML(os, "JK Configuration File");
	}

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		CommonsConfigManager config = new CommonsConfigManager();
		config.setProperty("Host", "192.168.1.1");
		config.setProperty("Port", "3306");
		config.setProperty("User", "root", true);
		config.setProperty("Password", "Irbid123", true);
		config.storeToXML("d:/config.xml");
		System.out.println("Done");
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String encode(String string) {
		return Base64.encodeBytes(string.getBytes());
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String decode(String string) {
		return new String(Base64.decode(string));
	}

	/**
	 * 
	 * @return
	 */
	public Properties getProperties() {
		return prop;
	}

	public String getFileName() {
		return fileName;
	}

	public int getIntProperty(String string, int def) {
		String s = getProperty(string, def + "");
		return Integer.parseInt(s);
	}

	public String getString(String name, String defaultValue) {
		return getProperty(name, defaultValue);
	}

	public String getString(String name) {
		return getProperty(name, null);
	}

	// //////////////////////////////////////////////////////////////////////////////////
	// check whether the keys available configuration is available
	// This will be used for mandatory fields
	public String validateProperties(String... keys) {
		StringBuffer buf = new StringBuffer();
		for (String key : keys) {
			String value = getString(key);
			if (value == null || value.trim().equals("")) {
				buf.append(key + "=\n");
			}
		}
		return buf.length() == 0 ? null : buf.toString();
	}

	public int getPropertyAsInteger(String name, int defaultValue) {
		String property = getProperty(name, defaultValue + "");
		if (property == null) {
			return 0;
		}
		return Integer.parseInt(property);
	}

	public String getProperty(String prop) {
		return getProperty(prop, null);
	}

	public boolean getPropertyAsBoolean(String property, boolean defaultValue) {
		String prop = getProperty(property);
		if (prop == null) {
			return defaultValue;
		}
		prop = prop.trim().toLowerCase();
		return prop.equals("true") || prop.equals("1");
	}

}
