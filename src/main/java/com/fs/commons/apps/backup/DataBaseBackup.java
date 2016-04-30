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

	public static final String BACKUPS_FOLDER = "backups-folder";
	public static final String IS_AUTOMATIC_BACKUP = "auto-backup";

	public static void main(final String[] args) throws IOException, CompressionException {
		// DataBaseBackup dataBaseBackup = new DataBaseBackup();
		// dataBaseBackup.start();
		// GeneralUtility.deleteFile("C:/Documents and
		// Settings/jamil/Desktop/mrk/zxczxczxc.sql");
	}

	private final DataSource dataSource;
	private boolean compress;

	private String outputPath;

	// /////////////////////////////////////////////////////////
	public DataBaseBackup() {
		this(DataSourceFactory.getDefaultDataSource());
	}

	// /////////////////////////////////////////////////////////
	public DataBaseBackup(final DataSource dataSource) {
		this.dataSource = dataSource;

	}

	public String getOutputPath() {
		return this.outputPath;
	}

	// /////////////////////////////////////////////////////////
	public boolean isCompress() {
		return this.compress;
	}

	// /////////////////////////////////////////////////////////
	public void setCompress(final boolean compress) {
		this.compress = compress;
	}

	public void setOutputPath(final String outputPath) {
		this.outputPath = outputPath;
	}

	// /////////////////////////////////////////////////////////
	public void start() throws IOException, CompressionException {
		final DatabaseInfo info = new DatabaseInfo(this.dataSource);
		final String path = getOutputPath();
		if (!path.equals("")) {
			final String logFilePath = getOutputPath() + ".log";
			GeneralUtility.writeLog("Before execution the dumb ", logFilePath);
			final String sqlPath = path + ".sql";
			info.setFileName(sqlPath);
			final String dbCompressedFileName = path + ".zip";
			MySqlUtil.export(info);
			GeneralUtility.writeLog("After execution the dumb ", logFilePath);
			GeneralUtility.writeLog("Before Add AutoCommit ", logFilePath);
			GeneralUtility.addAutocommitFalse(sqlPath);
			GeneralUtility.writeLog("After Add AutoCommit ", logFilePath);
			if (isCompress()) {
				GeneralUtility.writeLog("Before compress", logFilePath);
				// CompressionUtil.compress(info.getFileName(),
				// dbCompressedFileName);
				// CompressionUtil.compressAndSetPassword(info.getFileName(),
				// info.getDatabasePassword());
				GeneralUtility.writeLog("after compress", logFilePath);
				Logger.info("Done Compress sql file");
				GeneralUtility.deleteFile(getOutputPath() + ".sql");
				Logger.info("After delete sql file");
			}
		}
	}

}
