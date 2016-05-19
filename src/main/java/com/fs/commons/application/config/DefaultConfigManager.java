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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.util.GeneralUtility;
import com.jk.exceptions.handler.JKExceptionUtil;

public class DefaultConfigManager extends CommonsConfigManager {
	private static final String FILE_NAME = System.getProperty("db.config", "config.properties");
	public static final String CONFIG_FILE_NAMES[] = { FILE_NAME, "system.config", "system.config.xml" };

	public static void main(final String[] args) {
		final DefaultConfigManager m = new DefaultConfigManager();
		System.out.println(m.getProperties());
	}

	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public DefaultConfigManager() {
		try {
			// try to find within the classes
			for (final String name : CONFIG_FILE_NAMES) {
				InputStream in = null;
				in = GeneralUtility.getFileInputStream(name);
				if (in != null) {
					load(in);
					System.err.println("Config File : " + name + " loaded");
					return;
				}
			}

			for (final String name : CONFIG_FILE_NAMES) {
				InputStream in = null;
				try {
					in = GeneralUtility.getFileInputStream("/" + name);
					if (in != null) {
						load(in);
					}
					System.err.println("Config File : /" + name + " loaded");
					return;
				} catch (final FileNotFoundException e) {
				}
			}
			// not found
			String errorMessage = "No Configuration Available.\n The System will Exit";
			errorMessage += "\nFile is not found on " + new File("X").getAbsolutePath();
			SwingUtility.showUserErrorDialog(errorMessage);
			System.exit(0);
		} catch (final Exception e) {
			JKExceptionUtil.handle(e);
		}
	}

	// /**
	// * @param file
	// */
	// private void loadFile(File file) {
	// try {
	// load(file.getAbsolutePath());
	// System.getProperties().putAll(getProperties());
	// } catch (IOException e) {
	// String errorMessage = "Error Loading Configuration File.\n The System
	// will Exit\n" + file.getAbsolutePath();
	// SwingUtility.showErrorDialog(errorMessage, e);
	// System.exit(0);
	// }
	// }

	public DefaultConfigManager(final InputStream configStream) throws IOException {
		load(configStream);
	}
}
