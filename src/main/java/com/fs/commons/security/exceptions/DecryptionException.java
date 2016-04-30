/* Modification History
 * Version		Author		Date		Purpose
 *=============================================================================
 *1.0                 Bashar Nadir    Oct 2005          First version
 *
 */

package com.fs.commons.security.exceptions;

public class DecryptionException extends SecurityException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public DecryptionException() {
	}

	/**
	 * 
	 * @param msg
	 *            String
	 */
	public DecryptionException(String msg) {
		super(msg);
	}

	/**
	 * 
	 * @param msg
	 *            String
	 * @param cause
	 *            Throwable
	 */
	public DecryptionException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
