package com.fs.commons.application.exceptions.util;

import java.util.Hashtable;

public class ExceptionHandlerFactory {
	private static Hashtable<String, ExceptionHandler> handlers=new Hashtable<String, ExceptionHandler>();
	private static ExceptionHandler defaultExceptionHandler;
	
	/**
	 * Register specific handlers for specific exceptions
	 * @param exception
	 * @param handler
	 */
	public static void registerExceptionHandler(Class clas, ExceptionHandler handler){
		handlers.put(clas.getName(), handler);
	}

	/**
	 * 
	 * @param e 
	 * @return
	 */
	public static ExceptionHandler getExceptionHandler(Throwable e) {
		ExceptionHandler exceptionHandler = handlers.get(e.getClass().getName());
		if(exceptionHandler!=null){
			return exceptionHandler;
		}
		if (defaultExceptionHandler == null) {
			defaultExceptionHandler = new DefaultExceptionHandler();
		}
		return defaultExceptionHandler;
	}

	/**
	 * 
	 * @param handler
	 */
	public static void setDefaultHandler(ExceptionHandler handler) {
		ExceptionHandlerFactory.defaultExceptionHandler = handler;
	}
}


