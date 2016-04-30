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
package com.fs.commons.dao;

public class DaoException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -3953279828253839850L;

	public DaoException() {
		super();
		// TODO Auto-generated constructor stub
	}

	// public DaoException(String message, Throwable cause, boolean
	// enableSuppression, boolean writableStackTrace) {
	// super(message, cause, enableSuppression, writableStackTrace);
	// // TODO Auto-generated constructor stub
	// }

	public DaoException(final String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public DaoException(final String message, final Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public DaoException(final Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
