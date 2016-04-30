package com.fs.commons.apps.executors;

import com.fs.commons.desktop.swing.SwingUtility;

/**
 * @author u087
 * 
 */
public class CloseExecutor implements Runnable {

	/*
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		if (SwingUtility.showConfirmationDialog("EXIT_CONFIRMATION")) {
			//SwingUtility.showSuccessDialog("PLEASE_TAKE_DATABASE_BACKUP._GOOD_BYE...");
			System.exit(0);
		}
	}

}
