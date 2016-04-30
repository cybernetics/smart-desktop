package com.fs.license.comm;

public class HttpException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int errorCode;

	/**
	 * 
	 */
	public HttpException() {
		super();		
	}

	/**
	 * @param message
	 * @param cause
	 */
	public HttpException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param errorCode
	 */
	public HttpException(String message,int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @param cause
	 */
	public HttpException(Throwable cause) {
		super(cause);
	}
}
