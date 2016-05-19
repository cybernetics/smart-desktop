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
package com.fs.commons.dao.oracle;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.dao.dynamic.meta.generator.AbstractDataBaseAnaylazer;

public class OracleDatabaseAnaylaser extends AbstractDataBaseAnaylazer {
	//////////////////////////////////////////////////////////////////////
	public OracleDatabaseAnaylaser() throws JKDataAccessException, SQLException {
	}

	//////////////////////////////////////////////////////////////////////
	public OracleDatabaseAnaylaser(final JKDataSource connectionManager) throws JKDataAccessException, SQLException {
		super(connectionManager);
	}

	//////////////////////////////////////////////////////////////////////
	@Override
	protected String buildEmptyRowQuery(final String databaseName, final String tableName) {
		return "select * from " + databaseName + "." + tableName + " where ROWNUM=1";
	}

	//////////////////////////////////////////////////////////////////////
	@Override
	public ArrayList<String> getDatabasesName() throws SQLException {
		return getSchemas();
	}

	// //////////////////////////////////////////////////////////////////////
	// public static void main(String[] args) throws DaoException, SQLException
	// {
	// OracleDatabaseAnaylaser a=new OracleDatabaseAnaylaser();
	// ArrayList<String> catalogsName = a.getDatabasesName();
	// for (String catalog: catalogsName) {
	// System.out.println(catalog);
	//// ArrayList<String> databases = a.getDatabasesName();
	//// for (String db : databases) {
	//// //System.out.println(db + "-->" + a.getTablesMeta(db));
	//// }
	// }
	// // System.out.println(a.getSchemas());
	//
	// System.out.println("Done");
	//
	// // OracleDatabaseAnaylaser o = new OracleDatabaseAnaylaser();
	// // TableMeta meta = o.getTable("RW_OTHER_EMPLOYEES");
	// // System.out.println(meta);
	// }
	//////////////////////////////////////////////////////////////////////
	@Override
	protected ResultSet getImportedKeys(final DatabaseMetaData meta, final String databaseName, final String tableName) throws SQLException {
		return meta.getImportedKeys(null, databaseName, tableName);
	}

	//////////////////////////////////////////////////////////////////////
	@Override
	protected ResultSet getPrimaryKeysFromMeta(final DatabaseMetaData meta, final String databaseName, final String tableName) throws SQLException {
		return meta.getPrimaryKeys(null, databaseName, tableName);
	}

	//////////////////////////////////////////////////////////////////////
	@Override
	protected ResultSet getTableColumnsFromMeta(final DatabaseMetaData meta, final String database, final String tableName) throws SQLException {
		return meta.getColumns(null, database, tableName, "%");
	}

	@Override
	protected boolean isAutoIncrement(final String databaseName, final String tableName) throws JKDataAccessException, SQLException {
		return false;// since oracle doesnot support auto-increment by default
	}

	//////////////////////////////////////////////////////////////////////
	@Override
	protected ResultSet loadTableNamesFromMeta(final DatabaseMetaData meta, final String dbName) throws SQLException {
		return meta.getTables(null, dbName, null, new String[] { "TABLE" });
	}

}
