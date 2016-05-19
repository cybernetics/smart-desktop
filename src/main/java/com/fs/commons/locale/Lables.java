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
package com.fs.commons.locale;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.fs.commons.configuration.beans.Lable;
import com.fs.commons.util.CollectionUtil;
import com.fs.commons.util.FormatUtil;
import com.fs.commons.util.GeneralUtility;
import com.jk.exceptions.handler.JKExceptionUtil;

public class Lables {
	public static final String LABLES_FILE_NAME = "/lables.properties";

	private static Lables instance;

	// /////////////////////////////////////////////////////////////////////
	public static String get(final String label, final boolean capitlize) {
		final String lbl = getDefaultInstance().getLabel(label, capitlize);
		return lbl;
	}

	public static String get(final String label, final Object... params) {
		final String lbl = getDefaultInstance().getLabel(label, params);
		return lbl;
	}

	// /////////////////////////////////////////////////////////////////////
	public static Lables getDefaultInstance() {
		if (instance == null) {
			try {
				instance = new Lables(LABLES_FILE_NAME);
			} catch (final FileNotFoundException e) {
				// Default lables will be used
				instance = new Lables();
			}
		}
		return instance;
	}

	public final Properties prop = new Properties();

	// /////////////////////////////////////////////////////////////////////
	private Lables() {
	}

	// // /////////////////////////////////////////////////////////////////////
	// public void load(InputStream in) {
	// try {
	// addLables(FilesUtil.readPropertyStream(in));
	// } catch (IOException e) {
	// ExceptionUtil.handle(e);
	// }
	// }

	// // /////////////////////////////////////////////////////////////////////
	private Lables(final InputStream in) {
		if (in!= null) {
			addLables(GeneralUtility.readPropertyStream(in));
		}
	}

	// /////////////////////////////////////////////////////////////////////
	private Lables(final String lablesFileName) throws FileNotFoundException {
		this(GeneralUtility.getFileInputStream(lablesFileName));
	}

	// // /////////////////////////////////////////////////////////////////////
	// public static String get(String label) {
	// return getDefaultInstance().getLabel(label);
	// }

	public void addLables(final InputStream stream) throws IOException {
		addLables(GeneralUtility.readPropertyStream(stream));
	}

	public void addLables(final List<Lable> lables) {
		for (final Lable lable : lables) {
			setProperty(lable.getLableKey(), lable.getLableValue());
		}
	}

	// /////////////////////////////////////////////////////////////////////
	public void addLables(final Properties lables) {
		final Enumeration keys = lables.keys();
		while (keys.hasMoreElements()) {
			final String key = (String) keys.nextElement();
			this.prop.put(key, fixValue(lables.getProperty(key)));
		}
	}

	// /////////////////////////////////////////////////////////////////////
	public void clear() {
		this.prop.clear();
	}

	// /////////////////////////////////////////////////////////////////////
	public String fixValue(String value) {
		if (value != null && !value.equals("")) {
			final String[] words = value.toLowerCase().split("_");
			value = "";
			for (final String word : words) {
				if (word.length() > 1) {
					value += word.substring(0, 1).toUpperCase() + word.substring(1) + " ";
				} else {
					value = word;
				}
			}
		}
		if (value.contains("\\n")) {
			value = value.replace("\\n", System.getProperty("line.separator"));
		}
		return value.replaceAll("-", " ");
	}

	// /////////////////////////////////////////////////////////////////////
	public String getLabel(final String caption, final boolean captizliseFirstLetter) {
		return FormatUtil.capitalizeFirstLetters(getLabel(caption));
	}

	// // /////////////////////////////////////////////////////////////////////
	//
	// public static String get(String string, String compName) {
	// return Lables.get(string);
	// }

	// /////////////////////////////////////////////////////////////////////
	public String getLabel(String key, final Object... params) {
		if (key == null || key.trim().equals("")) {
			return "";
		}
		key = CollectionUtil.fixPropertyKey(key);
		if (this.prop.containsValue(key)) {
			// to avoid calling the values as keys
			return key;
		}
		String value = this.prop.getProperty(key);
		// Fix the string only if not found
		if (value == null || value.equals("")) {
			if (new Boolean(System.getProperty("labels.missing.dump", "false"))) {
				System.err.println("Missing Lable : " + key);
			}
			value = key;
			// }
			// if (!value.equals("")) {
			value = fixValue(value);
			// to avoid printing the error statement again
			setProperty(key, value);
		}
		return setParamsters(value, params);
	}

	// /////////////////////////////////////////////////////////////////////
	public void printLabels(final OutputStream out) throws IOException {
		final Enumeration keys = this.prop.keys();
		final OutputStreamWriter writer = new OutputStreamWriter(out);
		while (keys.hasMoreElements()) {
			final String key = (String) keys.nextElement();
			final String value = this.prop.getProperty(key);
			writer.write(key + "=" + value + "\n");
		}
	}

	// // /////////////////////////////////////////////////////////////////////
	// public void loadIntoDefaultLables(InputStream in) {
	// getDefaultInstance().addLables(lbl.prop);
	// }

	// /////////////////////////////////////////////////////////////////////
	private String setParamsters(String value, final Object[] params) {
		for (int i = 0; i < params.length; i++) {
			value = value.replaceAll("\\{" + i + "\\}", params[i].toString());
		}
		return value;
	}

	// /////////////////////////////////////////////////////////////////////
	protected void setProperty(final String propName, final String value) {
		this.prop.setProperty(CollectionUtil.fixPropertyKey(propName), value);
	}

}
