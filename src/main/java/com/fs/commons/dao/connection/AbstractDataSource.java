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
	public void close(Connection con) {
		try {
			if (con != null && !con.isClosed() && con != queryConnection) {
				// GeneralUtility.printStackTrace();
				if (isDebug()) {
					System.out.println("closing connection : Current connection " + (--connectionsCount));
				}
				// System.err.println("closing connection");
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ////////////////////////////////////////////////////////////////////////////
	public void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {

			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////////
	public void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////////
	public Connection getConnection() throws DaoException {
		if (!driverClassLoaded) {
			loadDriverClass();
			driverClassLoaded = true;
		}
		try {
			// System.err.println("Createing new connection");
			// GeneralUtility.printStackTrace();
			if (isDebug()) {
				System.out.println("Creating connection , current opened connections : " + (++connectionsCount));
			}
			return connect();
		} catch (Exception e) {
			try {
				GeneralUtility.checkServer(getDatabaseHost(), getDatabasePort());
				throw new DaoException(
						Lables.getDefaultInstance().getLabel("UNABLE_TO_CONNECT_TO_DATABASE_AT_HOST", true) + ": " + getDatabaseHost(), e);
			} catch (ServerDownException ex) {
				throw new DaoException(ex);
			}
		}
	}

	private boolean isDebug() {
		return new Boolean(System.getProperty("fs.connection.debug", "false"));
	}

	private void loadDriverClass() {
		try {
			Class.forName(getDriverName());
		} catch (ClassNotFoundException e) {
			ExceptionUtil.handleException(e);
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected Connection connect() throws SQLException {
		return DriverManager.getConnection(getDatabaseUrl(), getUsername(), getPassword());
	}

	// ////////////////////////////////////////////////////////////////////////////
	public void close(Connection connection, boolean commit) throws DaoException {
		try {
			if (commit) {
				connection.commit();
			} else {
				connection.rollback();
			}
			close(connection);
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	// ////////////////////////////////////////////////////////////////////////////
	public Session createSession() throws DaoException {
		if (parentSession == null || parentSession.isClosed()) {
			parentSession = new Session(this);
			return parentSession;
		}
		return new Session(parentSession);
	}

	// ////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDatabaseName(String databseName) {
	}

	// ////////////////////////////////////////////////////////////////////////////
	@Override
	public int getQueryLimit() {
		return Integer.parseInt(getProperty(PROPERTY_QUERY_LIMIT, DEFAULT_LIMIT + ""));
	}

	@Override
	public Connection getQueryConnection() throws DaoException {
		if (queryConnection == null) {
			queryConnection = getConnection();
		}
		return queryConnection;
		// System.out.println("\n\n///////////////////////////////////////////////////////////////\n");
		// GeneralUtility.printStackTrace();
		// return getConnection();
	}


}