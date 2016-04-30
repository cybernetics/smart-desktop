package com.fs.license;

public class LicenseAlreadyExistsException extends LicenseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LicenseAlreadyExistsException() {
		super();
	}

	public LicenseAlreadyExistsException(String string, int errorCode) {
		super(string, errorCode);
	}

	public LicenseAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public LicenseAlreadyExistsException(String message) {
		super(message);
	}

	public LicenseAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	public LicenseAlreadyExistsException(int errorCode) {
		setErrorCode(errorCode);
	}

}
