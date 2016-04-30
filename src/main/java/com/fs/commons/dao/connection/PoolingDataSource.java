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
	public PoolingDataSource(String configFileName) throws IOException {
		super(configFileName);
		init();
	}

	// ////////////////////////////////////////////////////////////////////////////
	private void init() {
		datasource = new BasicDataSource();
		datasource.setDriverClassName(super.getDriverName());
		datasource.setUrl(getDatabaseUrl());
		datasource.setUsername(getUsername());
		datasource.setPassword(getPassword());
		datasource.setInitialSize(getInitialPoolSize());
		// datasource.setMaxIdle(Integer.parseInt(getProperty("db-max-idle",
		// "5")));
		// datasource.setPoolPreparedStatements(Boolean.parseBoolean(getProperty("db-cache-ps","true")));
		datasource.setMaxActive(getMaxPoolSize());
		System.out.println(datasource.getUrl() + "   User : " + getUsername());
		// datasource.setMaxWait(1000);
	}

	// ////////////////////////////////////////////////////////////////////////////
	private int getInitialPoolSize() {
		return Integer.parseInt(getProperty("db-initial-size", "10"));
	}

	// ////////////////////////////////////////////////////////////////////////////
	private int getMaxPoolSize() {
		return Integer.parseInt(getProperty("db-max-active", "10"));
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
		Connection connection = datasource.getConnection();
		// to void any connections updates
		// connection.setAutoCommit(true);
		return connection;
	}

	@Override
	public Connection getQueryConnection() throws DaoException {
		Connection queryConnection = super.getQueryConnection();
		try {
			testConnection(queryConnection);
		} catch (SQLException e) {
			queryConnection = null;
			throw new DaoException("DATABASE_DOWN_ERROR", e);
		}
		return queryConnection;
	}

	private void testConnection(Connection queryConnection) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps=queryConnection.prepareStatement(getTestQuery());
			ps.executeQuery();
		} finally {
			close(ps);
		}
	}
}
