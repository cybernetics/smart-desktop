package com.fs.commons.apps.executors;

import com.fs.commons.application.ApplicationException;
import com.fs.commons.application.ApplicationManager;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.util.ExceptionUtil;

public class LogoutExecutor implements Runnable {

	@Override
	public void run() {
		if (SwingUtility.showConfirmationDialog("ARE_YOUR_SURE_YOU_WANT_TO_LOG_OUT")) {
			try {
				ApplicationManager.getInstance().logout();
			} catch (ApplicationException e) {
				ExceptionUtil.handleException(e);
			}			
		}
	}

}
