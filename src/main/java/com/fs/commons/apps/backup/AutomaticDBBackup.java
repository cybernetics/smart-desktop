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
	//////////////////////////////////////////////////////////////////////////////////////
	public AutomaticDBBackup() {
		setBackupsFolder();
	}
	//////////////////////////////////////////////////////////////////////////////////////
	@Override
	public String getOutputPath() {
		return DEFAULT_BACKUP_FOLDER + File.separator + DateTimeUtil.formatCurrentDatabaseDate();
	}
	//////////////////////////////////////////////////////////////////////////////////////
	public void setBackupsFolder(){
		DEFAULT_BACKUP_FOLDER=getProperty(DataBaseBackup.BACKUPS_FOLDER, "backups");
	}
	//////////////////////////////////////////////////////////////////////////////////////
	public boolean isAutomaticBackupAllwoed(){
//		String isAutomaticBackEnabled = getProperty(DataBaseBackup.IS_AUTOMATIC_BACKUP, "false");
//		return  Boolean.valueOf(isAutomaticBackEnabled);
		return false;
	}
	////////////////////////////////////////////////////////////////////////////////////
	//  todo: should be moved to utility calss because it use in several places
	//
	////////////////////////////////////////////////////////////////////////////////////
	public static String getProperty(String propName, String defultValue) {
		return DataSourceFactory.getDefaultDataSource().getProperty(propName,defultValue);
	}
	////////////////////////////////////////////////////////////////////////////////////
	public void startBackup(boolean overwrite) throws IOException, CompressionException {
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
		} catch (IOException e) {
			ExceptionUtil.handleException(e);
		}finally{
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {}
			}
		}

	}
	////////////////////////////////////////////////////////////////////////
	protected boolean isBackupCreated() {
		boolean fileExist = GeneralUtility.isFileExist(getLogFileName());
		if (fileExist) {
			System.err.println("Backup for today is already created");
		}
		return fileExist;
	}
	////////////////////////////////////////////////////////////////////////
	private String getLogFileName() {
		return getOutputPath() + ".log";
	}
	////////////////////////////////////////////////////////////////////////
	public static void processDatabaseAutobackup() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					AutomaticDBBackup backup = new AutomaticDBBackup();
					backup.startBackup(false);
				} catch (Exception e) {
					SwingUtility.showUserErrorDialog("UNABLE_TO_PROCESS_AUTOAMATIC_BACKUP");
					ExceptionUtil.handleException(e);
				}
			}
		});
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
	}
	//////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		String a = JOptionPane.showInputDialog("Please number 1 ");
		int anumber =  Integer.parseInt(a);
		String b = JOptionPane.showInputDialog("Please number 2");
		int bnumber =  Integer.parseInt(b);
		System.out.println(anumber+bnumber);
	}
}
