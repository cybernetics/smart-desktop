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
package com.fs.commons.application.exceptions.util;

import java.util.Hashtable;

public class ExceptionHandlerFactory {
	private static Hashtable<String, ExceptionHandler> handlers = new Hashtable<String, ExceptionHandler>();
	private static ExceptionHandler defaultExceptionHandler;

	/**
	 *
	 * @param e
	 * @return
	 */
	public static ExceptionHandler getExceptionHandler(final Throwable e) {
		final ExceptionHandler exceptionHandler = handlers.get(e.getClass().getName());
		if (exceptionHandler != null) {
			return exceptionHandler;
		}
		if (defaultExceptionHandler == null) {
			defaultExceptionHandler = new DefaultExceptionHandler();
		}
		return defaultExceptionHandler;
	}

	/**
	 * Register specific handlers for specific exceptions
	 * 
	 * @param exception
	 * @param handler
	 */
	public static void registerExceptionHandler(final Class clas, final ExceptionHandler handler) {
		handlers.put(clas.getName(), handler);
	}

	/**
	 *
	 * @param handler
	 */
	public static void setDefaultHandler(final ExceptionHandler handler) {
		ExceptionHandlerFactory.defaultExceptionHandler = handler;
	}
}
