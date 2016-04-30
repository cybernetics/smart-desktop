package com.fs.license;

public class LicenseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int errorCode;

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * 
	 * @param message
	 */
	public LicenseException(String message) {
		super(message);
	}

	public LicenseException() {
		super();
	}

	public LicenseException(String message, Throwable cause) {
		super(message, cause);
	}

	public LicenseException(Throwable cause) {
		super(cause);
	}

	public LicenseException(String string, int errorCode) {
		super(string);
		setErrorCode(errorCode);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

}
