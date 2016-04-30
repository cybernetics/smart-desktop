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

	private static String LOGS_FOLDER = GeneralUtility.getUserFolderPath(true) + "logs";

	public static String getCallerInfo() {
		return getCallerInfo(1);
	}

	public static String getCallerInfo(final int level) {
		final Throwable e = new Throwable();
		final String caller = e.getStackTrace()[level].getClassName() + "." + e.getStackTrace()[level].getMethodName();
		return caller;
	}

	/**
	 *
	 * @return
	 */
	public static String getLogFileName() {
		final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		final String logFileName = LOGS_FOLDER + "/" + format.format(new Date()) + ".log";
		return logFileName;
	}

	public static String getStackTraceAsString(final Throwable throwable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		throwable.printStackTrace(printWriter);
		return result.toString();
	}

	/**
	 *
	 * @return
	 * @throws IOException
	 */
	public static byte[] getTodayLogFile() throws IOException {
		try {
			final byte[] file = GeneralUtility.readFile(new File(getLogFileName()));
			return file;
		} catch (final FileNotFoundException e) {
			return new byte[] {};
		}
	}

	public static void handleException(final Throwable e) {
		System.err.println("Handling Exception: ");
		e.printStackTrace(System.err);
		ExceptionHandlerFactory.getExceptionHandler(e).handleException(e);
	}

	/**
	 *
	 * @throws FileNotFoundException
	 */
	public static void initExceptionLogging() throws FileNotFoundException {
		// check if log folder exists
		final File file = new File(LOGS_FOLDER);
		if (!file.exists()) {
			file.mkdir();
		}
		System.setErr(new ExceptionLogging(getLogFileName()));
	}

	public static void main(final String[] args) {
		System.out.println(getCallerInfo());
	}
}
