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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.sql.rowset.CachedRowSet;

import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.dao.connection.DataSourceUtil;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.desktop.swin.ConversionUtil;
import com.fs.commons.locale.Lables;
import com.fs.commons.security.Audit;
import com.fs.commons.security.User;
import com.fs.commons.util.DateTimeUtil;
import com.fs.commons.util.ExceptionUtil;
import com.sun.rowset.CachedRowSetImpl;

public class AbstractDao {
	// TODO : add support for max cache size
	private static Hashtable<String, Hashtable<Object, Object>> cache = new Hashtable<String, Hashtable<Object, Object>>();
	private static Hashtable<String, List<? extends Object>> listCache = new Hashtable<>();

	// //////////////////////////////////////////////////////////////
	public static void removeListCache(final String query) {
		listCache.remove(query);
	}

	// //////////////////////////////////////////////////////////////////////
	public synchronized static void resetCache() {
		cache.clear();
		listCache.clear();
	}

	private DataSource connectionManager;// =ConnectionManagerFactory.getDefaultConnectionManager();

	private Session session;

	// //////////////////////////////////////////////////////////////////////
	public AbstractDao() {
	}

	// //////////////////////////////////////////////////////////////////////
	public AbstractDao(final DataSource connectionManager) {
		this.connectionManager = connectionManager;
	}

	// //////////////////////////////////////////////////////////////////////
	public AbstractDao(final Session session) {
		setSession(session);
	}

	// //////////////////////////////////////////////////////////////////////
	public void addAudit(final Audit audit) throws DaoException {
		final DaoUpdater updater = new DaoUpdater() {

			@Override
			public String getUpdateSql() {
				return "INSERT INTO sec_audits (audit_id,user_record_id,audit_date,audit_type_id,record_id,record_name,old_value,new_value,description,gui) VALUES(?,?,?,?,?,?,?,?,?,?)";
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException, DaoException {
				int counter = 1;
				ps.setInt(counter++, getNextId("sec_audits", "audit_id"));
				ps.setInt(counter++, audit.getUser().getUserRecordId());
				ps.setTimestamp(counter++, new Timestamp(getSystemDate().getTime()));
				ps.setInt(counter++, audit.getAuditType().getAuditType());
				ps.setObject(counter++, audit.getBusinessRecordId());
				ps.setString(counter++, audit.getTableName());
				ps.setString(counter++, audit.getOldValue());
				ps.setString(counter++, audit.getNewValue());
				ps.setString(counter++, audit.getDescription());
				ps.setString(counter++, audit.getGui());
			}
		};
		executeUpdate(updater);
	}

	// //////////////////////////////////////////////////////////////////////////////
	protected void close(final Connection connection) {
		if (connection == null) {
			return;
		}
		if (this.session == null) {
			getDataSource().close(connection);
		} else {
			// System.err.println("keeping class connection open");
			// this connection is part of transaction , dont close it , it
			// should be closed by the object
			// who passed the connection to this object
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	protected void close(final java.sql.PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (final Exception e) {
			}
		}
	}

	public void close(final PreparedStatement ps, final Connection c) {
		close(ps);
		close(c);
	}

	// //////////////////////////////////////////////////////////////////////////////
	protected void close(final ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (final Exception e) {
			}
		}
	}

	public void close(final ResultSet rs, final PreparedStatement ps, final Connection c) {
		close(rs);
		close(ps);
		close(c);
	}

	// //////////////////////////////////////////////////////////////////////
	public void close(final Statement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (final SQLException e) {
			}
		}

	}

	// //////////////////////////////////////////////////////////////////////////////////////
	public List createRecordsFromSQL(String sql) throws DaoException {
		if (listCache.get(sql) == null) {
			Connection con = null;
			ResultSet rs = null;
			PreparedStatement ps = null;
			sql = sql.trim();
			// System.out.println("\n\n\n\nLoading list: " + sql);
			// GeneralUtility.printStackTrace();
			try {
				con = getConnection(true);
				ps = createStatement(sql, con);
				rs = ps.executeQuery();
				final Vector<IdValueRecord> results = new Vector<IdValueRecord>();
				final ResultSetMetaData metaData = rs.getMetaData();
				while (rs.next()) {
					final IdValueRecord combo = new IdValueRecord();
					combo.setId(rs.getString(1));

					if (metaData.getColumnCount() >= 2) {
						if (rs.getString(2) == null) {
							debug(sql + "\n generating null values");
						} else {
							combo.setValue(rs.getString(2));
						}
					} else {
						debug(sql + "\n generating single column only");
					}
					results.add(combo);
				}
				listCache.put(sql, results);
			} catch (final DaoException e) {
				throw e;
			} catch (final SQLException e) {
				throw new DaoException(e);
			} finally {
				close(rs, ps, con);
			}
		}
		return listCache.get(sql);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////
	protected Statement createStatement(final Connection con) throws SQLException {
		final Statement createStatement = con.createStatement();
		// if (getDataSource().getDriverName().toLowerCase().contains("mysql"))
		// {
		// createStatement.setFetchSize(Integer.MIN_VALUE);
		// }
		return createStatement;
	}

	protected PreparedStatement createStatement(final String sql, final Connection con) throws SQLException {
		return con.prepareStatement(sql);
	}

	// //////////////////////////////////////////////////////////////////////
	private void debug(final String string) {
		if (System.getProperty("fs.sql.debug", "false").equals("true")) {
			DateTimeUtil.printCurrentTime(string);
		}
	}

	// //////////////////////////////////////////////////////////////
	public String executeOutputQuery(final String query, final String fieldSeparator, final String recordsSepartor) throws DaoException {
		try {
			final CachedRowSet rs = executeQuery(query);
			final ResultSetMetaData meta = rs.getMetaData();
			final StringBuffer buf = new StringBuffer();
			while (rs.next()) {
				if (buf.length() > 0) {
					buf.append(recordsSepartor);
				}
				for (int i = 0; i < meta.getColumnCount(); i++) {
					buf.append(rs.getObject(i + 1));
					buf.append(fieldSeparator);
				}
			}
			return buf.toString();
		} catch (final SQLException e) {
			throw new DaoException(e);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	public CachedRowSet executeQuery(final String query) throws DaoException {
		Statement ps = null;
		Connection con = null;
		ResultSet rs = null;
		// System.out.println(query);
		try {
			con = getConnection();
			ps = createStatement(con);
			rs = ps.executeQuery(query);
			final CachedRowSet impl = new CachedRowSetImpl();
			impl.populate(rs);
			return impl;
		} catch (final SQLException ex) {
			throw new DaoException(ex.getMessage(), ex);
		} finally {
			close(rs);
			close(con);
		}
	}

	// ///////////////////////////////////////////////////////////////////
	public CachedRowSet executeQuery(final String query, final int fromRowIndex, final int toRowIndex) throws DaoException {
		throw new IllegalStateException("this method should be abstract to support other DBMS other than Oracle");
		// String sql =
		// "select * from ( select a.*, rownum r from ( "+query+") a where
		// rownum <= "
		// + (toRowIndex) + " ) where r > " + fromRowIndex;
		// return executeQuery(sql);
	}

	// //////////////////////////////////////////////////////////////////////
	public int executeUpdate(final DaoUpdater updater) throws DaoException {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = getConnection();
			ps = connection.prepareStatement(updater.getUpdateSql(), Statement.RETURN_GENERATED_KEYS);

			updater.setParamters(ps);
			final int count = ps.executeUpdate();
			debug("Executeing : " + ps.toString());
			debug("Affeted Rows : " + count);
			debug(ps.toString());
			if (count == 0) {
				throw new RecordNotFoundException("RECORD_NOT_FOUND");
			}
			return getGeneratedKeys(ps);
			// no auto increment fields
		} catch (final SQLException e) {
			System.err.print("Failed while trying to execute : \n" + updater.getUpdateSql());
			throw new DaoException(ExceptionUtil.getCallerInfo(2) + "\n" + e.getMessage(), e);
		} finally {
			close(ps);
			close(connection);
		}
	}

	/**
	 *
	 * @param updater
	 * @return
	 * @throws DaoException
	 */
	public int executeUpdate(final DaoUpdater updater, final boolean ignoreRecordNotFoundException) throws DaoException {
		try {
			return executeUpdate(updater);
		} catch (final RecordNotFoundException e) {
			if (!ignoreRecordNotFoundException) {
				throw e;
			}
			return 0;
		}
	}

	// //////////////////////////////////////////////////////////////////////
	public int executeUpdate(final String sql) throws DaoException {
		Connection connection = null;
		Statement ps = null;
		try {
			connection = getConnection();
			ps = createStatement(connection);
			System.err.println("Executeing : " + sql);
			final int count = ps.executeUpdate(sql);
			System.err.println("Affeted Rows : " + count);
			// no auto increment fields
			return 0;
		} catch (final SQLException e) {
			throw new DaoException(ExceptionUtil.getCallerInfo(2), e);
		} finally {
			close(ps);
			close(connection);
		}
	}

	public Object[] exeuteQueryAsArray(final String query) throws DaoException {
		try {
			final CachedRowSet rs = executeQuery(query);
			final ResultSetMetaData meta = rs.getMetaData();
			final ArrayList<Object[]> rows = new ArrayList<Object[]>();
			while (rs.next()) {
				final Object[] row = new Object[meta.getColumnCount()];
				for (int i = 0; i < meta.getColumnCount(); i++) {
					row[i] = rs.getObject(i + 1);
				}
				rows.add(row);
			}
			return rows.toArray();
		} catch (final SQLException e) {
			throw new DaoException(e);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	public Object exeuteSingleOutputQuery(final String query) throws DaoException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getConnection(true);
			ps = createStatement(query, con);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getObject(1);
			}
			throw new RecordNotFoundException("No value available for query :" + query);
		} catch (final SQLException ex) {
			throw new DaoException(ex.getMessage() + "\n" + query, ex);
		} finally {
			close(rs, ps, con);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	public Object exeuteSingleOutputQuery(final String query, final Object... params) throws DaoException {
		return exeuteSingleOutputQuery(DaoUtil.compileSql(query, params));
	}

	/**
	 *
	 * @param id
	 * @return
	 * @throws DaoException
	 * @throws RecordNotFoundException
	 */
	public Audit findAudit(final int id) throws RecordNotFoundException, DaoException {
		final DaoFinder finder = new DaoFinder() {

			@Override
			public String getFinderSql() {
				return "select * from sec_audits where audit_id=?";
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				final Audit audit = new Audit();
				audit.setAuditId(rs.getInt("audit_id"));
				audit.setUser(new User(rs.getInt("user_record_id")));
				audit.setDate(new Date(rs.getTime("audit_date").getTime()));
				audit.setTableName(rs.getString("record_name"));
				audit.setBusinessRecordId(rs.getObject("record_id"));
				audit.setOldValue(rs.getString("old_value"));
				audit.setNewValue(rs.getString("new_value"));
				audit.setDescription(rs.getString("description"));
				audit.setGui(rs.getString("gui"));
				return audit;
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);
			}
		};
		return (Audit) findRecord(finder);
	}

	// //////////////////////////////////////////////////////////////////////
	public Object findRecord(final DaoFinder finder) throws RecordNotFoundException, DaoException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = getConnection(true);
			ps = prepareStatement(connection, finder.getFinderSql());
			finder.setParamters(ps);
			rs = ps.executeQuery();
			if (rs.next()) {
				return finder.populate(rs);
			}
			throw new RecordNotFoundException(Lables.get("REOCRD_NOT_FOUND") + "\n" + ExceptionUtil.getCallerInfo(2));
		} catch (final SQLException e) {
			throw new DaoException("Error while executing the following select statement : \n" + finder.getFinderSql(), e);
		} finally {
			close(rs);
			close(ps);
			close(connection);
		}
	}

	// //////////////////////////////////////////////////////////////////////
	public Object findRecord(final DaoFinder finder, final String tableName, final Object recordId) throws RecordNotFoundException, DaoException {
		if (AbstractDao.cache.get(tableName) == null) {
			AbstractDao.cache.put(tableName, new Hashtable<Object, Object>());
		}
		try {
			final Hashtable<Object, Object> tableCache = AbstractDao.cache.get(tableName);
			if (tableCache.get(recordId) == null) {
				// System.out.println("loading record "+tableName);
				final Object record = findRecord(finder);
				// if the size exceeded the max cache size , don't cache
				if (tableCache.size() > Integer.parseInt(getDataSource().getProperty("max-cache-size", "1000"))) {
					return record;
				}
				tableCache.put(recordId, record);
			} else {
				// System.out.println("return "+tableName+" info from cache");
			}
			return tableCache.get(recordId);
		} catch (final RecordNotFoundException e) {
			throw new RecordNotFoundException(Lables.get("RECORD_NOT_FOUND_FOR_TABLE") + " (" + tableName + ") for ID (" + recordId + ")");
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	protected Connection getConnection() throws DaoException {
		return getConnection(false);
	}

	// //////////////////////////////////////////////////////////////////////////////
	protected Connection getConnection(final boolean query) throws DaoException {
		if (this.session != null && !this.session.isClosed()) {
			return this.session.getConnection();
		}
		if (query) {
			return getDataSource().getQueryConnection();
		} else {
			return getDataSource().getConnection();
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	protected DataSource getDataSource() {
		if (this.connectionManager != null) {
			return this.connectionManager;
		}
		return DataSourceFactory.getDefaultDataSource();
	}

	// ///////////////////////////////////////////////////////////////////////////////
	protected int getGeneratedKeys(final PreparedStatement ps) throws SQLException {
		if (DataSourceUtil.isOracle(getDataSource())) {
			return 0;
		}
		final ResultSet idRs = ps.getGeneratedKeys();
		if (idRs.next()) {
			final Object object = idRs.getObject(1);
			return ConversionUtil.toInteger(object);
			// Normal case in mysql
			// if (object instanceof Integer) {
			// return (Integer) object;
			// }
		}
		return 0;
	}

	// ////////////////////////////////////////////////////
	public int getNextId(final Connection connectoin, final String tableName, final String fieldName) throws DaoException {
		return getNextId(connectoin, tableName, fieldName, null);
	}

	// ////////////////////////////////////////////////////
	public int getNextId(final Connection con, final String tableName, final String fieldName, final String condition) throws DaoException {
		PreparedStatement ps = null;
		int ser;
		try {
			String sql = "SELECT MAX(" + fieldName + ")+1 FROM " + tableName;
			if (condition != null && !condition.trim().equals("")) {
				sql += " WHERE " + condition;
			}
			ps = createStatement(sql, con);

			final ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ser = rs.getInt(1);
				if (rs.wasNull()) {
					return 1;
				}
			} else {
				ser = 1;
			}
			return ser;
		} catch (final SQLException e) {
			throw new DaoException(e);
		} finally {
			close(ps);
		}
	}

	// ////////////////////////////////////////////////////
	public int getNextId(final String tableName, final String fieldName) throws DaoException {
		final Connection connection = getConnection(true);
		try {
			return getNextId(connection, tableName, fieldName);
		} finally {
			close(connection);
		}
	}

	// ////////////////////////////////////////////////////
	public int getNextId(final String tableName, final String fieldName, final String condition) throws DaoException {
		final Connection connection = getConnection(true);
		try {
			return getNextId(connection, tableName, fieldName, condition);
		} finally {
			close(connection);
		}
	}

	// ///////////////////////////////////////////////////////////////////
	public int getRowsCount(final String query) throws NumberFormatException, DaoException {
		final String sql = "SELECT COUNT(*) FROM (" + query + ") ";
		// System.out.println(sql);
		return new Integer(exeuteSingleOutputQuery(sql).toString());
	}

	// //////////////////////////////////////////////////////////////////////////////
	public Date getSystemDate() throws RecordNotFoundException, DaoException {
		final DaoFinder finder = new DaoFinder() {
			@Override
			public String getFinderSql() {
				return "SELECT SYSDATE()";
			}

			@Override
			public Date populate(final ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return rs.getTimestamp(1);
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
			}
		};
		return (Date) findRecord(finder);
	}

	// //////////////////////////////////////////////////////////////////////
	public ArrayList lstRecords(final DaoFinder finder) throws DaoException {
		// System.out.println("Executing : "+finder.getFinderSql());
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = getConnection(true);
			ps = prepareStatement(connection, finder.getFinderSql());
			finder.setParamters(ps);
			rs = ps.executeQuery();
			debug(ps.toString().substring(ps.toString().toUpperCase().indexOf("SELECT")));
			final ArrayList list = new ArrayList();
			while (rs.next()) {
				list.add(finder.populate(rs));
			}
			return list;
		} catch (final SQLException e) {
			if (ps != null) {
				debug(ps.toString());
			}
			throw new DaoException(e);
		} finally {
			close(rs);
			close(ps);
			close(connection);
		}
	}

	public List lstRecords(final DaoFinder finder, final String key) throws DaoException {
		if (listCache.get(key) == null) {
			listCache.put(key, lstRecords(finder));
		}
		return listCache.get(key);
	}

	protected PreparedStatement prepareStatement(final Connection connection, final String sql) throws SQLException {
		final PreparedStatement prepareStatement = connection.prepareStatement(sql);
		// if (getDataSource().getDriverName().toLowerCase().contains("mysql"))
		// {
		// prepareStatement.setFetchSize(Integer.MIN_VALUE);
		// }
		return prepareStatement;
	}

	// //////////////////////////////////////////////////////////////////////////////
	public void printRecordResultSet(final ResultSet rs) throws SQLException {
		printRecordResultSet(rs, true);
	}

	// //////////////////////////////////////////////////////////////////////
	public void printRecordResultSet(final ResultSet rs, final boolean all) throws SQLException {
		final java.sql.ResultSetMetaData meta = rs.getMetaData();
		System.out.println("At print result set");
		while (rs.next()) {
			System.out.println("------------------------------------------------------");
			for (int i = 0; i < meta.getColumnCount(); i++) {
				System.out.print(meta.getColumnName(i + 1) + " = " + rs.getObject(i + 1) + "\t");
			}
			System.out.println();
			if (!all) {
				return;
			}
		}
		System.out.println("///////////////////////");

	}

	// //////////////////////////////////////////////////////////////////////////////
	public void setSession(final Session session) {
		this.session = session;
	}

	// //////////////////////////////////////////////////////////////////////////////

}
