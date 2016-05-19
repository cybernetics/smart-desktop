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

import com.fs.commons.dao.JKAbstractPlainDataAccess;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.dao.connection.JKDataSourceFactory;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.mysql.MysqlAbstractDao;
import com.fs.commons.dao.oracle.OracleAbstractDao;
import com.fs.commons.dao.oracle.OracleDynamicDao;

public class DaoFactory {
	public static JKAbstractPlainDataAccess createDao() {
		return createDao(JKDataSourceFactory.getDefaultDataSource());
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public static JKAbstractPlainDataAccess createDao(final JKDataSource dataSource) {
		final String url = dataSource.getDatabaseUrl().toLowerCase();
		if (url.contains("oracle")) {
			return new OracleAbstractDao();
		}
		if (url.contains("mysql")) {
			return new MysqlAbstractDao();
		}
		return new JKAbstractPlainDataAccess();
	}

	public static DynamicDao createDynamicDao(final String tableMetaName) {
		return createDynamicDao(AbstractTableMetaFactory.getTableMeta(tableMetaName));
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public static DynamicDao createDynamicDao(final TableMeta tableMeta) {
		final JKDataSource dataSource = tableMeta.getDataSource();
		if (dataSource.getDatabaseUrl().toLowerCase().contains("oracle")) {
			return new OracleDynamicDao(tableMeta);
		}
		return new DynamicDao(tableMeta);
	}

}
