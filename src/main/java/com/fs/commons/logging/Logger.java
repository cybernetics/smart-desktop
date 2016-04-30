package com.fs.commons.logging;


/**
 * 
 * @author mkiswani
 * 
 */
public class Logger {
	private static LoggerManager loggerManager = null;
	
	private Logger(){
		
	}
	// ///////////////////////////////////////////////////////////////////////
	public static void info(String msg) {
		getLoggerManager().info(msg);
	}

	// ///////////////////////////////////////////////////////////////////////
	public static void printCurrentTime(Object msg) {
		getLoggerManager().printCurrentTime(msg);
	}

	// ///////////////////////////////////////////////////////////////////////
	public static void fatal(String msg) {
		getLoggerManager().fatal(msg);
	}

	

	public static LoggerManager getLoggerManager() {
		if(loggerManager == null){
			loggerManager = new DefalutLogger();
		}
		return loggerManager;
	}

	public static void setLoggerManager(LoggerManager loggerManager) {
		Logger.loggerManager = loggerManager;
	}
}
