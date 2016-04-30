package com.fs.commons.desktop.swing.dialogs;

import java.util.TimerTask;

import javax.swing.JDialog;

import com.sun.awt.AWTUtilities;

public class Fader extends TimerTask {

	private JDialog jDialog;

	public Fader(JDialog jDialog) {
		this.jDialog = jDialog;
	}

	// As Fader extends from Timer, it's the run() method which does the main
	// job
	@Override
	public void run() {
		// The opacity is reduced by 0,01f steps
		// If this value equals 0 (invisible), we close the JDialog with
		// dispose()
		if (AWTUtilities.getWindowOpacity(jDialog) > 0.01f) {
			AWTUtilities.setWindowOpacity(jDialog, AWTUtilities.getWindowOpacity(jDialog) - 0.01f);
		} else {
			//jDialog.dispose();
		}
	}
}