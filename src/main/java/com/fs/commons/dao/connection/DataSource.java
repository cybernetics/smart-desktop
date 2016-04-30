package com.fs.commons.dao.connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.fs.commons.dao.Session;
import com.fs.commons.dao.dynamic.meta.generator.DataBaseAnaylser;
import com.fs.commons.dao.exception.DaoException;

public interface DataSource {
	public static final String PROPERTY_ENCODED = "encoded";
	public static final String PROPERTY_DB_HOST = "db-host";
	public static final String PROPERTY_DB_PORT = "db-port";
	public static final String PROPERTY_DB_USER = "db-user";
	public static final String PROPERTY_DB_PASSWORD = "db-password";
	public static final String PROPERTY_DB_NAME = "db-name";
	public static final String PROPERTY_DRIVER_NAME = "db-driver-name";
	public static final String PROPERTY_DB_URL = "db-url";

	public static final String PROPERTY_QUERY_LIMIT = "query-limit";
	public static final int DEFAULT_LIMIT = 5000;

	/**
	 * @return
	 * @throws DaoException
	 */
	public Connection getConnection() throws DaoException;

	/**
	 * @param con
	 */
	public void close(Connection con);

	/**
	 * 
	 * @param stmt
	 */
	public void close(Statement stmt);

	/**
	 * 
	 * @param rs
	 */
	public void close(ResultSet rs);

	/**
	 * 
	 * @param connection
	 * @param commit
	 * @throws DaoException
	 */
	public void close(Connection connection, boolean commit) throws DaoException;

	/**
	 * 
	 * @return
	 * @throws DaoException
	 */
	public Session createSession() throws DaoException;

	/**
	 * @return
	 */
	public String getPassword();

	/**
	 * @return
	 */
	public String getUsername();

	/**
	 * @return
	 */
	public String getDatabaseName();

	/**
	 * @return
	 */
	public int getDatabasePort();

	/**
	 * @return
	 */
	public String getDatabaseHost();

	void setDatabaseName(String databseName);

	public int getQueryLimit();

	public String getProperty(String property, String defaultValue);

	public Properties getProperties();

	public Connection getQueryConnection() throws DaoException;

	public String getDriverName();

	public String getDatabaseUrl();

	public DataBaseAnaylser getDatabaseAnasyaler() throws DaoException, SQLException;

	public String getDefaultDatabaseName();
	
	public String getTestQuery();
}
