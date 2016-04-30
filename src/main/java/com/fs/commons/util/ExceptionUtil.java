package com.fs.commons.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fs.commons.application.exceptions.util.ExceptionHandlerFactory;
import com.fs.commons.application.exceptions.util.ExceptionLogging;

public class ExceptionUtil {

	private static String LOGS_FOLDER = GeneralUtility.getUserFolderPath(true)+ "logs";

	/**
	 * 
	 * @throws FileNotFoundException
	 */
	public static void initExceptionLogging() throws FileNotFoundException {
		// check if log folder exists
		File file = new File(LOGS_FOLDER);
		if (!file.exists()) {
			file.mkdir();
		}
		System.setErr(new ExceptionLogging(getLogFileName()));
	}

	/**
	 * 
	 * @return
	 */
	public static String getLogFileName() {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		String logFileName = LOGS_FOLDER + "/" + format.format(new Date()) + ".log";
		return logFileName;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public static byte[] getTodayLogFile() throws IOException {
		try {
			byte[] file = GeneralUtility.readFile(new File(getLogFileName()));
			return file;
		} catch (FileNotFoundException e) {
			return new byte[] {};
		}
	}

	public static void handleException(Throwable e) {
		System.err.println("Handling Exception: ");
		e.printStackTrace(System.err);
		ExceptionHandlerFactory.getExceptionHandler(e).handleException(e);
	}

	public static String getCallerInfo() {
		return getCallerInfo(1);
	}

	public static String getCallerInfo(int level) {
		Throwable e = new Throwable();
		String caller = e.getStackTrace()[level].getClassName() + "." + e.getStackTrace()[level].getMethodName();
		return caller;
	}

	public static void main(String[] args) {
		System.out.println(getCallerInfo());
	}

	public static String getStackTraceAsString(Throwable throwable) {
		final Writer result = new StringWriter();
	    final PrintWriter printWriter = new PrintWriter(result);
	    throwable.printStackTrace(printWriter);
	    return result.toString();
	}
}
