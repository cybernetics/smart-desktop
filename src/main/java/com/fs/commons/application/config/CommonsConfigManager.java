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
package com.fs.commons.application.config;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.fs.commons.util.CollectionUtil;
import com.fs.commons.util.GeneralUtility;
import com.jk.logging.JKLogger;
import com.jk.logging.JKLoggerFactory;
import com.lowagie.text.pdf.codec.Base64;

public class CommonsConfigManager {

	/**
	 *
	 * @param string
	 * @return
	 */
	public static String decode(final String string) {
		return new String(Base64.decode(string));
	}

	/**
	 *
	 * @param string
	 * @return
	 */
	public static String encode(final String string) {
		return Base64.encodeBytes(string.getBytes());
	}

	/**
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {
		final CommonsConfigManager config = new CommonsConfigManager();
		config.setProperty("Host", "192.168.1.1");
		config.setProperty("Port", "3306");
		config.setProperty("User", "root", true);
		config.setProperty("Password", "Irbid123", true);
		config.storeToXML("d:/config.xml");
		System.out.println("Done");
	}

	private Properties prop = new Properties();

	// static Base64 encDec = Base64. new Base64();
	private String fileName;

	private JKLogger logger=JKLoggerFactory.getLogger(getClass());

	/**
	 *
	 */
	public CommonsConfigManager() {
	}

	/**
	 *
	 * @param inputStream
	 * @throws IOException
	 */
	public CommonsConfigManager(final InputStream inputStream) throws IOException {
		load(inputStream);
	}

	/**
	 * @throws IOException
	 * @throws FileNotFoundException
	 *
	 */
	public CommonsConfigManager(final String fileName) throws FileNotFoundException, IOException {
		this(new FileInputStream(fileName));
		this.fileName = fileName;
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	private String fixKey(final String name) {
		return CollectionUtil.fixPropertyKey(name);
	}

	public String getFileName() {
		return this.fileName;
	}

	public int getIntProperty(final String string, final int def) {
		final String s = getProperty(string, def + "");
		return Integer.parseInt(s);
	}

	/**
	 *
	 * @return
	 */
	public Properties getProperties() {
		return this.prop;
	}

	public String getProperty(final String prop) {
		return getProperty(prop, null);
	}

	/**
	 *
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public String getProperty(final String name, final String defaultValue) {
		return getProperty(CollectionUtil.fixPropertyKey(name), defaultValue, false);
	}

	/**
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public String getProperty(final String name, final String defaultValue, final boolean encoded) {
		String value = this.prop.getProperty(fixKey(name));
		if (value != null) {
			if (encoded && isReadEncoded()) {
				final byte[] decode = Base64.decode(value);
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

	public boolean getPropertyAsBoolean(final String property, final boolean defaultValue) {
		String prop = getProperty(property);
		if (prop == null) {
			return defaultValue;
		}
		prop = prop.trim().toLowerCase();
		return prop.equals("true") || prop.equals("1");
	}

	public int getPropertyAsInteger(final String name, final int defaultValue) {
		final String property = getProperty(name, defaultValue + "");
		if (property == null) {
			return 0;
		}
		return Integer.parseInt(property);
	}

	public String getString(final String name) {
		return getProperty(name, null);
	}

	public String getString(final String name, final String defaultValue) {
		return getProperty(name, defaultValue);
	}

	/**
	 *
	 * @return
	 */
	protected boolean isReadEncoded() {
		return Boolean.parseBoolean(this.prop.getProperty("encoded", "true"));
	}

	/**
	 *
	 * @param inStream
	 * @throws IOException
	 */
	public void load(final InputStream inStream) throws IOException {
		final BufferedInputStream in = new BufferedInputStream(inStream);
		// try {
		// in.mark(0);
		// this.prop.loadFromXML(in);
		// // Note : the keys is not fixed if in xml format
		//
		// } catch (final InvalidPropertiesFormatException e) {
		// // not xml , try to load normal properties file
		// in.reset();
		this.prop = GeneralUtility.readPropertyStream(in);
		// }
		CollectionUtil.fixPropertiesKeys(this.prop);
		System.getProperties().putAll(this.prop);
		logger.debug(System.getProperties().toString().replaceAll(",", "\n"));
	}

	/**
	 *
	 * @param fileName
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void load(final String fileName) throws FileNotFoundException, IOException {
		load(GeneralUtility.getFileInputStream(fileName));
	}

	/**
	 *
	 * @param name
	 * @param value
	 */
	public void setProperty(final String name, final String value) {
		setProperty(name, value, false);
	}

	/**
	 *
	 * @param name
	 * @param value
	 * @param encoded
	 */
	public void setProperty(final String name, String value, final boolean encoded) {
		if (encoded) {
			value = Base64.encodeBytes(value.getBytes());
		}
		this.prop.setProperty(fixKey(name), value);
	}

	/**
	 *
	 * @param fileName
	 * @throws IOException
	 */
	public void storeToXML() throws IOException {
		if (this.fileName != null) {
			storeToXML(this.fileName);
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
	public void storeToXML(final OutputStream os) throws IOException {
		this.prop.storeToXML(os, "JK Configuration File");
	}

	/**
	 *
	 * @param os
	 * @param comment
	 * @param encoding
	 * @throws IOException
	 */
	public void storeToXML(final String fileName) throws IOException {
		storeToXML(new FileOutputStream(fileName));
	}

	// //////////////////////////////////////////////////////////////////////////////////
	// check whether the keys available configuration is available
	// This will be used for mandatory fields
	public String validateProperties(final String... keys) {
		final StringBuffer buf = new StringBuffer();
		for (final String key : keys) {
			final String value = getString(key);
			if (value == null || value.trim().equals("")) {
				buf.append(key + "=\n");
			}
		}
		return buf.length() == 0 ? null : buf.toString();
	}

}
