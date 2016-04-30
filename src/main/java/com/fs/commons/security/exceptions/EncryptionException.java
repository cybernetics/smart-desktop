/* Modification History
 * Version		Author		Date		Purpose
 *=============================================================================
 *1.0                 Bashar Nadir    Oct 2005          First version
 *
 */

package com.fs.commons.security.exceptions;

public class EncryptionException extends SecurityException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public EncryptionException() {
	}

	/**
	 * 
	 * @param msg
	 *            String
	 */
	public EncryptionException(String msg) {
		super(msg);
	}

	/**
	 * 
	 * @param msg
	 *            String
	 * @param cause
	 *            Throwable
	 */
	public EncryptionException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
