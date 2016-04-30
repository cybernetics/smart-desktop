package com.fs.commons.desktop.validation.builtin;

import com.fs.commons.locale.Lables;

public class NbBundle {

	public static String getMessage(Class class1, String string, String compName, String model) {
		return Lables.get(string,compName);
	}

	public static String getMessage(Class class1, String string, String compName) {
		return Lables.get(string,compName);
	}

	public static String getMessage(Class class1, String string, String compName, String curr, String charsetName) {
		return Lables.get(string,compName);
	}

	public static String getMessage(Class class1, String string, int port) {
		return Lables.get(string,port);
	}

	public static String getMessage(Class  class1, String string, Object[] objects) {
		return Lables.get(string,objects);
	}

}