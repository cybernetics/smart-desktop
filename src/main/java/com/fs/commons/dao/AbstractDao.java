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
	private DataSource connectionManager;// =ConnectionManagerFactory.getDefaultConnectionManager();
	private Session session;

	// //////////////////////////////////////////////////////////////////////
	public AbstractDao() {
	}

	// //////////////////////////////////////////////////////////////////////
	public AbstractDao(DataSource connectionManager) {
		this.connectionManager = connectionManager;
	}

	// //////////////////////////////////////////////////////////////////////
	public AbstractDao(Session session) {
		setSession(session);
	}

	// //////////////////////////////////////////////////////////////////////////////
	protected DataSource getDataSource() {
		if (this.connectionManager != null) {
			return connectionManager;
		}
		return DataSourceFactory.getDefaultDataSource();
	}

	// //////////////////////////////////////////////////////////////////////////////
	protected Connection getConnection() throws DaoException {
		return getConnection(false);
	}

	// //////////////////////////////////////////////////////////////////////////////
	protected Connection getConnection(boolean query) throws DaoException {
		if (session != null && !session.isClosed()) {
			return session.getConnection();
		}
		if (query) {
			return getDataSource().getQueryConnection();
		} else {
			return getDataSource().getConnection();
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	protected void close(Connection connection) {
		if (connection == null) {
			return;
		}
		if (session == null) {
			getDataSource().close(connection);
		} else {
			// System.err.println("keeping class connection open");
			// this connection is part of transaction , dont close it , it
			// should be closed by the object
			// who passed the connection to this object
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	public void setSession(Session session) {
		this.session = session;
	}

	// //////////////////////////////////////////////////////////////////////
	public Object findRecord(DaoFinder finder) throws RecordNotFoundException, DaoException {
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
		} catch (SQLException e) {
			throw new DaoException("Error while executing the following select statement : \n" + finder.getFinderSql(), e);
		} finally {
			close(rs);
			close(ps);
			close(connection);
		}
	}

	protected PreparedStatement prepareStatement(Connection connection, String sql) throws SQLException {
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		// if (getDataSource().getDriverName().toLowerCase().contains("mysql"))
		// {
		// prepareStatement.setFetchSize(Integer.MIN_VALUE);
		// }
		return prepareStatement;
	}

	// //////////////////////////////////////////////////////////////////////
	public Object findRecord(DaoFinder finder, String tableName, Object recordId) throws RecordNotFoundException, DaoException {
		if (AbstractDao.cache.get(tableName) == null) {
			AbstractDao.cache.put(tableName, new Hashtable<Object, Object>());
		}
		try {
			Hashtable<Object, Object> tableCache = AbstractDao.cache.get(tableName);
			if (tableCache.get(recordId) == null) {
				// System.out.println("loading record "+tableName);
				Object record = findRecord(finder);
				// if the size exceeded the max cache size , don't cache
				if (tableCache.size() > Integer.parseInt(getDataSource().getProperty("max-cache-size", "1000"))) {
					return record;
				}
				tableCache.put(recordId, record);
			} else {
				// System.out.println("return "+tableName+"  info from cache");
			}
			return tableCache.get(recordId);
		} catch (RecordNotFoundException e) {
			throw new RecordNotFoundException(Lables.get("RECORD_NOT_FOUND_FOR_TABLE") + " (" + tableName + ") for ID (" + recordId + ")");
		}
	}

	public List lstRecords(DaoFinder finder, String key) throws DaoException {
		if (listCache.get(key) == null) {
			listCache.put(key, lstRecords(finder));
		}
		return listCache.get(key);
	}

	// //////////////////////////////////////////////////////////////////////
	public ArrayList lstRecords(DaoFinder finder) throws DaoException {
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
			ArrayList list = new ArrayList();
			while (rs.next()) {
				list.add(finder.populate(rs));
			}
			return list;
		} catch (SQLException e) {
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

	/**
	 * 
	 * @param updater
	 * @return
	 * @throws DaoException
	 */
	public int executeUpdate(DaoUpdater updater, boolean ignoreRecordNotFoundException) throws DaoException {
		try {
			return executeUpdate(updater);
		} catch (RecordNotFoundException e) {
			if (!ignoreRecordNotFoundException) {
				throw e;
			}
			return 0;
		}
	}

	// //////////////////////////////////////////////////////////////////////
	public int executeUpdate(DaoUpdater updater) throws DaoException {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = getConnection();
			ps = connection.prepareStatement(updater.getUpdateSql(), Statement.RETURN_GENERATED_KEYS);

			updater.setParamters(ps);
			int count = ps.executeUpdate();
			debug("Executeing : " + ps.toString());
			debug("Affeted Rows : " + count);
			debug(ps.toString());
			if (count == 0) {
				throw new RecordNotFoundException("RECORD_NOT_FOUND");
			}
			return getGeneratedKeys(ps);
			// no auto increment fields
		} catch (SQLException e) {
			System.err.print("Failed while trying to execute : \n" + updater.getUpdateSql());
			throw new DaoException(ExceptionUtil.getCallerInfo(2) + "\n" + e.getMessage(), e);
		} finally {
			close(ps);
			close(connection);
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////
	protected int getGeneratedKeys(PreparedStatement ps) throws SQLException {
		if (DataSourceUtil.isOracle(getDataSource())) {
			return 0;
		}
		ResultSet idRs = ps.getGeneratedKeys();
		if (idRs.next()) {
			Object object = idRs.getObject(1);
			return ConversionUtil.toInteger(object);
			// Normal case in mysql
			// if (object instanceof Integer) {
			// return (Integer) object;
			// }
		}
		return 0;
	}

	// //////////////////////////////////////////////////////////////////////////////
	protected void close(java.sql.PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	protected void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
			}
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	public void printRecordResultSet(ResultSet rs) throws SQLException {
		printRecordResultSet(rs, true);
	}

	// //////////////////////////////////////////////////////////////////////////////
	public Date getSystemDate() throws RecordNotFoundException, DaoException {
		DaoFinder finder = new DaoFinder() {
			@Override
			public String getFinderSql() {
				return "SELECT SYSDATE()";
			}

			@Override
			public Date populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return rs.getTimestamp(1);
			}

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
			}
		};
		return (Date) findRecord(finder);
	}

	// //////////////////////////////////////////////////////////////////////////////
	public Object exeuteSingleOutputQuery(String query) throws DaoException {
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
		} catch (SQLException ex) {
			throw new DaoException(ex.getMessage() + "\n" + query, ex);
		} finally {
			close(rs, ps, con);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	public CachedRowSet executeQuery(String query) throws DaoException {
		Statement ps = null;
		Connection con = null;
		ResultSet rs = null;
		// System.out.println(query);
		try {
			con = getConnection();
			ps = createStatement(con);
			rs = ps.executeQuery(query);
			CachedRowSet impl = new CachedRowSetImpl();
			impl.populate(rs);
			return impl;
		} catch (SQLException ex) {
			throw new DaoException(ex.getMessage(), ex);
		} finally {
			close(rs);
			close(con);
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////
	protected Statement createStatement(Connection con) throws SQLException {
		Statement createStatement = con.createStatement();
		// if (getDataSource().getDriverName().toLowerCase().contains("mysql"))
		// {
		// createStatement.setFetchSize(Integer.MIN_VALUE);
		// }
		return createStatement;
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
				Vector<IdValueRecord> results = new Vector<IdValueRecord>();
				ResultSetMetaData metaData = rs.getMetaData();
				while (rs.next()) {
					IdValueRecord combo = new IdValueRecord();
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
			} catch (DaoException e) {
				throw e;
			} catch (SQLException e) {
				throw new DaoException(e);
			} finally {
				close(rs, ps, con);
			}
		}
		return listCache.get(sql);
	}

	protected PreparedStatement createStatement(String sql, Connection con) throws SQLException {
		return con.prepareStatement(sql);
	}

	// //////////////////////////////////////////////////////////////////////
	public int executeUpdate(String sql) throws DaoException {
		Connection connection = null;
		Statement ps = null;
		try {
			connection = getConnection();
			ps = createStatement(connection);
			System.err.println("Executeing : " + sql);
			int count = ps.executeUpdate(sql);
			System.err.println("Affeted Rows : " + count);
			// no auto increment fields
			return 0;
		} catch (SQLException e) {
			throw new DaoException(ExceptionUtil.getCallerInfo(2), e);
		} finally {
			close(ps);
			close(connection);
		}
	}

	// //////////////////////////////////////////////////////////////////////
	public void close(Statement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
			}
		}

	}

	// //////////////////////////////////////////////////////////////////////
	public void addAudit(final Audit audit) throws DaoException {
		DaoUpdater updater = new DaoUpdater() {

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException, DaoException {
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

			@Override
			public String getUpdateSql() {
				return "INSERT INTO sec_audits (audit_id,user_record_id,audit_date,audit_type_id,record_id,record_name,old_value,new_value,description,gui) VALUES(?,?,?,?,?,?,?,?,?,?)";
			}
		};
		executeUpdate(updater);
	}

	// //////////////////////////////////////////////////////////////////////
	private void debug(String string) {
		if (System.getProperty("fs.sql.debug", "false").equals("true")) {
			DateTimeUtil.printCurrentTime(string);
		}
	}

	// //////////////////////////////////////////////////////////////////////
	public synchronized static void resetCache() {
		cache.clear();
		listCache.clear();
	}

	// //////////////////////////////////////////////////////////////
	public static void removeListCache(String query) {
		listCache.remove(query);
	}

	// //////////////////////////////////////////////////////////////
	public String executeOutputQuery(String query, String fieldSeparator, String recordsSepartor) throws DaoException {
		try {
			CachedRowSet rs = executeQuery(query);
			ResultSetMetaData meta = rs.getMetaData();
			StringBuffer buf = new StringBuffer();
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
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	public Object[] exeuteQueryAsArray(String query) throws DaoException {
		try {
			CachedRowSet rs = executeQuery(query);
			ResultSetMetaData meta = rs.getMetaData();
			ArrayList<Object[]> rows = new ArrayList<Object[]>();
			while (rs.next()) {
				Object[] row = new Object[meta.getColumnCount()];
				for (int i = 0; i < meta.getColumnCount(); i++) {
					row[i] = (rs.getObject(i + 1));
				}
				rows.add(row);
			}
			return (Object[]) rows.toArray();
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	public void close(ResultSet rs, PreparedStatement ps, Connection c) {
		close(rs);
		close(ps);
		close(c);
	}

	public void close(PreparedStatement ps, Connection c) {
		close(ps);
		close(c);
	}

	// ////////////////////////////////////////////////////
	public int getNextId(String tableName, String fieldName) throws DaoException {
		Connection connection = getConnection(true);
		try {
			return getNextId(connection, tableName, fieldName);
		} finally {
			close(connection);
		}
	}

	// ////////////////////////////////////////////////////
	public int getNextId(Connection connectoin, String tableName, String fieldName) throws DaoException {
		return getNextId(connectoin, tableName, fieldName, null);
	}

	// ////////////////////////////////////////////////////
	public int getNextId(String tableName, String fieldName, String condition) throws DaoException {
		Connection connection = getConnection(true);
		try {
			return getNextId(connection, tableName, fieldName, condition);
		} finally {
			close(connection);
		}
	}

	// ////////////////////////////////////////////////////
	public int getNextId(Connection con, String tableName, String fieldName, String condition) throws DaoException {
		PreparedStatement ps = null;
		int ser;
		try {
			String sql = "SELECT MAX(" + fieldName + ")+1 FROM " + tableName;
			if (condition != null && !condition.trim().equals("")) {
				sql += " WHERE " + condition;
			}
			ps = createStatement(sql, con);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ser = rs.getInt(1);
				if (rs.wasNull()) {
					return 1;
				}
			} else {
				ser = 1;
			}
			return (ser);
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			close(ps);
		}
	}

	// //////////////////////////////////////////////////////////////////////
	public void printRecordResultSet(ResultSet rs, boolean all) throws SQLException {
		java.sql.ResultSetMetaData meta = rs.getMetaData();
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

	// ///////////////////////////////////////////////////////////////////
	public int getRowsCount(String query) throws NumberFormatException, DaoException {
		String sql = "SELECT COUNT(*) FROM (" + query + ") ";
		// System.out.println(sql);
		return new Integer(exeuteSingleOutputQuery(sql).toString());
	}

	// ///////////////////////////////////////////////////////////////////
	public CachedRowSet executeQuery(String query, int fromRowIndex, int toRowIndex) throws DaoException {
		throw new IllegalStateException("this method should be abstract to support other DBMS other than Oracle");
		// String sql =
		// "select *  from ( select a.*, rownum r  from ( "+query+") a  where rownum <= "
		// + (toRowIndex) + " ) where r > " + fromRowIndex;
		// return executeQuery(sql);
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws DaoException
	 * @throws RecordNotFoundException
	 */
	public Audit findAudit(final int id) throws RecordNotFoundException, DaoException {
		DaoFinder finder = new DaoFinder() {

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);
			}

			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				Audit audit = new Audit();
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
			public String getFinderSql() {
				return "select * from sec_audits where audit_id=?";
			}
		};
		return (Audit) findRecord(finder);
	}

	// //////////////////////////////////////////////////////////////////////////////
	public Object exeuteSingleOutputQuery(String query, Object... params) throws DaoException {
		return exeuteSingleOutputQuery(DaoUtil.compileSql(query, params));
	}

	// //////////////////////////////////////////////////////////////////////////////

}
