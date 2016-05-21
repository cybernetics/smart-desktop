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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.apache.commons.dbcp.BasicDataSource;

import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.util.GeneralUtility;
import com.jk.logging.JKLogger;
import com.jk.logging.JKLoggerFactory;

public class JKPoolingDataSource extends JKDefaultDataSource {
	JKLogger logger = JKLoggerFactory.getLogger(getClass());
	private BasicDataSource datasource;

	// ////////////////////////////////////////////////////////////////////////////
	public JKPoolingDataSource() {
		super();
		init();
	}

	// ////////////////////////////////////////////////////////////////////////////
	public JKPoolingDataSource(final String configFileName) throws IOException {
		super(configFileName);
		init();
	}

	// ////////////////////////////////////////////////////////////////////////////
	@Override
	protected Connection connect() throws SQLException {
		logger.debug("trying to get connection from datasource");
		if (connectionsCount >= getMaxPoolSize()) {
			GeneralUtility.printStackTrace();
			SwingUtility.showUserErrorDialog("CONNECTION_POOL_IS_FULL");
		}
		// return
		// DriverManager.getConnection(getUrl(),getUsername(),getPassword());
		// if(connectionsCount>3){
		// GeneralUtility.printStackTrace();
		// System.exit(0);
		// }
		final Connection connection = this.datasource.getConnection();
		// to void any connections updates
		// connection.setAutoCommit(true);
		return connection;
	}

	// ////////////////////////////////////////////////////////////////////////////
	private int getInitialPoolSize() {
		return Integer.parseInt(getProperty("db-initial-size", "2"));
	}

	// ////////////////////////////////////////////////////////////////////////////
	private int getMaxPoolSize() {
		return Integer.parseInt(getProperty("db-max-active", "10"));
	}

	// @Override
	// public Connection getQueryConnection() throws DaoException {
	// return getQueryConnection();
	// try {
	// testConnection(queryConnection);
	// } catch (final SQLException e) {
	// queryConnection = null;
	// throw new DaoException("DATABASE_DOWN_ERROR", e);
	// }
	// return queryConnection;
	// }

	// ////////////////////////////////////////////////////////////////////////////
	private void init() {

		this.datasource = new BasicDataSource();
		this.datasource.setDriverClassName(super.getDriverName());
		this.datasource.setUrl(getDatabaseUrl());
		this.datasource.setUsername(getUsername());
		this.datasource.setPassword(getPassword());
		this.datasource.setInitialSize(getInitialPoolSize());
		// datasource.setMaxIdle(Integer.parseInt(getProperty("db-max-idle",
		// "5")));
		// datasource.setPoolPreparedStatements(Boolean.parseBoolean(getProperty("db-cache-ps","true")));
		this.datasource.setMaxActive(getMaxPoolSize());
		logger.info("Init database connection-pool(", getInitialPoolSize(), "-", getMaxPoolSize(), ")...");

		logger.debug(this.datasource.toString());
		// datasource.setMaxWait(1000);
	}

	// private void testConnection(final Connection queryConnection) throws
	// SQLException {
	// PreparedStatement ps = null;
	// try {
	// ps = queryConnection.prepareStatement(getTestQuery());
	// ps.executeQuery();
	// } finally {
	// close(ps);
	// }
	// }
}
