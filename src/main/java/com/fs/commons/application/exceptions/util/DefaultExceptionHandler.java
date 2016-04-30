package com.fs.commons.application.exceptions.util;
/**
 * 
 * @author Administrator
 *
 */
public class DefaultExceptionHandler<T extends Throwable> implements ExceptionHandler<T>{

	@Override
	public void handleException(T e) {
		e.printStackTrace();
	}

	@Override
	public void showError(Throwable e, String message, String string, boolean b) {
		System.err.println(message+" - "+ e.getMessage());
	}
	
}