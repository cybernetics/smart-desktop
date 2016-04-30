package com.fs.commons.application.listener;

import com.fs.commons.application.Application;

public interface ApplicationListener{
	
	/**
	 * 
	 * @param application
	 */
	public void applicationLoaded(Application application);
	
	/**
	 * 
	 * @param application
	 */
	public void beforeInit(Application application);
	
	/**
	 * 
	 * @param application
	 */
	public void afterInit(Application application);
	
	/**
	 * 
	 * @param application
	 */
	public void beforeStart(Application application);
	
	/**
	 * 
	 * @param application
	 */
	public void afterStart(Application application);
	
	/**
	 * 
	 * @param application
	 * @param e 
	 */
	public void onException(Application application, Exception e);
}
