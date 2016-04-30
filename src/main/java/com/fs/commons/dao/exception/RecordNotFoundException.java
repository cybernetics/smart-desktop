package com.fs.commons.dao.exception;


public class RecordNotFoundException extends DaoException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * 
	 */
	public RecordNotFoundException() {
		
	}

	/**
	 * 
	 * @param str
	 */
	public RecordNotFoundException(String str) {
		super(str);
	}

	/**
	 * 
	 * @param str
	 */
	public RecordNotFoundException(Throwable e) {
		super(e);
	}

	/**
	 * 
	 * @param str
	 * @param cause
	 */
	public RecordNotFoundException(String str, Throwable cause) {
		super(str, cause);
	}
}
