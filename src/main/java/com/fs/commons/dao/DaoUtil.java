package com.fs.commons.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.dynamic.DaoFactory;
import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.util.GeneralUtility;

/**
 * TODO : refactor all the methods to use AbstractDao
 * 
 * @author Administrator
 * 
 */
public class DaoUtil {

	// static ConnectionManager manager =
	// ConnectionManagerFactory.getDefaultConnectionManager();
	// static DefaultDao dao=new DefaultDao();
	// /////////////////////////////////////////////////////////////////////////////////////////////
	public static Object exeuteSingleOutputQuery(DataSource con, String query) throws DaoException {
		DefaultDao dao = new DefaultDao(con);
		return dao.exeuteSingleOutputQuery(query);
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	public static Object exeuteSingleOutputQuery(String query) throws DaoException {
		DefaultDao dao = new DefaultDao();
		return dao.exeuteSingleOutputQuery(query);
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	public static CachedRowSet executeQuery(String query) throws DaoException {
		DefaultDao dao = new DefaultDao();
		return dao.executeQuery(query);
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	private static Object readResult(ResultSet rs, Field field) throws SQLException {
		switch (field.getMeta().getType()) {
		case Types.BOOLEAN:
		case Types.TINYINT:
			return rs.getInt(field.getMeta().getName()) == 1;
		case Types.INTEGER:
		case Types.NUMERIC:
		case Types.VARCHAR:
			return rs.getString(field.getMeta().getName());
		case Types.BLOB:
		case Types.VARBINARY:
		case Types.LONGVARBINARY:
		case Types.BINARY:// binary
			return getBinaryStream(rs, field.getMeta().getName());
		case Types.DATE:
			return rs.getDate(field.getMeta().getName());
		case Types.TIME:
			return rs.getTime(field.getMeta().getName());
		}
		return rs.getObject(field.getMeta().getName());
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	public static byte[] getBinaryStream(ResultSet rs, String name) throws SQLException {
		InputStream in = rs.getBinaryStream(name);
		if (in == null) {
			return null;
		}
		byte[] arr = null;
		try {
			arr = GeneralUtility.readStream(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arr;
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	public static Record readRecord(ResultSet rs, TableMeta tableMeta) throws RecordNotFoundException, DaoException, SQLException {
		Record record = tableMeta.createEmptyRecord();
		record.setIdValue(rs.getObject(tableMeta.getIdField().getName()));
		// Field idField = record.getIdField();
		// idField.setValue(rs.getString(idField.getMeta().getName()));
		ArrayList<Field> fields = record.getFields();
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			Object value = readResult(rs, field);
			// Object value = rs.getObject(field.getMeta().getName());
			if (value != null) {
				field.setValue(value);
			}
		}
		record.setNewRecord(false);
		return record;
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	public static List<IdValueRecord> createRecordsFromSQL(String sql) throws DaoException {
		DefaultDao dao = new DefaultDao();
		return dao.createRecordsFromSQL(sql);
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	public static void setParamters(Record record, PreparedStatement ps, boolean includeId) throws SQLException {
		int counter = 1;
		if (includeId && record.getIdValue() != null) {
			setParamter(ps, counter++, record.getIdField());
		}
		ArrayList<Field> fields = record.getFields();
		for (int i = 0; i < fields.size(); i++) {
			try {
				setParamter(ps, counter++, fields.get(i));
			} catch (Exception e) {
				FieldMeta meta = fields.get(i).getMeta();
				throw new SQLException("Field " + meta.getName() + " Failed to set paramter with value :" + fields.get(i).getValueObject() + "  with type  :" + meta.getType(), e);
			}
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	public static void setParamter(PreparedStatement ps, int index, Field field) throws SQLException {
		// System.out.println(field.getMeta().getName()+" - "+field.getValue());
		// System.out.println(index + "=" +
		// field.getValueObject()+"  type:"+field.getMeta().getType());
		Object value = field.getValueObject();
		if (value == null || value.toString().equals("")) {
			ps.setObject(index, null);
			return;
		}
		switch (field.getMeta().getType()) {
		case Types.LONGVARBINARY:
		case Types.BINARY:
			byte[] data = (byte[]) field.getValueObject();
			ps.setBinaryStream(index, new ByteArrayInputStream(data), data.length);
			break;

		case Types.BOOLEAN:
		case Types.TINYINT:
		case Types.BIT:
			// add support for tiny integers represented as numbers
			ps.setInt(index, ((Boolean) field.getValueAsBoolean()) ? 1 : 0);
			break;
		case Types.DECIMAL:
			ps.setDouble(index, field.getValueAsDouble());
			break;
		case Types.DATE:
			ps.setDate(index, new java.sql.Date(((Date) field.getValueObject()).getTime()));
			break;
		case Types.TIME:
			ps.setTime(index, new Time(((Date) field.getValueObject()).getTime()));
			break;
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
			ps.setString(index, field.getValueObject().toString());
		default:
			// String value=fixStringValue(field.getValue());
			ps.setObject(index, value);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	public static String fixStringValue(String value) {
		return value.replaceAll("'", "\\\\'");
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	public static byte[] getBlobColumn(ResultSet rs, String columnName) throws SQLException {
		try {
			Blob blob = rs.getBlob(columnName);
			if (blob == null) {
				return null;
			}

			InputStream is = blob.getBinaryStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			if (is == null) {
				return null;
			} else {
				byte buffer[] = new byte[64];
				int c = is.read(buffer);
				while (c > 0) {
					bos.write(buffer, 0, c);
					c = is.read(buffer);
				}
				return bos.toByteArray();
			}
		} catch (IOException e) {
			throw new SQLException("Failed to read BLOB column due to IOException: " + e.getMessage());
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	public static String compileSql(String sql, Object param) {
		Object[] params = { param };
		return compileSql(sql, params);
	}

	public static String compileSql(String sql, Object... param) {
		for (int i = 0; i < param.length; i++) {
			sql = sql.replaceFirst("\\?", param[i].toString());
		}
		return sql;
	}

	public static Date getSystemDate() throws RecordNotFoundException, DaoException {
		return DaoFactory.createDao().getSystemDate();
	}

	// ///////////////////////////////////////////////////////////////////////////////////
	public static String exeuteOutputQuery(String query) throws DaoException {
		DefaultDao dao = new DefaultDao();
		return dao.executeOutputQuery(query, " ", "\n");
	}

	// //////////////////////////////////////////////////////////////////////////////////
	public static Object[] executeQueryAsArray(String query) throws DaoException {
		DefaultDao dao = new DefaultDao();
		return dao.exeuteQueryAsArray(query);
		// String out= dao.executeOutputQuery(templateParamtersQuery,"|","\n");
		// String[] records = out.split("\n");
		// Object[][] result=new Object[records.length][];
		// for (int i = 0; i < records.length; i++) {
		// result[i]=records[i].split("\\|");
		// }
		// return result;
	}

	public static java.sql.Date getSystemDateAsSqlDate() throws RecordNotFoundException, DaoException {
		return new java.sql.Date(getSystemDate().getTime());
	}

	public static void clearCache(TableMeta tableMeta) {
		AbstractDao.removeListCache(tableMeta.getListSql());
	}

}
