package com.fs.commons.dao.dynamic.meta.generator;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.rowset.CachedRowSet;

import com.fs.commons.dao.DefaultDao;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.IdFieldMeta;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.exception.DaoException;

public abstract class AbstractDataBaseAnaylazer implements DataBaseAnaylser {

	private DatabaseMetaData meta;

	private DataSource connectionManager;

	private DefaultDao dao;

	// ///////////////////////////////////////////////////////////////////////
	public AbstractDataBaseAnaylazer() throws DaoException, SQLException {
		this(DataSourceFactory.getDefaultDataSource());
	}

	// /////////////////////////////////////////////////////////////////////////
	public AbstractDataBaseAnaylazer(DataSource connectionManager) throws DaoException, SQLException {
		this.connectionManager = connectionManager;
		Connection connection = connectionManager.getQueryConnection();
		try {
			meta = connection.getMetaData();
			dao = new DefaultDao(this.connectionManager);
		} finally {
			connectionManager.close(connection);
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	@Override
	public ArrayList<String> getCatalogsName() throws SQLException {
		ResultSet rs = meta.getCatalogs();
		ArrayList<String> catalogNames = new ArrayList<String>();
		int counter = 1;
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
	public ArrayList<String> getSchemas() throws SQLException {
		ResultSet rs = meta.getSchemas();
		ArrayList<String> schemaNames = new ArrayList<String>();
		while (rs.next()) {
			// for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
			schemaNames.add(rs.getString("TABLE_SCHEM"));
			// }
		}
		rs.close();
		return schemaNames;
	}

	@Override
	public ArrayList<TableMeta> getTablesMeta() throws SQLException, DaoException {
		return getTablesMeta(this.connectionManager.getDefaultDatabaseName());
	}

	// /////////////////////////////////////////////////////////////////////////
	@Override
	public ArrayList<TableMeta> getTablesMeta(String databaseName) throws SQLException, DaoException {
		ResultSet rs = loadTableNamesFromMeta(meta, databaseName);
		ArrayList<TableMeta> tables = new ArrayList<TableMeta>();
		while (rs.next()) {
			String tableType = rs.getString("TABLE_TYPE");
			if (tableType.toUpperCase().equals("TABLE")) {
				String tableName = rs.getString("TABLE_NAME");
				TableMeta meta = getTable(databaseName, tableName);
				tables.add(meta);
			}
		}
		rs.close();
		return tables;
	}

	// //////////////////////////////////////////////////////////////////////////////
	protected TableMeta getTable(String tableName) throws SQLException, DaoException {
		return getTable(connectionManager.getDefaultDatabaseName(), tableName);
	}

	// //////////////////////////////////////////////////////////////////////////////
	protected TableMeta getTable(String databaseName, String tableName) throws SQLException, DaoException {
//		System.out.println("Fetching table : " + tableName);
		TableMeta meta = new TableMeta();
		meta.setTableName(tableName);
		// meta.setIdField(getIdField(databaseName, tableName));
		loadFields(databaseName, meta);
		return meta;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.fs.commons.dao.dynamic.meta.generator.DataBaseAnaylser1#getDyanmicMeta
	 * (java.lang.String)
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

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.fs.commons.dao.dynamic.meta.generator.DataBaseAnaylser1#getFields
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void loadFields(String database, TableMeta tableMeta) throws SQLException, DaoException {
//		System.out.println("Processing table : "+tableMeta.getTableName());
		IdFieldMeta idField = getIdField(database, tableMeta.getTableName());
		tableMeta.setIdField(idField);
		ArrayList<ForiegnKeyFieldMeta> foriegnFields = getForiegnKeys(database, tableMeta.getTableName());
		for (ForiegnKeyFieldMeta foriegnKeyFieldMeta : foriegnFields) {
			foriegnKeyFieldMeta.setParentTable(tableMeta);
		}
		
		ResultSet rs = getTableColumnsFromMeta(meta, database, tableMeta.getTableName());
		while (rs.next()) {
			String fieldName = rs.getString("COLUMN_NAME");
//			System.out.println("Processing field : "+fieldName);
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
			int type = rs.getInt("DATA_TYPE");
			int nullable = rs.getInt("NULLABLE");
			String defaultValue = rs.getString("COLUMN_DEF");
			int maxLength = rs.getInt("COLUMN_SIZE");
			// int decimalDigits=rs.getInt("DECIMAL_DIGITS");

			fieldMeta.setMaxLength(maxLength);
			fieldMeta.setType(type);
			fieldMeta.setRequired((nullable == 0 ? true : false));
			if (defaultValue != null && !defaultValue.equals("null")) {
				fieldMeta.setDefaultValue(defaultValue);
			}
			if (newField) {				
				tableMeta.addField(fieldMeta);
			}
		}
		rs.close();
	}

	protected ResultSet getTableColumnsFromMeta(DatabaseMetaData meta, String database, String tableName) throws SQLException {
		return meta.getColumns(database, null, tableName, null);
	}

	/**
	 * 
	 * @param databaseName
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	private ArrayList<ForiegnKeyFieldMeta> getForiegnKeys(String databaseName, String tableName) throws SQLException {
		ResultSet rs = getImportedKeys(meta, databaseName, tableName);
		ArrayList<ForiegnKeyFieldMeta> fields = new ArrayList<ForiegnKeyFieldMeta>();
		while (rs.next()) {
			ForiegnKeyFieldMeta field = new ForiegnKeyFieldMeta();
			field.setName(rs.getString("FKCOLUMN_NAME"));
			field.setReferenceTable(rs.getString("PKTABLE_NAME"));
			field.setReferenceField(rs.getString("PKCOLUMN_NAME"));
			fields.add(field);
		}
		rs.close();
		return fields;
	}

	// //////////////////////////////////////////////////////////////////////////
	protected ResultSet getImportedKeys(DatabaseMetaData meta, String databaseName, String tableName) throws SQLException {
		return meta.getImportedKeys(databaseName, null, tableName);
	}

	/**
	 * 
	 * @param databaseName
	 * @param tableName
	 * @return
	 * @throws SQLException
	 * @throws DaoException
	 */
	private IdFieldMeta getIdField(String databaseName, String tableName) throws SQLException, DaoException {
		ResultSet rs = getPrimaryKeysFromMeta(meta, databaseName, tableName);
		try {
			if (rs.next()) {
				IdFieldMeta id = new IdFieldMeta();
				id.setName(rs.getString("COLUMN_NAME"));
				boolean autoIncrement = isAutoIncrement(databaseName, tableName);
				id.setAutoIncrement(autoIncrement);
				return id;
			}
			return null;
		} finally {
			rs.close();
		}

	}

	// //////////////////////////////////////////////////////////////////////////////////////
	protected boolean isAutoIncrement(String databaseName, String tableName) throws DaoException, SQLException {
		String emptyRowQuery = buildEmptyRowQuery(databaseName, tableName);
//		System.out.println("Executing : " + emptyRowQuery);
		CachedRowSet rowSet = dao.executeQuery(emptyRowQuery);
		boolean autoIncrement = rowSet.getMetaData().isAutoIncrement(1);
		return autoIncrement;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	protected ResultSet getPrimaryKeysFromMeta(DatabaseMetaData meta, String databaseName, String tableName) throws SQLException {
		return meta.getPrimaryKeys(databaseName, null, tableName);
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isTableExist(String tableName) throws SQLException, DaoException {
		ArrayList<TableMeta> tables = getTablesMeta(connectionManager.getDatabaseName());
		for (TableMeta tableMeta : tables) {
			if (tableMeta.getTableName().trim().equalsIgnoreCase(tableName)) {
				return true;
			}
		}
		return false;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) throws DaoException, SQLException {
		AbstractDataBaseAnaylazer a = (AbstractDataBaseAnaylazer) DataSourceFactory.getDefaultDataSource().getDatabaseAnasyaler();
		System.out.println(a.connectionManager.getDefaultDatabaseName());
		//ResultSet tableTypes = a.meta.getTableTypes();
		//ResultSet rs=a.getPrimaryKeysFromMeta(a.meta, "FINANCE", "SEC_USERS");
//		 ResultSet resultSet = a.loadTableNamesFromMeta(a.meta,"FINANCE");
		//a.dao.printRecordResultSet(rs,true);
		// ArrayList<String> databaseNames = a.getDatabasesName();
		// for (String databaseName : databaseNames) {
		// System.out.println(databaseName);
		System.out.println("-----------------------------------------------------");
//		System.out.println(a.getIdField( "FINANCE", "sec_users/"));
		System.out.println(a.getTable("sec_users"));
//		System.out.println();
		// }
		// System.out.println(a.getSchemas());

		System.out.println("Done");

		// OracleDatabaseAnaylaser o = new OracleDatabaseAnaylaser();
		// TableMeta meta = o.getTable("RW_OTHER_EMPLOYEES");
		// System.out.println(meta);
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	@Override
	public ArrayList<String> getDatabasesName() throws SQLException {
		return getCatalogsName();
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	protected ResultSet loadTableNamesFromMeta(DatabaseMetaData meta, String dbName) throws SQLException {
		return meta.getTables(dbName, null, null, new String[] { "TABLE" });
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	protected abstract String buildEmptyRowQuery(String databaseName, String tableName);
	// /////////////////////////////////////////////////////////////////////////////////////

}
