/*
 * Copyright 2002-2016 Jalal Kiswani.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fs.commons.apps.backup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.util.DateTimeUtil;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

/**
 *
 * @author Mohamed Kiswani
 *
 */
public class AutomaticDBBackup extends DataBaseBackup {

	private static String DEFAULT_BACKUP_FOLDER = "backups";

	////////////////////////////////////////////////////////////////////////////////////
	// todo: should be moved to utility calss because it use in several places
	//
	////////////////////////////////////////////////////////////////////////////////////
	public static String getProperty(final String propName, final String defultValue) {
		return DataSourceFactory.getDefaultDataSource().getProperty(propName, defultValue);
	}

	//////////////////////////////////////////////////////////////////////
	public static void main(final String[] args) {
		final String a = JOptionPane.showInputDialog("Please number 1 ");
		final int anumber = Integer.parseInt(a);
		final String b = JOptionPane.showInputDialog("Please number 2");
		final int bnumber = Integer.parseInt(b);
		System.out.println(anumber + bnumber);
	}

	////////////////////////////////////////////////////////////////////////
	public static void processDatabaseAutobackup() {
		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					final AutomaticDBBackup backup = new AutomaticDBBackup();
					backup.startBackup(false);
				} catch (final Exception e) {
					SwingUtility.showUserErrorDialog("UNABLE_TO_PROCESS_AUTOAMATIC_BACKUP");
					ExceptionUtil.handleException(e);
				}
			}
		});
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
	}

	//////////////////////////////////////////////////////////////////////////////////////
	public AutomaticDBBackup() {
		setBackupsFolder();
	}

	////////////////////////////////////////////////////////////////////////
	private String getLogFileName() {
		return getOutputPath() + ".log";
	}

	//////////////////////////////////////////////////////////////////////////////////////
	@Override
	public String getOutputPath() {
		return DEFAULT_BACKUP_FOLDER + File.separator + DateTimeUtil.formatCurrentDatabaseDate();
	}

	//////////////////////////////////////////////////////////////////////////////////////
	public boolean isAutomaticBackupAllwoed() {
		// String isAutomaticBackEnabled =
		// getProperty(DataBaseBackup.IS_AUTOMATIC_BACKUP, "false");
		// return Boolean.valueOf(isAutomaticBackEnabled);
		return false;
	}

	////////////////////////////////////////////////////////////////////////
	protected boolean isBackupCreated() {
		final boolean fileExist = GeneralUtility.isFileExist(getLogFileName());
		if (fileExist) {
			System.err.println("Backup for today is already created");
		}
		return fileExist;
	}

	//////////////////////////////////////////////////////////////////////////////////////
	public void setBackupsFolder() {
		DEFAULT_BACKUP_FOLDER = getProperty(DataBaseBackup.BACKUPS_FOLDER, "backups");
	}

	////////////////////////////////////////////////////////////////////////////////////
	public void startBackup(final boolean overwrite) throws IOException, CompressionException {
		if (overwrite || !isBackupCreated()) {
			if (!GeneralUtility.isDebugMode() && isAutomaticBackupAllwoed()) {
				writeLogFile();
				setCompress(true);
				start();
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////////
	private void writeLogFile() {
		BufferedWriter out = null;
		FileWriter fileWriter = null;
		try {
			GeneralUtility.createDirectory(DEFAULT_BACKUP_FOLDER);
			fileWriter = new FileWriter(getLogFileName());
			out = new BufferedWriter(fileWriter);
		} catch (final IOException e) {
			ExceptionUtil.handleException(e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (final IOException e) {
				}
			}
		}

	}
}
