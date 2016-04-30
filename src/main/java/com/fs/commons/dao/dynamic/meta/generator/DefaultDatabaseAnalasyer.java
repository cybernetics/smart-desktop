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
package com.fs.commons.dao.dynamic.meta.generator;

import java.sql.SQLException;

import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.exception.DaoException;

public class DefaultDatabaseAnalasyer extends AbstractDataBaseAnaylazer {

	public DefaultDatabaseAnalasyer() throws DaoException, SQLException {
	}

	public DefaultDatabaseAnalasyer(final DataSource connectionManager) throws DaoException, SQLException {
		super(connectionManager);
	}

	@Override
	protected String buildEmptyRowQuery(final String catalogName, final String tableName) {
		return "select * from " + catalogName + "." + tableName;
	}

}
