package com.fs.commons.dao.connection;

import java.sql.Connection;

import com.fs.commons.application.ApplicationManager;
import com.fs.commons.application.exceptions.ServerDownException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.util.GeneralUtility;

public class DataSourceFactory {
	private static DataSource defaultResourceManager;

	/**
	 * @return
	 */
	public static DataSource getDefaultDataSource() {
		if (defaultResourceManager == null) {
			try {
				checkApplicationInitialized();
			} catch (Exception e) {
				throw new IllegalStateException("Unable to Initialize the Application due to the following error:\n" + e.getMessage(), e);
			}
		}
		return defaultResourceManager;
	}

	/**
	 * @throws Exception
	 * 
	 */
	private static void checkApplicationInitialized() throws Exception {
		try {
			ApplicationManager.getInstance().init();
		} catch (Exception e) {
			throw e;
			// if this occurs , it would be handled by the getResourceManager
			// which will throw NullPointerException for non
			// Initialized resource manager
		}
	}

	/**
	 * 
	 * @param impl
	 * @throws ServerDownException
	 * @throws DaoException
	 */
	public static void setDefaultDataSource(DataSource impl) throws ServerDownException, DaoException {
		defaultResourceManager = impl;
		// check weather
		GeneralUtility.checkDatabaseServer(impl.getDatabaseName(), impl.getDatabaseHost(), impl.getDatabasePort());
		// try to get connect to the database to check valid username and
		// password
		Connection connection = null;
		try {
			connection = impl.getQueryConnection();
		} finally {
			impl.close(connection);
		}
	}
}
