/**
 * Modification history
 * ====================================================
 * Version    Date         Developer        Purpose 
 * ====================================================
 * 1.1      12/06/2008     Jamil Shreet    -Add the following class : 
 */
package com.fs.commons.reports;

/**
 * @1.1
 * @author jamil
 *
 */
public class EmptyReportException extends ReportException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public EmptyReportException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public EmptyReportException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public EmptyReportException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public EmptyReportException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}
