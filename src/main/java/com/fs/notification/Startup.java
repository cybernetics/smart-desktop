package com.fs.notification;

import java.io.FileNotFoundException;

import com.fs.commons.application.ApplicationException;
import com.fs.commons.application.ApplicationManager;
import com.fs.commons.util.ExceptionUtil;

public class Startup {
	public static void main(String[] args) {
		try {
			ApplicationManager instance = ApplicationManager.getInstance();
			instance.init();
			instance.start();
		} catch (FileNotFoundException e) {
			ExceptionUtil.handleException(e);
			System.exit(0);
		} catch (ApplicationException e) {
			ExceptionUtil.handleException(e);
			System.exit(0);
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
			System.exit(0);
		}
	}
}
