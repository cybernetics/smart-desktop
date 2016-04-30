package com.fs.license;

public class InvalidLicenseException extends LicenseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidLicenseException() {
		super();
	}

	public InvalidLicenseException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidLicenseException(String message) {
		super(message);
	}

	public InvalidLicenseException(Throwable cause) {
		super(cause);
	}

	public InvalidLicenseException(String string, int errorCode) {
		super(string, errorCode);
	}

}
