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
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.util.GeneralUtility;

public class PoolingDataSource extends DefaultDataSource {

	private BasicDataSource datasource;

	// ////////////////////////////////////////////////////////////////////////////
	public PoolingDataSource() {
		super();
		init();
	}

	// ////////////////////////////////////////////////////////////////////////////
	public PoolingDataSource(final String configFileName) throws IOException {
		super(configFileName);
		init();
	}

	// ////////////////////////////////////////////////////////////////////////////
	@Override
	protected Connection connect() throws SQLException {
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
		return Integer.parseInt(getProperty("db-initial-size", "10"));
	}

	// ////////////////////////////////////////////////////////////////////////////
	private int getMaxPoolSize() {
		return Integer.parseInt(getProperty("db-max-active", "10"));
	}

	@Override
	public Connection getQueryConnection() throws DaoException {
		Connection queryConnection = super.getQueryConnection();
		try {
			testConnection(queryConnection);
		} catch (final SQLException e) {
			queryConnection = null;
			throw new DaoException("DATABASE_DOWN_ERROR", e);
		}
		return queryConnection;
	}

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
		System.out.println(this.datasource.getUrl() + "   User : " + getUsername());
		// datasource.setMaxWait(1000);
	}

	private void testConnection(final Connection queryConnection) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = queryConnection.prepareStatement(getTestQuery());
			ps.executeQuery();
		} finally {
			close(ps);
		}
	}
}
