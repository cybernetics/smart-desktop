package com.fs.commons.application.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

public class DefaultConfigManager extends CommonsConfigManager {
	private static final String FILE_NAME = System.getProperty("db.config", "config.properties");
	public static final String CONFIG_FILE_NAMES[] = { FILE_NAME, "system.config", "system.config.xml" };

	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public DefaultConfigManager() {
		try {
			// try to find within the classes
			for (String name : CONFIG_FILE_NAMES) {
				InputStream in = null;
				try {
					in = GeneralUtility.getFileInputStream( name);
					load(in);
					System.err.println("Config File : "+name+" loaded");
					return;
				} catch (FileNotFoundException e) {
				}
			}

			for (String name : CONFIG_FILE_NAMES) {
				InputStream in = null;
				try {
					in = GeneralUtility.getFileInputStream("/" +name);
					load(in);
					System.err.println("Config File : /"+name+" loaded");
					return;
				} catch (FileNotFoundException e) {
				}
			}
			// not found
			String errorMessage = "No Configuration Available.\n The System will Exit";
			errorMessage += "\nFile is not found on " + new File("X").getAbsolutePath();
			SwingUtility.showUserErrorDialog(errorMessage);
			System.exit(0);
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	public DefaultConfigManager(InputStream configStream) throws IOException {
		load(configStream);
	}

//	/**
//	 * @param file
//	 */
//	private void loadFile(File file) {
//		try {
//			load(file.getAbsolutePath());
//			System.getProperties().putAll(getProperties());
//		} catch (IOException e) {
//			String errorMessage = "Error Loading Configuration File.\n The System will Exit\n" + file.getAbsolutePath();
//			SwingUtility.showErrorDialog(errorMessage, e);
//			System.exit(0);
//		}
//	}

	public static void main(String[] args) {
		DefaultConfigManager m = new DefaultConfigManager();
		System.out.println(m.getProperties());
	}
}
