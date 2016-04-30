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
package com.fs.commons.dao.dynamic.meta;

import java.util.Enumeration;
import java.util.Hashtable;

import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.util.ExceptionUtil;

public class AbstractTableMetaFactory {
	static Hashtable<DataSource, TableMetaFactory> metaFactorys = new Hashtable<DataSource, TableMetaFactory>();
	static DataSource defaultConnectionManager = DataSourceFactory.getDefaultDataSource();
	static {
		try {
			final TableMetaFactory defaultFactory = new TableMetaFactory(defaultConnectionManager);
			metaFactorys.put(defaultConnectionManager, defaultFactory);
		} catch (final DaoException e) {
			ExceptionUtil.handleException(e);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////
	public static TableMetaFactory addTablesMeta(final DataSource connectionManager, final Hashtable<String, TableMeta> newTables)
			throws DaoException {
		final TableMetaFactory metaFactory = getMetaFactory(connectionManager);
		metaFactory.addTablesMeta(newTables);
		return metaFactory;
	}

	/**
	 * @return
	 */
	public static TableMetaFactory getDefaultMetaFactory() {
		return metaFactorys.get(defaultConnectionManager);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////
	// public static void loadMetaFiles(InputStream in) throws
	// FileNotFoundException, JKXmlException {
	// getDefaultMetaFactory().loadMetaFiles(in);
	// }

	// //////////////////////////////////////////////////////////////////////////////////////////////////////
	public static TableMetaFactory getMetaFactory(final DataSource connectionManager) throws DaoException {
		TableMetaFactory metaFactory = metaFactorys.get(connectionManager);
		if (metaFactory == null) {
			metaFactory = new TableMetaFactory(connectionManager);
			metaFactorys.put(connectionManager, metaFactory);
		}
		return metaFactory;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	public static TableMeta getTableMeta(final DataSource connectionManager, final String metaName) {
		return metaFactorys.get(connectionManager).getTableMeta(metaName);
	}

	// /**
	// * @return
	// */
	// public static Hashtable<String, TableMeta> getTables() {
	// return getDefaultMetaFactory().getTables();
	// }

	// /**
	// *
	// * @return
	// */
	// public static ArrayList<TableMeta> getTablesAsArrayList() {
	// return getDefaultMetaFactory().getTablesAsArrayList();
	// }

	// public static boolean isMetaExists(String metaName) {
	// return getDefaultMetaFactory().isMetaExists(metaName);
	// }

	/**
	 * If if connectinManager is not defined , the system will start look inside
	 * other meta
	 *
	 * @param tableName
	 * @return
	 * @throws TableMetaNotFoundException
	 */
	public static TableMeta getTableMeta(final String tableName) throws TableMetaNotFoundException {
		final TableMetaFactory defaultMetaFactory = getDefaultMetaFactory();
		if (defaultMetaFactory.isMetaExists(tableName)) {
			return defaultMetaFactory.getTableMeta(tableName);
		}
		// look in other connections
		final Enumeration<DataSource> keys = metaFactorys.keys();
		while (keys.hasMoreElements()) {
			final TableMetaFactory tableMetaFactory = metaFactorys.get(keys.nextElement());
			if (defaultMetaFactory != tableMetaFactory) {
				if (tableMetaFactory.isMetaExists(tableName)) {
					return tableMetaFactory.getTableMeta(tableName);
				}
			}
		}
		throw new TableMetaNotFoundException("TableMeta : " + tableName + " doesnot exists");
	}

	public static void registerFactory(final DataSource manager, final TableMetaFactory factory) {
		metaFactorys.put(manager, factory);
	}

}
