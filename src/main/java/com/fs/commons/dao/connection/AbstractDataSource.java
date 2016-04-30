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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.fs.commons.application.exceptions.ServerDownException;
import com.fs.commons.dao.Session;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

public abstract class AbstractDataSource implements DataSource {
	protected static int connectionsCount;
	private Session parentSession;
	private Connection queryConnection;
	private boolean driverClassLoaded;

	// ////////////////////////////////////////////////////////////////////////////
	public AbstractDataSource() {
	}

	// ////////////////////////////////////////////////////////////////////////////
	@Override
	public void close(final Connection con) {
		try {
			if (con != null && !con.isClosed() && con != this.queryConnection) {
				// GeneralUtility.printStackTrace();
				if (isDebug()) {
					System.out.println("closing connection : Current connection " + --connectionsCount);
				}
				// System.err.println("closing connection");
				con.close();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	// ////////////////////////////////////////////////////////////////////////////
	@Override
	public void close(final Connection connection, final boolean commit) throws DaoException {
		try {
			if (commit) {
				connection.commit();
			} else {
				connection.rollback();
			}
			close(connection);
		} catch (final SQLException e) {
			throw new DaoException(e);
		}
	}

	// ////////////////////////////////////////////////////////////////////////////
	@Override
	public void close(final ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (final Exception e) {
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////////
	@Override
	public void close(final Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (final Exception e) {

			}
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected Connection connect() throws SQLException {
		return DriverManager.getConnection(getDatabaseUrl(), getUsername(), getPassword());
	}

	// ////////////////////////////////////////////////////////////////////////////
	@Override
	public Session createSession() throws DaoException {
		if (this.parentSession == null || this.parentSession.isClosed()) {
			this.parentSession = new Session(this);
			return this.parentSession;
		}
		return new Session(this.parentSession);
	}

	// ////////////////////////////////////////////////////////////////////////////
	@Override
	public Connection getConnection() throws DaoException {
		if (!this.driverClassLoaded) {
			loadDriverClass();
			this.driverClassLoaded = true;
		}
		try {
			// System.err.println("Createing new connection");
			// GeneralUtility.printStackTrace();
			if (isDebug()) {
				System.out.println("Creating connection , current opened connections : " + (++connectionsCount));
			}
			return connect();
		} catch (final Exception e) {
			try {
				GeneralUtility.checkServer(getDatabaseHost(), getDatabasePort());
				throw new DaoException(Lables.getDefaultInstance().getLabel("UNABLE_TO_CONNECT_TO_DATABASE_AT_HOST", true) + ": " + getDatabaseHost(),
						e);
			} catch (final ServerDownException ex) {
				throw new DaoException(ex);
			}
		}
	}

	@Override
	public Connection getQueryConnection() throws DaoException {
		if (this.queryConnection == null) {
			this.queryConnection = getConnection();
		}
		return this.queryConnection;
		// System.out.println("\n\n///////////////////////////////////////////////////////////////\n");
		// GeneralUtility.printStackTrace();
		// return getConnection();
	}

	// ////////////////////////////////////////////////////////////////////////////
	@Override
	public int getQueryLimit() {
		return Integer.parseInt(getProperty(PROPERTY_QUERY_LIMIT, DEFAULT_LIMIT + ""));
	}

	private boolean isDebug() {
		return new Boolean(System.getProperty("fs.connection.debug", "false"));
	}

	private void loadDriverClass() {
		try {
			Class.forName(getDriverName());
		} catch (final ClassNotFoundException e) {
			ExceptionUtil.handleException(e);
		}
	}

	// ////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDatabaseName(final String databseName) {
	}

}