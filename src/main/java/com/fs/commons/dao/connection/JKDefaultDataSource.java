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
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.fs.commons.application.config.CommonsConfigManager;
import com.fs.commons.application.config.ConfigManagerFactory;
import com.fs.commons.application.config.DefaultConfigManager;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.meta.generator.DataBaseAnaylser;
import com.fs.commons.dao.dynamic.meta.generator.DefaultDatabaseAnalasyer;
import com.fs.commons.dao.mysql.MySqlDatabaseAnalasyer;
import com.fs.commons.dao.oracle.OracleDatabaseAnaylaser;
import com.fs.commons.util.GeneralUtility;

/**
 * TODO : Make the separate classes of each DBMS
 * 
 * @author Jalal
 *
 */
public abstract class JKDefaultDataSource extends JKAbstractDataSource {

	private String host;

	private String userName;

	private int port;

	private String password;

	private String dbName;

	private final DefaultConfigManager config;

	private String driverName;

	private String dbUrl;

	private boolean encoded;

	/**
	 *
	 */
	public JKDefaultDataSource() {
		this.config = ConfigManagerFactory.getDefaultConfigManager();
		init();
	}

	public JKDefaultDataSource(final String configFileName) throws IOException {
		this.config =  ConfigManagerFactory.DefaultConfigManager(GeneralUtility.getFileInputStream(configFileName));
		init();
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////
	private String buildMySqlUrl() {
		final StringBuffer buf = new StringBuffer();
		buf.append("jdbc:mysql://" + getDatabaseHost() + ":" + getDatabasePort() + "/" + getDatabaseName());
		buf.append("?");
		buf.append("characterEncoding=utf8");
		 buf.append("&allowMultiQueries=true");
		buf.append("&dumpQueriesOnException=true");
//		buf.append("&allowMultiQueries=true");
		// buf.append("&useCompression=true");
		// buf.append("&autoReconnect=true");
		// buf.append("&cacheResultSetMetadata=true");
		// buf.append("&useJvmCharsetConverters=true");
		// buf.append("&largeRowSizeThreshold=1");
		// buf.append("&rewriteBatchedStatements=true");
		// buf.append("&cacheServerConfiguration=true");
		return buf.toString();
	}

	private String buildOracleUrl() {
		return "jdbc:oracle:thin:@" + getDatabaseHost() + ":" + getDatabasePort() + ":" + getDatabaseName();
	}

	public CommonsConfigManager getConfigManager() {
		return this.config;
	}

	@Override
	public DataBaseAnaylser getDatabaseAnasyaler() throws JKDataAccessException, SQLException {
		if (isOracle()) {
			return new OracleDatabaseAnaylaser(this);
		}
		if (isMySql()) {
			return new MySqlDatabaseAnalasyer(this);
		}
		return new DefaultDatabaseAnalasyer(this);
	}

	@Override
	public String getDatabaseHost() {
		return this.host;
	}

	@Override
	/**
	 * of oracle , it will return the SID , if mysql it will return the database
	 * name Please look at getDefaultDatabaseName
	 */
	public String getDatabaseName() {
		return this.dbName;
	}

	@Override
	public int getDatabasePort() {
		return this.port;
	}

	@Override
	public String getDatabaseUrl() {
		// /////////////////////////////////////////////////////////////////////////////////////////////////////////
		if (this.dbUrl == null) {
			// Try to create smart-url
			if (getDriverName() != null) {
				if (getDriverName().toLowerCase().contains("mysql")) {
					return buildMySqlUrl();
				}
				if (getDriverName().toLowerCase().contains("oracle")) {
					return buildOracleUrl();
				}
			}
			throw new RuntimeException("Please set (" + PROPERTY_DB_URL + ") OR (" + PROPERTY_DRIVER_NAME + ")  properties");
		}
		return this.dbUrl;
	}

	@Override
	public String getDefaultDatabaseName() {
		return isOracle() ? getUsername().toUpperCase() : getDatabaseName();
	}

	@Override
	public String getDriverName() {
		return this.driverName;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public Properties getProperties() {
		return this.config.getProperties();
	}

	@Override
	public String getProperty(final String name, final String defaultValue) {
		return this.config.getProperty(name, defaultValue);
	}

	@Override
	public String getTestQuery() {
		// TODO : this is specific to mysql
		return this.config.getProperty("DB_TEST_QUERY", "SELECT version()");
	}

	@Override
	public String getUsername() {
		return this.userName;
	}

	/**
	 * @param config
	 */
	private void init() {
		this.encoded = this.config.getPropertyAsBoolean(PROPERTY_ENCODED, true);
		this.host = this.config.getProperty(PROPERTY_DB_HOST, null);
		this.port = this.config.getPropertyAsInteger(PROPERTY_DB_PORT, 0);
		this.userName = this.config.getProperty(PROPERTY_DB_USER, null, this.encoded);
		this.password = this.config.getProperty(PROPERTY_DB_PASSWORD, null, this.encoded);
		this.dbName = this.config.getProperty(PROPERTY_DB_NAME, null);
		this.dbUrl = this.config.getProperty(PROPERTY_DB_URL, null);

		setDriverName(this.config.getProperty(PROPERTY_DRIVER_NAME, "mysql"));
	}

	private boolean isMySql() {
		return getDatabaseUrl().toLowerCase().contains("mysql");
	}

	private boolean isOracle() {
		return getDatabaseUrl().toLowerCase().contains("oracle");
	}

	@Override
	public void setDatabaseName(final String databseName) {
		this.dbName = databseName;
	}

	public void setDriverName(String driverName) {
		if (driverName != null) {
			if (driverName.toLowerCase().equals("oracle")) {
				driverName = "oracle.jdbc.driver.OracleDriver";
			}
			if (driverName.toLowerCase().equals("mysql")) {
				driverName = "com.mysql.jdbc.Driver";
			}
		}
		this.driverName = driverName;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected Connection connect() throws SQLException {
		return DriverManager.getConnection(getDatabaseUrl(), getUsername(), getPassword());
	}
}
