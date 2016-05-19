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
import java.util.logging.Logger;

import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.dao.connection.JKDataSourceFactory;
import com.jk.exceptions.handler.JKExceptionUtil;

public class AbstractTableMetaFactory {
	static Logger logger = Logger.getLogger(AbstractTableMetaFactory.class.getName());
	static Hashtable<JKDataSource, TableMetaFactory> metaFactorys = new Hashtable<JKDataSource, TableMetaFactory>();
	static JKDataSource defaultConnectionManager = JKDataSourceFactory.getDefaultDataSource();
	static {
		try {
			final TableMetaFactory defaultFactory = new TableMetaFactory(defaultConnectionManager);
			metaFactorys.put(defaultConnectionManager, defaultFactory);
		} catch (final JKDataAccessException e) {
			JKExceptionUtil.handle(e);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////
	public static TableMetaFactory addTablesMeta(final JKDataSource connectionManager, final Hashtable<String, TableMeta> newTables)
			throws JKDataAccessException {
		logger.info("addTablesMeta: " + newTables);
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
	public static TableMetaFactory getMetaFactory(final JKDataSource connectionManager) throws JKDataAccessException {
		TableMetaFactory metaFactory = metaFactorys.get(connectionManager);
		if (metaFactory == null) {
			metaFactory = new TableMetaFactory(connectionManager);
			metaFactorys.put(connectionManager, metaFactory);
		}
		return metaFactory;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	public static TableMeta getTableMeta(final JKDataSource connectionManager, final String metaName) {
		logger.info("getTableMeta :" + metaName);
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
		logger.info("2getTableMeta : " + tableName);
		final TableMetaFactory defaultMetaFactory = getDefaultMetaFactory();
		if (defaultMetaFactory.isMetaExists(tableName)) {
			logger.info("get from default datasource");
			return defaultMetaFactory.getTableMeta(tableName);
		}
		// look in other connections
		logger.info("not found inside default datasource , look into other connections");
		final Enumeration<JKDataSource> keys = metaFactorys.keys();
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

	public static void registerFactory(final JKDataSource manager, final TableMetaFactory factory) {
		metaFactorys.put(manager, factory);
	}

}
