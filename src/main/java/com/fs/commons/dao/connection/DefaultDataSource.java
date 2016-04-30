package com.fs.commons.dao.connection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import com.fs.commons.application.config.CommonsConfigManager;
import com.fs.commons.application.config.DefaultConfigManager;
import com.fs.commons.dao.dynamic.meta.generator.DataBaseAnaylser;
import com.fs.commons.dao.dynamic.meta.generator.DefaultDatabaseAnalasyer;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.mysql.MySqlDatabaseAnalasyer;
import com.fs.commons.dao.oracle.OracleDatabaseAnaylaser;
import com.fs.commons.util.GeneralUtility;

/**
 * TODO : Make the separate classes of each DBMS 
 * @author Jalal
 *
 */
public class DefaultDataSource extends AbstractDataSource {

	private String host;

	private String userName;

	private int port;

	private String password;

	private String dbName;

	private DefaultConfigManager config;

	private String driverName;

	private String dbUrl;

	private boolean encoded;

	/**
	 * 
	 */
	public DefaultDataSource() {
		config = new DefaultConfigManager();
		init();
	}

	public DefaultDataSource(String configFileName) throws IOException {
		config = new DefaultConfigManager(GeneralUtility.getFileInputStream(configFileName));
		init();
	}

	/**
	 * @param config
	 */
	private void init() {
		encoded = config.getPropertyAsBoolean(PROPERTY_ENCODED, true);
		host = config.getProperty(PROPERTY_DB_HOST, null);
		port = config.getPropertyAsInteger(PROPERTY_DB_PORT, 0);
		userName = config.getProperty(PROPERTY_DB_USER, null, encoded);
		password = config.getProperty(PROPERTY_DB_PASSWORD, null, encoded);
		dbName = config.getProperty(PROPERTY_DB_NAME, null);
		dbUrl = config.getProperty(PROPERTY_DB_URL, null);

		setDriverName(config.getProperty(PROPERTY_DRIVER_NAME,"mysql"));
	}

	@Override
	public String getDriverName() {
		return driverName;
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

	@Override
	public String getDatabaseHost() {
		return host;
	}

	@Override
	/**
	 * of oracle , it will return the SID , if mysql it will return the database name
	 * Please look at getDefaultDatabaseName
	 */
	public String getDatabaseName() {
		return dbName;
	}

	@Override
	public int getDatabasePort() {
		return port;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public void setDatabaseName(String databseName) {
		this.dbName = databseName;
	}

	@Override
	public String getProperty(String name, String defaultValue) {
		return config.getProperty(name, defaultValue);
	}

	@Override
	public Properties getProperties() {
		return config.getProperties();
	}

	@Override
	public String getDatabaseUrl() {
		// /////////////////////////////////////////////////////////////////////////////////////////////////////////
		if (dbUrl == null) {
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
		return dbUrl;
	}

	private String buildOracleUrl() {
		return "jdbc:oracle:thin:@" + getDatabaseHost() + ":" + getDatabasePort() + ":" + getDatabaseName();
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////
	private String buildMySqlUrl() {
		StringBuffer buf = new StringBuffer();
		buf.append("jdbc:mysql://" + getDatabaseHost() + ":" + getDatabasePort() + "/" + getDatabaseName());
		buf.append("?");
		buf.append("characterEncoding=utf8");
		// buf.append("&allowMultiQueries=true");
		buf.append("&dumpQueriesOnException=true");
		// buf.append("&useCompression=true");
//		buf.append("&autoReconnect=true");
//		buf.append("&cacheResultSetMetadata=true");
		// buf.append("&useJvmCharsetConverters=true");
		// buf.append("&largeRowSizeThreshold=1");
		// buf.append("&rewriteBatchedStatements=true");
		// buf.append("&cacheServerConfiguration=true");
		return buf.toString();
	}

	public CommonsConfigManager getConfigManager() {
		return config;
	}

	@Override
	public DataBaseAnaylser getDatabaseAnasyaler() throws DaoException, SQLException {
		if (isOracle()) {
			return new OracleDatabaseAnaylaser(this);
		}
		if (isMySql()) {
			return new MySqlDatabaseAnalasyer(this);
		}
		return new DefaultDatabaseAnalasyer(this);
	}

	private boolean isOracle() {
		return getDatabaseUrl().toLowerCase().contains("oracle");
	}

	private boolean isMySql() {
		return getDatabaseUrl().toLowerCase().contains("mysql");
	}

	@Override
	public String getDefaultDatabaseName() {
		return isOracle()?getUsername().toUpperCase():getDatabaseName();
	}

	@Override
	public String getTestQuery() {		
		//TODO : this is specific to mysql
		return  config.getProperty("DB_TEST_QUERY", "SELECT version()");
	}

}
