package com.fs.commons.dao.oracle;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.dynamic.meta.generator.AbstractDataBaseAnaylazer;
import com.fs.commons.dao.exception.DaoException;

public class OracleDatabaseAnaylaser extends AbstractDataBaseAnaylazer {
	//////////////////////////////////////////////////////////////////////
	public OracleDatabaseAnaylaser() throws DaoException, SQLException {
	}
	//////////////////////////////////////////////////////////////////////
	public OracleDatabaseAnaylaser(DataSource connectionManager) throws DaoException, SQLException {
		super(connectionManager);
	}
	//////////////////////////////////////////////////////////////////////
	@Override
	public ArrayList<String> getDatabasesName() throws SQLException {
		return getSchemas();
	}
	//////////////////////////////////////////////////////////////////////
	@Override
	protected String buildEmptyRowQuery(String databaseName, String tableName) {
		return "select * from " + databaseName + "." + tableName + " where ROWNUM=1";
	}
	//////////////////////////////////////////////////////////////////////
	@Override
	protected ResultSet loadTableNamesFromMeta(DatabaseMetaData meta, String dbName) throws SQLException {
		return meta.getTables(null, dbName, null, new String[] { "TABLE" });
	}
	//////////////////////////////////////////////////////////////////////
	@Override
	protected ResultSet getPrimaryKeysFromMeta(DatabaseMetaData meta, String databaseName, String tableName) throws SQLException {
		return meta.getPrimaryKeys(null, databaseName, tableName);
	}
	//////////////////////////////////////////////////////////////////////
	@Override
	protected ResultSet getTableColumnsFromMeta(DatabaseMetaData meta, String database, String tableName) throws SQLException {
		return meta.getColumns(null, database, tableName, "%");
	}
//	//////////////////////////////////////////////////////////////////////
//	public static void main(String[] args) throws DaoException, SQLException {
//		OracleDatabaseAnaylaser a=new OracleDatabaseAnaylaser();
//		ArrayList<String> catalogsName = a.getDatabasesName();
//		for (String catalog: catalogsName) {
//			System.out.println(catalog);
////			ArrayList<String> databases = a.getDatabasesName();
////			for (String db : databases) {
////				//System.out.println(db + "-->" + a.getTablesMeta(db));
////			}
//		}
//		// System.out.println(a.getSchemas());
//
//		System.out.println("Done");
//
//		// OracleDatabaseAnaylaser o = new OracleDatabaseAnaylaser();
//		// TableMeta meta = o.getTable("RW_OTHER_EMPLOYEES");
//		// System.out.println(meta);
//	}
	//////////////////////////////////////////////////////////////////////
	protected ResultSet getImportedKeys(DatabaseMetaData meta, String databaseName, String tableName) throws SQLException {
		return meta.getImportedKeys(null,databaseName, tableName);
	}
	
	@Override
	protected boolean isAutoIncrement(String databaseName, String tableName) throws DaoException, SQLException {
		return false;//since oracle doesnot support auto-increment by default
	}
	

}
