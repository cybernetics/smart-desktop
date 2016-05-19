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
package com.fs.commons.apps.backup;

import com.fs.commons.dao.connection.JKDataSource;

/**
 * @1.1
 * 
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

	public DatabaseInfo(final JKDataSource con) {
		setDatabaseHost(con.getDatabaseHost());
		setDatabaseName(con.getDatabaseName());
		setDatabasePort(con.getDatabasePort());
		setDatabaseUser(con.getUsername());
		setDatabasePassword(con.getPassword());
	}

	/**
	 * @return the databaseHost
	 */
	public String getDatabaseHost() {
		return this.databaseHost;
	}

	/**
	 * @return the databaseName
	 */
	public String getDatabaseName() {
		return this.databaseName;
	}

	/**
	 * @return the databasePassword
	 */
	public String getDatabasePassword() {
		return this.databasePassword;
	}

	/**
	 * @return the databasePort
	 */
	public int getDatabasePort() {
		return this.databasePort;

	}

	/**
	 * @return the databaseUser
	 */
	public String getDatabaseUser() {
		return this.databaseUser;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * @param databaseHost
	 *            the databaseHost to set
	 */
	public void setDatabaseHost(final String databaseHost) {
		this.databaseHost = databaseHost;
	}

	/**
	 * @param databaseName
	 *            the databaseName to set
	 */
	public void setDatabaseName(final String databaseName) {
		this.databaseName = databaseName;
	}

	/**
	 * @param databasePassword
	 *            the databasePassword to set
	 */
	public void setDatabasePassword(final String databasePassword) {
		this.databasePassword = databasePassword;
	}

	/**
	 * @param databasePort
	 *            the databasePort to set
	 */
	public void setDatabasePort(final int databasePort) {
		this.databasePort = databasePort;
	}

	/**
	 * @param databaseUser
	 *            the databaseUser to set
	 */
	public void setDatabaseUser(final String databaseUser) {
		this.databaseUser = databaseUser;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}
}
