package com.fs.commons.security.exceptions;

public class SecurityException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public SecurityException() {
	}

	/**
	 * 
	 * @param msg
	 *            String
	 */
	public SecurityException(String msg) {
		super(msg);
	}

	/**
	 * 
	 * @param msg
	 *            String
	 * @param cause
	 *            Throwable
	 */
	public SecurityException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public SecurityException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
