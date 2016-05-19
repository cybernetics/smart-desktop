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
package com.fs.commons.dao.mysql;

import java.sql.SQLException;

import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.dao.dynamic.meta.generator.AbstractDataBaseAnaylazer;

public class MySqlDatabaseAnalasyer extends AbstractDataBaseAnaylazer {

	public MySqlDatabaseAnalasyer() throws JKDataAccessException, SQLException {
	}

	public MySqlDatabaseAnalasyer(final JKDataSource connectionManager) throws JKDataAccessException, SQLException {
		super(connectionManager);
	}

	@Override
	protected String buildEmptyRowQuery(final String catalogName, final String tableName) {
		return "select * from `" + catalogName + "`.`" + tableName + "` limit 1";
	}
}
