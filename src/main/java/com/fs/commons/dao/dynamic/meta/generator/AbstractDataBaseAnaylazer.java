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
package com.fs.commons.dao.dynamic.meta.generator;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.rowset.CachedRowSet;

import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.JKDefaultDataAccess;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.dao.connection.JKDataSourceFactory;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.IdFieldMeta;
import com.fs.commons.dao.dynamic.meta.TableMeta;

public abstract class AbstractDataBaseAnaylazer implements DataBaseAnaylser {

	// /////////////////////////////////////////////////////////////////////////////////////
	public static void main(final String[] args) throws JKDataAccessException, SQLException {
		final AbstractDataBaseAnaylazer a = (AbstractDataBaseAnaylazer) JKDataSourceFactory.getDefaultDataSource().getDatabaseAnasyaler();
		System.out.println(a.connectionManager.getDefaultDatabaseName());
		// ResultSet tableTypes = a.meta.getTableTypes();
		// ResultSet rs=a.getPrimaryKeysFromMeta(a.meta, "FINANCE",
		// "SEC_USERS");
		// ResultSet resultSet = a.loadTableNamesFromMeta(a.meta,"FINANCE");
		// a.dao.printRecordResultSet(rs,true);
		// ArrayList<String> databaseNames = a.getDatabasesName();
		// for (String databaseName : databaseNames) {
		// System.out.println(databaseName);
		System.out.println("-----------------------------------------------------");
		// System.out.println(a.getIdField( "FINANCE", "sec_users/"));
		System.out.println(a.getTable("sec_users"));
		// System.out.println();
		// }
		// System.out.println(a.getSchemas());

		System.out.println("Done");

		// OracleDatabaseAnaylaser o = new OracleDatabaseAnaylaser();
		// TableMeta meta = o.getTable("RW_OTHER_EMPLOYEES");
		// System.out.println(meta);
	}

	private DatabaseMetaData meta;

	private final JKDataSource connectionManager;

	private JKDefaultDataAccess dao;

	// ///////////////////////////////////////////////////////////////////////
	public AbstractDataBaseAnaylazer() throws JKDataAccessException, SQLException {
		this(JKDataSourceFactory.getDefaultDataSource());
	}

	// /////////////////////////////////////////////////////////////////////////
	public AbstractDataBaseAnaylazer(final JKDataSource connectionManager) throws JKDataAccessException, SQLException {
		this.connectionManager = connectionManager;
		final Connection connection = connectionManager.getQueryConnection();
		try {
			this.meta = connection.getMetaData();
			this.dao = new JKDefaultDataAccess(this.connectionManager);
		} finally {
			connectionManager.close(connection);
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	protected abstract String buildEmptyRowQuery(String databaseName, String tableName);
	// /////////////////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	@Override
	public ArrayList<String> getCatalogsName() throws SQLException {
		final ResultSet rs = this.meta.getCatalogs();
		final ArrayList<String> catalogNames = new ArrayList<String>();
		final int counter = 1;
		while (rs.next()) {
			// for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
			catalogNames.add(rs.getString("TABLE_CAT"));
			// }
		}
		rs.close();
		return catalogNames;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	@Override
	public ArrayList<String> getDatabasesName() throws SQLException {
		return getCatalogsName();
	}

	/**
	 *
	 * @param databaseName
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	private ArrayList<ForiegnKeyFieldMeta> getForiegnKeys(final String databaseName, final String tableName) throws SQLException {
		final ResultSet rs = getImportedKeys(this.meta, databaseName, tableName);
		final ArrayList<ForiegnKeyFieldMeta> fields = new ArrayList<ForiegnKeyFieldMeta>();
		while (rs.next()) {
			final ForiegnKeyFieldMeta field = new ForiegnKeyFieldMeta();
			field.setName(rs.getString("FKCOLUMN_NAME"));
			field.setReferenceTable(rs.getString("PKTABLE_NAME"));
			field.setReferenceField(rs.getString("PKCOLUMN_NAME"));
			fields.add(field);
		}
		rs.close();
		return fields;
	}

	/**
	 *
	 * @param databaseName
	 * @param tableName
	 * @return
	 * @throws SQLException
	 * @throws JKDataAccessException
	 */
	private IdFieldMeta getIdField(final String databaseName, final String tableName) throws SQLException, JKDataAccessException {
		final ResultSet rs = getPrimaryKeysFromMeta(this.meta, databaseName, tableName);
		try {
			if (rs.next()) {
				final IdFieldMeta id = new IdFieldMeta();
				id.setName(rs.getString("COLUMN_NAME"));
				final boolean autoIncrement = isAutoIncrement(databaseName, tableName);
				id.setAutoIncrement(autoIncrement);
				return id;
			}
			return null;
		} finally {
			rs.close();
		}

	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * (non-Javadoc)
	 *
	 * @see com.fs.commons.dao.dynamic.meta.generator.DataBaseAnaylser1#
	 * getDyanmicMeta (java.lang.String)
	 */
	// @Override
	// public Hashtable<String, TableMeta> getDyanmicMeta(String databaseName)
	// throws SQLException, DaoException {
	// ArrayList<TableMeta> tables = getTablesMeta(databaseName);
	// Hashtable<String, TableMeta> hash = new Hashtable<String, TableMeta>();
	// for (TableMeta tableMeta : tables) {
	// hash.put(tableMeta.getTableName(), tableMeta);
	// }
	// return hash;
	// }

	// //////////////////////////////////////////////////////////////////////////
	protected ResultSet getImportedKeys(final DatabaseMetaData meta, final String databaseName, final String tableName) throws SQLException {
		return meta.getImportedKeys(databaseName, null, tableName);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	protected ResultSet getPrimaryKeysFromMeta(final DatabaseMetaData meta, final String databaseName, final String tableName) throws SQLException {
		return meta.getPrimaryKeys(databaseName, null, tableName);
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	@Override
	public ArrayList<String> getSchemas() throws SQLException {
		final ResultSet rs = this.meta.getSchemas();
		final ArrayList<String> schemaNames = new ArrayList<String>();
		while (rs.next()) {
			// for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
			schemaNames.add(rs.getString("TABLE_SCHEM"));
			// }
		}
		rs.close();
		return schemaNames;
	}

	// //////////////////////////////////////////////////////////////////////////////
	protected TableMeta getTable(final String tableName) throws SQLException, JKDataAccessException {
		return getTable(this.connectionManager.getDefaultDatabaseName(), tableName);
	}

	// //////////////////////////////////////////////////////////////////////////////
	protected TableMeta getTable(final String databaseName, final String tableName) throws SQLException, JKDataAccessException {
		// System.out.println("Fetching table : " + tableName);
		final TableMeta meta = new TableMeta();
		meta.setTableName(tableName);
		// meta.setIdField(getIdField(databaseName, tableName));
		loadFields(databaseName, meta);
		return meta;
	}

	protected ResultSet getTableColumnsFromMeta(final DatabaseMetaData meta, final String database, final String tableName) throws SQLException {
		return meta.getColumns(database, null, tableName, null);
	}

	@Override
	public ArrayList<TableMeta> getTablesMeta() throws SQLException, JKDataAccessException {
		return getTablesMeta(this.connectionManager.getDefaultDatabaseName());
	}

	// /////////////////////////////////////////////////////////////////////////
	@Override
	public ArrayList<TableMeta> getTablesMeta(final String databaseName) throws SQLException, JKDataAccessException {
		final ResultSet rs = loadTableNamesFromMeta(this.meta, databaseName);
		final ArrayList<TableMeta> tables = new ArrayList<TableMeta>();
		while (rs.next()) {
			final String tableType = rs.getString("TABLE_TYPE");
			if (tableType.toUpperCase().equals("TABLE")) {
				final String tableName = rs.getString("TABLE_NAME");
				final TableMeta meta = getTable(databaseName, tableName);
				tables.add(meta);
			}
		}
		rs.close();
		return tables;
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	protected boolean isAutoIncrement(final String databaseName, final String tableName) throws JKDataAccessException, SQLException {
		final String emptyRowQuery = buildEmptyRowQuery(databaseName, tableName);
		// System.out.println("Executing : " + emptyRowQuery);
		final CachedRowSet rowSet = this.dao.executeQuery(emptyRowQuery);
		final boolean autoIncrement = rowSet.getMetaData().isAutoIncrement(1);
		return autoIncrement;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isTableExist(final String tableName) throws SQLException, JKDataAccessException {
		final ArrayList<TableMeta> tables = getTablesMeta(this.connectionManager.getDatabaseName());
		for (final TableMeta tableMeta : tables) {
			if (tableMeta.getTableName().trim().equalsIgnoreCase(tableName)) {
				return true;
			}
		}
		return false;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.fs.commons.dao.dynamic.meta.generator.DataBaseAnaylser1#getFields
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void loadFields(final String database, final TableMeta tableMeta) throws SQLException, JKDataAccessException {
		// System.out.println("Processing table : "+tableMeta.getTableName());
		IdFieldMeta idField = getIdField(database, tableMeta.getTableName());
		tableMeta.setIdField(idField);
		final ArrayList<ForiegnKeyFieldMeta> foriegnFields = getForiegnKeys(database, tableMeta.getTableName());
		for (final ForiegnKeyFieldMeta foriegnKeyFieldMeta : foriegnFields) {
			foriegnKeyFieldMeta.setParentTable(tableMeta);
		}

		final ResultSet rs = getTableColumnsFromMeta(this.meta, database, tableMeta.getTableName());
		while (rs.next()) {
			final String fieldName = rs.getString("COLUMN_NAME");
			// System.out.println("Processing field : "+fieldName);
			FieldMeta fieldMeta;
			boolean newField = true;
			// the following check is used for primary keys and foreign keys
			// which is already added to the list
			if ((fieldMeta = tableMeta.getField(fieldName, true)) != null) {
				newField = false;
			} else {
				if (idField == null) {
					idField = new IdFieldMeta();
					idField.setAutoIncrement(false);
					tableMeta.setIdField(idField);
					fieldMeta = idField;
					newField = false;
				} else {
					fieldMeta = new FieldMeta();
				}
				fieldMeta.setName(fieldName);
				fieldMeta.setParentTable(tableMeta);
				int index;
				if ((index = foriegnFields.indexOf(fieldMeta)) != -1) {
					fieldMeta = foriegnFields.get(index);
				}
			}
			final int type = rs.getInt("DATA_TYPE");
			final int nullable = rs.getInt("NULLABLE");
			final String defaultValue = rs.getString("COLUMN_DEF");
			final int maxLength = rs.getInt("COLUMN_SIZE");
			// int decimalDigits=rs.getInt("DECIMAL_DIGITS");

			fieldMeta.setMaxLength(maxLength);
			fieldMeta.setType(type);
			fieldMeta.setRequired(nullable == 0 ? true : false);
			if (defaultValue != null && !defaultValue.equals("null")) {
				fieldMeta.setDefaultValue(defaultValue);
			}
			if (newField) {
				tableMeta.addField(fieldMeta);
			}
		}
		rs.close();
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	protected ResultSet loadTableNamesFromMeta(final DatabaseMetaData meta, final String dbName) throws SQLException {
		return meta.getTables(dbName, null, null, new String[] { "TABLE" });
	}

}
