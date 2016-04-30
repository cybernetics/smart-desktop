package com.fs.commons.apps.backup;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.util.GeneralUtility;

/**
 * 
 * 
 * @author Mohamed Kiswani
 * @since 1-2-2010
 *
 */
public class ImportManager {
	
	private String sqlFilePath;
	public ImportManager(String sqlFilePath) throws ValidationException{
		if(GeneralUtility.isEmpty(sqlFilePath)){
			throw new ValidationException("SQL_FILE_PATH_CAN_NOT_BE_EMPTY");
		}
		this.sqlFilePath = sqlFilePath;
	}
	
	public boolean doImport() throws Exception {
		DatabaseInfo dbInfo = new DatabaseInfo(DataSourceFactory.getDefaultDataSource());
		dbInfo.setFileName(sqlFilePath);
		MySqlUtil.importDb(dbInfo);
		return true;
	}
}
