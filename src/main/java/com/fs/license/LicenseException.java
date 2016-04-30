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
package com.fs.license;

public class LicenseException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	int errorCode;

	public LicenseException() {
		super();
	}

	/**
	 *
	 * @param message
	 */
	public LicenseException(final String message) {
		super(message);
	}

	public LicenseException(final String string, final int errorCode) {
		super(string);
		setErrorCode(errorCode);
	}

	public LicenseException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public LicenseException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return this.errorCode;
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(final int errorCode) {
		this.errorCode = errorCode;
	}

}
