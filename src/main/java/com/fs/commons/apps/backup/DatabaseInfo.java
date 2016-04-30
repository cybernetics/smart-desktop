/**
 * Modification history
 * ====================================================
 * Version    Date         Developer        Purpose 
 * ====================================================
 * 1.1      30/06/2008     Jamil Shreet    -Add the following class : 
 */
package com.fs.commons.apps.backup;

import com.fs.commons.dao.connection.DataSource;

/**
 * @1.1
 * @author ASUS
 * 
 */
public class DatabaseInfo {

	private String databaseHost;
	private int databasePort;
	private String databaseName;
	private String databaseUser;
	private String databasePassword;
	private String fileName;

	public DatabaseInfo() {
	}

	public DatabaseInfo(DataSource con) {
		setDatabaseHost(con.getDatabaseHost());
		setDatabaseName(con.getDatabaseName());
		setDatabasePort(con.getDatabasePort());
		setDatabaseUser(con.getUsername());
		setDatabasePassword(con.getPassword());
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the databaseHost
	 */
	public String getDatabaseHost() {
		return databaseHost;
	}

	/**
	 * @param databaseHost
	 *            the databaseHost to set
	 */
	public void setDatabaseHost(String databaseHost) {
		this.databaseHost = databaseHost;
	}

	/**
	 * @return the databasePort
	 */
	public int getDatabasePort() {
		return databasePort;

	}

	/**
	 * @param databasePort
	 *            the databasePort to set
	 */
	public void setDatabasePort(int databasePort) {
		this.databasePort = databasePort;
	}

	/**
	 * @return the databaseName
	 */
	public String getDatabaseName() {
		return databaseName;
	}

	/**
	 * @param databaseName
	 *            the databaseName to set
	 */
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	/**
	 * @return the databaseUser
	 */
	public String getDatabaseUser() {
		return databaseUser;
	}

	/**
	 * @param databaseUser
	 *            the databaseUser to set
	 */
	public void setDatabaseUser(String databaseUser) {
		this.databaseUser = databaseUser;
	}

	/**
	 * @return the databasePassword
	 */
	public String getDatabasePassword() {
		return databasePassword;
	}

	/**
	 * @param databasePassword
	 *            the databasePassword to set
	 */
	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}
}
