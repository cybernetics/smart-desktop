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

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.dao.connection.JKDataSourceFactory;
import com.fs.commons.util.GeneralUtility;

/**
 *
 *
 * @author Mohamed Kiswani
 * @since 1-2-2010
 *
 */
public class ImportManager {

	private final String sqlFilePath;

	public ImportManager(final String sqlFilePath) throws ValidationException {
		if (GeneralUtility.isEmpty(sqlFilePath)) {
			throw new ValidationException("SQL_FILE_PATH_CAN_NOT_BE_EMPTY");
		}
		this.sqlFilePath = sqlFilePath;
	}

	public boolean doImport() throws Exception {
		final DatabaseInfo dbInfo = new DatabaseInfo(JKDataSourceFactory.getDefaultDataSource());
		dbInfo.setFileName(this.sqlFilePath);
		MySqlUtil.importDb(dbInfo);
		return true;
	}
}
