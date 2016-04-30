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
package com.fs.commons.logging;

/**
 *
 * @author mkiswani
 *
 */
public class Logger {
	private static LoggerManager loggerManager = null;

	// ///////////////////////////////////////////////////////////////////////
	public static void fatal(final String msg) {
		getLoggerManager().fatal(msg);
	}

	public static LoggerManager getLoggerManager() {
		if (loggerManager == null) {
			loggerManager = new DefalutLogger();
		}
		return loggerManager;
	}

	// ///////////////////////////////////////////////////////////////////////
	public static void info(final String msg) {
		getLoggerManager().info(msg);
	}

	// ///////////////////////////////////////////////////////////////////////
	public static void printCurrentTime(final Object msg) {
		getLoggerManager().printCurrentTime(msg);
	}

	public static void setLoggerManager(final LoggerManager loggerManager) {
		Logger.loggerManager = loggerManager;
	}

	private Logger() {

	}
}
