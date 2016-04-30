/*  Modification history
 * ====================================================
 * Version    Date         Developer        Purpose 
 * ====================================================
 * 1.1      24/8/2008     ahmad ali    -Add this class 

 */

package com.fs.commons.apps.backup;

import java.io.IOException;

import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.logging.Logger;
import com.fs.commons.util.GeneralUtility;

/**
 * @author ahmad
 * 
 */
public class DataBaseBackup {
	
	
	public static final String BACKUPS_FOLDER =  "backups-folder";
	public static final String IS_AUTOMATIC_BACKUP =  "auto-backup";
	
	private final DataSource dataSource;
	private boolean compress;
	private String outputPath;


	// /////////////////////////////////////////////////////////
	public DataBaseBackup() {
		this(DataSourceFactory.getDefaultDataSource());
	}

	// /////////////////////////////////////////////////////////
	public DataBaseBackup(DataSource dataSource ) {
		this.dataSource = dataSource;
		
	}

	// /////////////////////////////////////////////////////////
	public void start() throws IOException, CompressionException {
		DatabaseInfo info = new DatabaseInfo(dataSource);
		String path = getOutputPath();
		if (!(path.equals(""))) {
			String logFilePath = getOutputPath()+ ".log";
			GeneralUtility.writeLog("Before execution the dumb ", logFilePath);
			String sqlPath = path + ".sql";
			info.setFileName(sqlPath);
			String dbCompressedFileName = path + ".zip";
			MySqlUtil.export(info);
			GeneralUtility.writeLog("After execution the dumb ", logFilePath);
			GeneralUtility.writeLog("Before Add AutoCommit ", logFilePath);
			GeneralUtility.addAutocommitFalse(sqlPath);
			GeneralUtility.writeLog("After Add AutoCommit ", logFilePath);
			if (isCompress()) {
				GeneralUtility.writeLog("Before compress", logFilePath);
//				CompressionUtil.compress(info.getFileName(), dbCompressedFileName);
//				CompressionUtil.compressAndSetPassword(info.getFileName(), info.getDatabasePassword());
				GeneralUtility.writeLog("after compress", logFilePath);
				Logger.info("Done Compress sql file");
				GeneralUtility.deleteFile(getOutputPath()+".sql");
				Logger.info("After delete sql file");
			}
		}
	}
	
	// /////////////////////////////////////////////////////////
	public boolean isCompress() {
		return compress;
	}

	// /////////////////////////////////////////////////////////
	public void setCompress(boolean compress) {
		this.compress = compress;
	}



	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	
	public static void main(String[] args) throws IOException, CompressionException {
//		DataBaseBackup dataBaseBackup = new DataBaseBackup();
//		dataBaseBackup.start();
//		GeneralUtility.deleteFile("C:/Documents and Settings/jamil/Desktop/mrk/zxczxczxc.sql");
	}
	
}
