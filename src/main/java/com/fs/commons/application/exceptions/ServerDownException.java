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
package com.fs.commons.application.exceptions;

import java.net.ConnectException;
import java.net.UnknownHostException;

public class ServerDownException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -2582331807098593228L;
	private int port;
	private String host;

	/**
	 *
	 * @param ex
	 * @param host
	 * @param port
	 */
	public ServerDownException(final Exception ex, final String host, final int port) {
		super(ex);
		this.host = host;
		this.port = port;
	}

	public ServerDownException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return this.host;
	}

	@Override
	public String getMessage() {
		if (getCause() instanceof UnknownHostException) {
			return "Host (" + getHost() + ") is unreachable !!!!";
		}
		if (getCause() instanceof ConnectException) {
			return "Host (" + getHost() + ") is unreachable at Port (" + getPort() + ")!!!!";
		}
		return getCause().getMessage();
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(final String host) {
		this.host = host;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(final int port) {
		this.port = port;
	}

}
