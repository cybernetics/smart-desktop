/*  Modification history
 * ====================================================
 * Version    Date         Developer        Purpose 
 * ====================================================
 * 1.1      24/8/2008     ahmad ali    -Add this class 

*/


package com.fs.commons.apps.executors;

import java.io.IOException;

import com.fs.commons.apps.backup.CompressionException;
import com.fs.commons.apps.backup.DataBaseBackup;
import com.fs.commons.util.ExceptionUtil;

public class DBExcutor implements Runnable{
	
	public void run() {
		DataBaseBackup dataBaseBackup=new DataBaseBackup();
		try {
			dataBaseBackup.start();
		} catch (IOException e) {
			ExceptionUtil.handleException(e);
		} catch (CompressionException e) {
			ExceptionUtil.handleException(e);
		}
	}
}
