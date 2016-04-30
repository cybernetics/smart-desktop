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

	public static void clearCache(final TableMeta tableMeta) {
		AbstractDao.removeListCache(tableMeta.getListSql());
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	public static String compileSql(final String sql, final Object param) {
		final Object[] params = { param };
		return compileSql(sql, params);
	}

	public static String compileSql(String sql, final Object... param) {
		for (final Object element : param) {
			sql = sql.replaceFirst("\\?", element.toString());
		}
		return sql;
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	public static List<IdValueRecord> createRecordsFromSQL(final String sql) throws DaoException {
		final DefaultDao dao = new DefaultDao();
		return dao.createRecordsFromSQL(sql);
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	public static CachedRowSet executeQuery(final String query) throws DaoException {
		final DefaultDao dao = new DefaultDao();
		return dao.executeQuery(query);
	}

	// //////////////////////////////////////////////////////////////////////////////////
	public static Object[] executeQueryAsArray(final String query) throws DaoException {
		final DefaultDao dao = new DefaultDao();
		return dao.exeuteQueryAsArray(query);
		// String out= dao.executeOutputQuery(templateParamtersQuery,"|","\n");
		// String[] records = out.split("\n");
		// Object[][] result=new Object[records.length][];
		// for (int i = 0; i < records.length; i++) {
		// result[i]=records[i].split("\\|");
		// }
		// return result;
	}

	// ///////////////////////////////////////////////////////////////////////////////////
	public static String exeuteOutputQuery(final String query) throws DaoException {
		final DefaultDao dao = new DefaultDao();
		return dao.executeOutputQuery(query, " ", "\n");
	}

	// static ConnectionManager manager =
	// ConnectionManagerFactory.getDefaultConnectionManager();
	// static DefaultDao dao=new DefaultDao();
	// /////////////////////////////////////////////////////////////////////////////////////////////
	public static Object exeuteSingleOutputQuery(final DataSource con, final String query) throws DaoException {
		final DefaultDao dao = new DefaultDao(con);
		return dao.exeuteSingleOutputQuery(query);
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	public static Object exeuteSingleOutputQuery(final String query) throws DaoException {
		final DefaultDao dao = new DefaultDao();
		return dao.exeuteSingleOutputQuery(query);
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	public static String fixStringValue(final String value) {
		return value.replaceAll("'", "\\\\'");
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	public static byte[] getBinaryStream(final ResultSet rs, final String name) throws SQLException {
		final InputStream in = rs.getBinaryStream(name);
		if (in == null) {
			return null;
		}
		byte[] arr = null;
		try {
			arr = GeneralUtility.readStream(in);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return arr;
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	public static byte[] getBlobColumn(final ResultSet rs, final String columnName) throws SQLException {
		try {
			final Blob blob = rs.getBlob(columnName);
			if (blob == null) {
				return null;
			}

			final InputStream is = blob.getBinaryStream();
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();

			if (is == null) {
				return null;
			} else {
				final byte buffer[] = new byte[64];
				int c = is.read(buffer);
				while (c > 0) {
					bos.write(buffer, 0, c);
					c = is.read(buffer);
				}
				return bos.toByteArray();
			}
		} catch (final IOException e) {
			throw new SQLException("Failed to read BLOB column due to IOException: " + e.getMessage());
		}
	}

	public static Date getSystemDate() throws RecordNotFoundException, DaoException {
		return DaoFactory.createDao().getSystemDate();
	}

	public static java.sql.Date getSystemDateAsSqlDate() throws RecordNotFoundException, DaoException {
		return new java.sql.Date(getSystemDate().getTime());
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	public static Record readRecord(final ResultSet rs, final TableMeta tableMeta) throws RecordNotFoundException, DaoException, SQLException {
		final Record record = tableMeta.createEmptyRecord();
		record.setIdValue(rs.getObject(tableMeta.getIdField().getName()));
		// Field idField = record.getIdField();
		// idField.setValue(rs.getString(idField.getMeta().getName()));
		final ArrayList<Field> fields = record.getFields();
		for (int i = 0; i < fields.size(); i++) {
			final Field field = fields.get(i);
			final Object value = readResult(rs, field);
			// Object value = rs.getObject(field.getMeta().getName());
			if (value != null) {
				field.setValue(value);
			}
		}
		record.setNewRecord(false);
		return record;
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	private static Object readResult(final ResultSet rs, final Field field) throws SQLException {
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
	public static void setParamter(final PreparedStatement ps, final int index, final Field field) throws SQLException {
		// System.out.println(field.getMeta().getName()+" - "+field.getValue());
		// System.out.println(index + "=" +
		// field.getValueObject()+" type:"+field.getMeta().getType());
		final Object value = field.getValueObject();
		if (value == null || value.toString().equals("")) {
			ps.setObject(index, null);
			return;
		}
		switch (field.getMeta().getType()) {
		case Types.LONGVARBINARY:
		case Types.BINARY:
			final byte[] data = (byte[]) field.getValueObject();
			ps.setBinaryStream(index, new ByteArrayInputStream(data), data.length);
			break;

		case Types.BOOLEAN:
		case Types.TINYINT:
		case Types.BIT:
			// add support for tiny integers represented as numbers
			ps.setInt(index, field.getValueAsBoolean() ? 1 : 0);
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
	public static void setParamters(final Record record, final PreparedStatement ps, final boolean includeId) throws SQLException {
		int counter = 1;
		if (includeId && record.getIdValue() != null) {
			setParamter(ps, counter++, record.getIdField());
		}
		final ArrayList<Field> fields = record.getFields();
		for (int i = 0; i < fields.size(); i++) {
			try {
				setParamter(ps, counter++, fields.get(i));
			} catch (final Exception e) {
				final FieldMeta meta = fields.get(i).getMeta();
				throw new SQLException("Field " + meta.getName() + " Failed to set paramter with value :" + fields.get(i).getValueObject()
						+ "  with type  :" + meta.getType(), e);
			}
		}
	}

}
