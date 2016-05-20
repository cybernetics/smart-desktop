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
package com.fs.commons.dao.connection;

import java.sql.Connection;
import java.util.logging.Logger;

import com.fs.commons.application.ApplicationManager;
import com.fs.commons.application.exceptions.ServerDownException;
import com.fs.commons.dao.JKDataAccessException;
import com.jk.logging.JKLogger;
import com.jk.logging.JKLoggerFactory;

public class JKDataSourceFactory {
	static JKLogger logger = JKLoggerFactory.getLogger(JKDataSourceFactory.class);
	private static JKDataSource defaultResourceManager;

	/**
	 * @throws Exception
	 *
	 */
	private static void checkApplicationInitialized() throws Exception {
		try {
			logger.debug("ApplicationManager.getInstance().init()");
			ApplicationManager.getInstance().init();
		} catch (final Exception e) {
			throw e;
			// if this occurs , it would be handled by the getResourceManager
			// which will throw NullPointerException for non
			// Initialized resource manager
		}
	}

	/**
	 * @return
	 */
	public static JKDataSource getDefaultDataSource() {
		if (defaultResourceManager == null) {
			try {
				logger.debug("checkApplicationInitialized();");
				checkApplicationInitialized();
			} catch (final Exception e) {
				throw new IllegalStateException("Unable to Initialize the Application due to the following error:\n" + e.getMessage(), e);
			}
		}
		return defaultResourceManager;
	}

	/**
	 *
	 * @param impl
	 * @throws ServerDownException
	 * @throws JKDataAccessException
	 */
	public static void setDefaultDataSource(final JKDataSource impl) throws ServerDownException, JKDataAccessException {
		logger.debug("setDefaultDataSource");
		defaultResourceManager = impl;
		// check weather
//		GeneralUtility.checkDatabaseServer(impl.getDatabaseName(), impl.getDatabaseHost(), impl.getDatabasePort());
		// try to get connect to the database to check valid username and
		// password
		Connection connection = null;
		try {
			logger.debug("check database connetion");
			connection = impl.getQueryConnection();
		} finally {
			impl.close(connection);
		}
	}
}
