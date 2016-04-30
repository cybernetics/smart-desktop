package com.fs.security;

import java.io.FileNotFoundException;

import com.fs.commons.application.ApplicationException;
import com.fs.commons.application.ApplicationManager;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.util.ExceptionUtil;

public class Startup {
	public static void main(String[] args) {
		try {			
			SwingUtility s=new SwingUtility();
			ApplicationManager instance = ApplicationManager.getInstance();
			instance.init();
			instance.start();
		} catch (FileNotFoundException e) {
			ExceptionUtil.handleException(e);
		} catch (ApplicationException e) {
			ExceptionUtil.handleException(e);
		}
		
	}
}
