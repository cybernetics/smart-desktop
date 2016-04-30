// package com.fs.commons.dao.dynamic.meta.generator;
//
// import java.sql.Connection;
// import java.sql.DatabaseMetaData;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.util.ArrayList;
// import java.util.Hashtable;
//
// import javax.sql.rowset.CachedRowSet;
//
// import com.fs.commons.dao.DefaultDao;
// import com.fs.commons.dao.connection.ConnectionManager;
// import com.fs.commons.dao.dynamic.meta.FieldMeta;
// import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
// import com.fs.commons.dao.dynamic.meta.IdFieldMeta;
// import com.fs.commons.dao.dynamic.meta.TableMeta;
// import com.fs.commons.dao.exception.DaoException;
//
// public class DataBaseAnaylazer {
//
// private DatabaseMetaData meta;
//
// private ConnectionManager connectionManager;
// //
// ///////////////////////////////////////////////////////////////////////////
// // public DataBaseAnaylazer() throws SQLException, DaoException {
// //
// this(ConnectionManagerFactory.getDefaultConnectionManager().getConnection());
// // }
//
// private DefaultDao dao;
//
// // /////////////////////////////////////////////////////////////////////////
// // private DataBaseAnaylazer(Connection connection) throws SQLException {
// // try{
// // meta = connection.getMetaData();
// // }finally{
// // connection.close();
// // }
// //
// // }
// // /////////////////////////////////////////////////////////////////////////
// public DataBaseAnaylazer(ConnectionManager connectionManager) throws
// DaoException, SQLException {
// this.connectionManager = connectionManager;
// Connection connection = connectionManager.getConnection();
// try {
// meta = connection.getMetaData();
// dao = new DefaultDao(this.connectionManager);
// } finally {
// connectionManager.close(connection);
// }
// }
//
// // /////////////////////////////////////////////////////////////////////////
// public ArrayList<String> getDatabaseNames() throws SQLException {
// ResultSet rs = meta.getCatalogs();
// ArrayList<String> catalogNames = new ArrayList<String>();
// for (int count = 0; rs.next(); count++) {
// for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
// catalogNames.add(rs.getString(i + 1));
// }
// }
// rs.close();
// return catalogNames;
// }
//
// // /////////////////////////////////////////////////////////////////////////
// public ArrayList<TableMeta> getTables(String databaseName) throws
// SQLException, DaoException {
// ResultSet rs = meta.getTables(databaseName, null, null, new String[] {
// "TABLE" });
//
// ArrayList<TableMeta> tables = new ArrayList<TableMeta>();
// for (int counter = 0; rs.next(); counter++) {
// String tableName = rs.getString("TABLE_NAME");
// TableMeta meta = new TableMeta();
// meta.setTableName(tableName);
// meta.setIdField(getIdField(databaseName, tableName));
// meta.setFieldList(getFields(databaseName, tableName));
// tables.add(meta);
// }
// rs.close();
// return tables;
// }
//
// //
// ///////////////////////////////////////////////////////////////////////////////////////////////////////////
// public Hashtable<String, TableMeta> getDyanmicMeta() throws SQLException,
// DaoException {
// if (connectionManager == null) {
// throw new IllegalStateException("calling getDyanmicMeta is not allowed
// without connectionManager");
// }
// return getDyanmicMeta(connectionManager.getDatabaseName());
// }
//
// //
// ///////////////////////////////////////////////////////////////////////////////////////////////////////////
// public Hashtable<String, TableMeta> getDyanmicMeta(String databaseName)
// throws SQLException, DaoException {
// ArrayList<TableMeta> tables = getTables(databaseName);
// Hashtable<String, TableMeta> hash = new Hashtable<String, TableMeta>();
// for (TableMeta tableMeta : tables) {
// hash.put(tableMeta.getTableName(), tableMeta);
// }
// return hash;
// }
//
// //
// ///////////////////////////////////////////////////////////////////////////////////////////////////////////
// public ArrayList<FieldMeta> getFields(String catalogName, String tableName)
// throws SQLException, DaoException {
// TableMeta tableMeta = new TableMeta();// to temp deal with fields
// // ArrayList<FieldMeta> fields=new ArrayList<FieldMeta>();
// IdFieldMeta idField = getIdField(catalogName, tableName);
// tableMeta.setIdField(idField);
// ArrayList<ForiegnKeyFieldMeta> foriegnFields = getForiegnKeys(catalogName,
// tableName);
// // tableMeta.addFields(foriegnFields);
// ResultSet rs = meta.getColumns(catalogName, null, tableName, null);
// for (int counter = 0; rs.next(); counter++) {
// String fieldName = rs.getString("COLUMN_NAME");
// int type = rs.getInt("DATA_TYPE");
// int nullable = rs.getInt("NULLABLE");
// String defaultValue = rs.getString("COLUMN_DEF");
// int maxLength = rs.getInt("COLUMN_SIZE");
// // int decimalDigits=rs.getInt("DECIMAL_DIGITS");
// FieldMeta fieldMeta;
// boolean newField = true;
// // the following check is used for primary keys and foreign keys
// // which is already added to the list
// if ((fieldMeta = tableMeta.getField(fieldName, true)) != null) {
// newField = false;
// } else {
// fieldMeta = new FieldMeta();
// fieldMeta.setName(fieldName);
// int index;
// if ((index = foriegnFields.indexOf(fieldMeta)) != -1) {
// fieldMeta = foriegnFields.get(index);
// }
// }
// fieldMeta.setMaxLength(maxLength);
// fieldMeta.setType(type);
// fieldMeta.setRequired((nullable == 0 ? true : false));
// if (defaultValue != null && !defaultValue.equals("null")) {
// fieldMeta.setDefaultValue(defaultValue);
// }
// if (newField) {
// fieldMeta.setParentTable(tableMeta);
// tableMeta.addField(fieldMeta);
// }
// }
// return tableMeta.getFieldList();
// }
//
// /**
// *
// * @param catalogName
// * @param tableName
// * @return
// * @throws SQLException
// */
// private ArrayList<ForiegnKeyFieldMeta> getForiegnKeys(String catalogName,
// String tableName) throws SQLException {
// ResultSet rs = meta.getImportedKeys(catalogName, null, tableName);
// ArrayList<ForiegnKeyFieldMeta> fields = new ArrayList<ForiegnKeyFieldMeta>();
// while (rs.next()) {
// ForiegnKeyFieldMeta field = new ForiegnKeyFieldMeta();
// field.setName(rs.getString("FKCOLUMN_NAME"));
// field.setReferenceTable(rs.getString("PKTABLE_NAME"));
// field.setReferenceField(rs.getString("PKCOLUMN_NAME"));
// fields.add(field);
// }
// return fields;
// }
//
// /**
// *
// * @param catalogName
// * @param tableName
// * @return
// * @throws SQLException
// * @throws DaoException
// */
// private IdFieldMeta getIdField(String catalogName, String tableName) throws
// SQLException, DaoException {
// ResultSet rs = meta.getPrimaryKeys(catalogName, null, tableName);
// if (rs.next()) {
// IdFieldMeta id = new IdFieldMeta();
// id.setName(rs.getString("COLUMN_NAME"));
// // add the single cot to make the compiler read it as string
// // and recognize it as a DB name
// CachedRowSet rowSet = dao.executeQuery("select * from `" + catalogName + "`."
// + tableName + " limit 1");
// // add limit 1 to return single record
// id.setAutoIncrement(rowSet.getMetaData().isAutoIncrement(1));
// return id;
// }
// return null;
// }
//
// public boolean isTableExist(String tableName) throws SQLException,
// DaoException {
// ArrayList<TableMeta> tables = getTables(connectionManager.getDatabaseName());
// for (TableMeta tableMeta : tables) {
// if(tableMeta.getTableName().trim().equalsIgnoreCase(tableName)){
// return true;
// }
// }
// return false;
// }
// }
