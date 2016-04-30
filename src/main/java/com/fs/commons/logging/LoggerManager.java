package com.fs.commons.logging;

/**
 * 
 * @author mkiswani
 *
 */
public interface LoggerManager {
	public void info(String msg);
	public void printCurrentTime(Object msg) ;
	public void fatal(String msg) ;
}
