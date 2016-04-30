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
	 * @param con
	 */
	public void close(Connection con);

	/**
	 *
	 * @param connection
	 * @param commit
	 * @throws DaoException
	 */
	public void close(Connection connection, boolean commit) throws DaoException;

	/**
	 *
	 * @param rs
	 */
	public void close(ResultSet rs);

	/**
	 *
	 * @param stmt
	 */
	public void close(Statement stmt);

	/**
	 *
	 * @return
	 * @throws DaoException
	 */
	public Session createSession() throws DaoException;

	/**
	 * @return
	 * @throws DaoException
	 */
	public Connection getConnection() throws DaoException;

	public DataBaseAnaylser getDatabaseAnasyaler() throws DaoException, SQLException;

	/**
	 * @return
	 */
	public String getDatabaseHost();

	/**
	 * @return
	 */
	public String getDatabaseName();

	/**
	 * @return
	 */
	public int getDatabasePort();

	public String getDatabaseUrl();

	public String getDefaultDatabaseName();

	public String getDriverName();

	/**
	 * @return
	 */
	public String getPassword();

	public Properties getProperties();

	public String getProperty(String property, String defaultValue);

	public Connection getQueryConnection() throws DaoException;

	public int getQueryLimit();

	public String getTestQuery();

	/**
	 * @return
	 */
	public String getUsername();

	void setDatabaseName(String databseName);
}
