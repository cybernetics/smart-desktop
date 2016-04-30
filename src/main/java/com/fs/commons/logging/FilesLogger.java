package com.fs.commons.logging;

import java.io.File;
import java.io.IOException;

import com.fs.commons.util.DateTimeUtil;
import com.fs.commons.util.GeneralUtility;

/**
 * 
 * @author mkiswani
 *
 */
public class FilesLogger implements LoggerManager{
	private File file = new File("log_"+new java.sql.Date(System.currentTimeMillis())+".txt");
	
	public FilesLogger() throws IOException{
		if(!file.exists()){
			file.createNewFile();
		}
	}
	
	@Override
	public void info(String msg) {
		try {
			msg = "INFO:"+msg+"\n";
			GeneralUtility.writeDataToFile(msg.getBytes(), file, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void printCurrentTime(Object msg) {
		try {
			String currentTime = DateTimeUtil.getCurrentTime(msg+"");
			currentTime = "INFO:"+currentTime+"\n";
			GeneralUtility.writeDataToFile(currentTime.getBytes(), file, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void fatal(String msg) {
		try {
			msg = "FATAL:"+msg+"\n";
			GeneralUtility.writeDataToFile(msg.getBytes(), file, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
