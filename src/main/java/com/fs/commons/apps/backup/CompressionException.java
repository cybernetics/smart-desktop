/**
 * Modification history
 * ====================================================
 * Version    Date         Developer        Purpose 
 * ====================================================
 * 1.1      30/06/2008     Jamil Shreet    -Add the following class : 
 */
package com.fs.commons.apps.backup;

import com.fs.commons.locale.Lables;

/**
 * @1.1
 * @author ASUS
 * 
 */
public class CompressionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param message
	 * @param e
	 */
	public CompressionException(String message, Exception e) {
		super(Lables.get(message), e);
	}

	/**
	 * 
	 * @param message
	 */
	public CompressionException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param e
	 */
	public CompressionException(Exception e) {
		super(e);
	}
}
