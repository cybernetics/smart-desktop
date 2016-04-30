package com.fs.commons.apps.executors;

import com.fs.commons.application.config.UserPreferences;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

public class RestoreDefaultsExecutor implements Runnable{

	@Override
	public void run() {
		try {
			UserPreferences.clear();
			GeneralUtility.clearTempFiles();
			System.exit(0);
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

}
