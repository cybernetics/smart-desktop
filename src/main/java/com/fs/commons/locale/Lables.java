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
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.FormatUtil;
import com.fs.commons.util.GeneralUtility;

public class Lables {
	public static final String LABLES_FILE_NAME = "/lables.properties";

	public final Properties prop = new Properties();
	private static Lables instance;

	// /////////////////////////////////////////////////////////////////////
	public static Lables getDefaultInstance() {
		if (instance == null) {
			try {
				instance = new Lables(LABLES_FILE_NAME);
			} catch (FileNotFoundException e) {
				// Default lables will be used
				instance = new Lables();
			}
		}
		return instance;
	}

	// /////////////////////////////////////////////////////////////////////
	private Lables() {
	}

	// /////////////////////////////////////////////////////////////////////
	private Lables(String lablesFileName) throws FileNotFoundException {
		this(GeneralUtility.getFileInputStream(lablesFileName));
	}

	// // /////////////////////////////////////////////////////////////////////
	private Lables(InputStream in) {
		try {
			addLables(GeneralUtility.readPropertyStream(in));
		} catch (IOException e) {
			ExceptionUtil.handleException(e);
		}
	}

	// // /////////////////////////////////////////////////////////////////////
	// public void load(InputStream in) {
	// try {
	// addLables(FilesUtil.readPropertyStream(in));
	// } catch (IOException e) {
	// ExceptionUtil.handleException(e);
	// }
	// }

	// /////////////////////////////////////////////////////////////////////
	protected void setProperty(String propName, String value) {
		prop.setProperty(CollectionUtil.fixPropertyKey(propName), value);
	}

	// /////////////////////////////////////////////////////////////////////
	public void printLabels(OutputStream out) throws IOException {
		Enumeration keys = prop.keys();
		OutputStreamWriter writer = new OutputStreamWriter(out);
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value = prop.getProperty(key);
			writer.write(key + "=" + value + "\n");
		}
	}

	// // /////////////////////////////////////////////////////////////////////
	// public static String get(String label) {
	// return getDefaultInstance().getLabel(label);
	// }

	// /////////////////////////////////////////////////////////////////////
	public String getLabel(String key, Object... params) {
		if (key == null || key.trim().equals("")) {
			return "";
		}
		key = CollectionUtil.fixPropertyKey(key);
		if (prop.containsValue(key)) {
			// to avoid calling the values as keys
			return key;
		}
		String value = prop.getProperty(key);
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
	private String setParamsters(String value, Object[] params) {
		for (int i = 0; i < params.length; i++) {
			value = value.replaceAll("\\{" + i + "\\}", params[i].toString());
		}
		return value;
	}

	// /////////////////////////////////////////////////////////////////////
	public String getLabel(String caption, boolean captizliseFirstLetter) {
		return FormatUtil.capitalizeFirstLetters(getLabel(caption));
	}

	// /////////////////////////////////////////////////////////////////////
	public String fixValue(String value) {
		if (value != null && !value.equals("")) {
			String[] words = value.toLowerCase().split("_");
			value = "";
			for (String word : words) {
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
	public void addLables(Properties lables) {
		Enumeration keys = lables.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			prop.put(key, fixValue(lables.getProperty(key)));
		}
	}

	// /////////////////////////////////////////////////////////////////////
	public void clear() {
		prop.clear();
	}

	// // /////////////////////////////////////////////////////////////////////
	//
	// public static String get(String string, String compName) {
	// return Lables.get(string);
	// }

	// /////////////////////////////////////////////////////////////////////
	public static String get(String label, boolean capitlize) {
		String lbl = getDefaultInstance().getLabel(label, capitlize);
		return lbl;
	}
	
	public static String get(String label, Object... params) {
		String lbl = getDefaultInstance().getLabel(label, params);
		return lbl;
	}

	// // /////////////////////////////////////////////////////////////////////
	// public void loadIntoDefaultLables(InputStream in) {
	// getDefaultInstance().addLables(lbl.prop);
	// }

	public void addLables(List<Lable> lables) {
		for (Lable lable : lables) {
			setProperty(lable.getLableKey(), lable.getLableValue());
		}
	}

	public void addLables(InputStream stream) throws IOException {
		addLables(GeneralUtility.readPropertyStream(stream));
	}

}
