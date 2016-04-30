package com.fs.commons.bean.util;

public class BeanUtilException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BeanUtilException(String string, Exception e) {
		super(string, e);
	}

	public BeanUtilException(String string) {
		super(string);
	}

	public BeanUtilException(Exception e) {
		super(e);
	}

}
