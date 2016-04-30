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
package com.fs.commons.dao.dynamic;

import com.fs.commons.dao.AbstractDao;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.mysql.MysqlAbstractDao;
import com.fs.commons.dao.oracle.OracleAbstractDao;
import com.fs.commons.dao.oracle.OracleDynamicDao;

public class DaoFactory {
	public static AbstractDao createDao() {
		return createDao(DataSourceFactory.getDefaultDataSource());
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public static AbstractDao createDao(final DataSource dataSource) {
		final String url = dataSource.getDatabaseUrl().toLowerCase();
		if (url.contains("oracle")) {
			return new OracleAbstractDao();
		}
		if (url.contains("mysql")) {
			return new MysqlAbstractDao();
		}
		return new AbstractDao();
	}

	public static DynamicDao createDynamicDao(final String tableMetaName) {
		return createDynamicDao(AbstractTableMetaFactory.getTableMeta(tableMetaName));
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public static DynamicDao createDynamicDao(final TableMeta tableMeta) {
		final DataSource dataSource = tableMeta.getDataSource();
		if (dataSource.getDatabaseUrl().toLowerCase().contains("oracle")) {
			return new OracleDynamicDao(tableMeta);
		}
		return new DynamicDao(tableMeta);
	}

}
