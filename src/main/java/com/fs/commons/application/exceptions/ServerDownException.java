package com.fs.commons.application.exceptions;

import java.net.ConnectException;
import java.net.UnknownHostException;

public class ServerDownException extends Exception {

	private int port;
	private String host;

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * 
	 * @param ex
	 * @param host
	 * @param port
	 */
	public ServerDownException(Exception ex, String host, int port) {
		super(ex);
		this.host = host;
		this.port = port;
	}

	public ServerDownException(Throwable cause) {
		super(cause);
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

}
