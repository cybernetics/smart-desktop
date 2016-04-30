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
			TableMetaFactory defaultFactory = new TableMetaFactory(defaultConnectionManager);
			metaFactorys.put(defaultConnectionManager, defaultFactory);
		} catch (DaoException e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 * If if connectinManager is not defined , the system will start look inside
	 * other meta
	 * 
	 * @param tableName
	 * @return
	 * @throws TableMetaNotFoundException
	 */
	public static TableMeta getTableMeta(String tableName) throws TableMetaNotFoundException {
		TableMetaFactory defaultMetaFactory = getDefaultMetaFactory();
		if (defaultMetaFactory.isMetaExists(tableName)) {
			return defaultMetaFactory.getTableMeta(tableName);
		}
		// look in other connections
		Enumeration<DataSource> keys = metaFactorys.keys();
		while (keys.hasMoreElements()) {
			TableMetaFactory tableMetaFactory = metaFactorys.get(keys.nextElement());
			if (defaultMetaFactory != tableMetaFactory) {
				if (tableMetaFactory.isMetaExists(tableName)) {
					return tableMetaFactory.getTableMeta(tableName);
				}
			}
		}
		throw new TableMetaNotFoundException("TableMeta : " + tableName + " doesnot exists");
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	public static TableMeta getTableMeta(DataSource connectionManager, String metaName) {
		return metaFactorys.get(connectionManager).getTableMeta(metaName);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////
	// public static void loadMetaFiles(InputStream in) throws
	// FileNotFoundException, JKXmlException {
	// getDefaultMetaFactory().loadMetaFiles(in);
	// }

	// //////////////////////////////////////////////////////////////////////////////////////////////////////
	public static TableMetaFactory addTablesMeta(DataSource connectionManager, Hashtable<String, TableMeta> newTables) throws DaoException {
		TableMetaFactory metaFactory = getMetaFactory(connectionManager);
		metaFactory.addTablesMeta(newTables);
		return metaFactory;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////
	public static TableMetaFactory getMetaFactory(DataSource connectionManager) throws DaoException {
		TableMetaFactory metaFactory = metaFactorys.get(connectionManager);
		if (metaFactory == null) {
			metaFactory = new TableMetaFactory(connectionManager);
			metaFactorys.put(connectionManager, metaFactory);
		}
		return metaFactory;
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
	 * @return
	 */
	public static TableMetaFactory getDefaultMetaFactory() {
		return metaFactorys.get(defaultConnectionManager);
	}

	public static void registerFactory(DataSource manager, TableMetaFactory factory) {
		metaFactorys.put(manager, factory);
	}

}
