package com.fs.commons.apps.executors;

import com.fs.commons.application.ApplicationManager;

public class ChangeLocaleExecutor implements Runnable {

	@Override
	public void run() {
		ApplicationManager instance = ApplicationManager.getInstance();
		instance.switchLocale();
	}

}
