package com.fs.commons.dao.mysql;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.sql.rowset.CachedRowSet;

import com.fs.commons.dao.AbstractDao;
import com.fs.commons.dao.DaoFinder;
import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.Session;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;

public class MysqlAbstractDao extends AbstractDao {

	public MysqlAbstractDao() {
		super();
	}

	public MysqlAbstractDao(DataSource connectionManager) {
		super(connectionManager);
	}

	public MysqlAbstractDao(Session session) {
		super(session);
	}


	@Override
	public Timestamp getSystemDate() throws RecordNotFoundException, DaoException {
		DaoFinder finder = new DaoFinder() {

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
			}

			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				Timestamp date = rs.getTimestamp(1);
				return date;
			}

			@Override
			public String getFinderSql() {
				return "SELECT NOW()  ";
			}
		};
		return (java.sql.Timestamp) findRecord(finder);
	}

	// /////////////////////////////////////////////////////////////////////
	protected boolean isDuplicateKey(SQLException e) throws SQLException {
		return isDuplicateKey(e, true);
	}

	// /////////////////////////////////////////////////////////////////////
	protected boolean isDuplicateKey(SQLException e, boolean throwException) throws SQLException {
		if (e.getErrorCode() == 1) {
			return true;
		}
		if (throwException) {
			throw e;
		}
		return false;
	}

	// /////////////////////////////////////////////////////////////////////
	public static byte[] blobToByteArray(Blob blob) throws IOException, SQLException {
		InputStream inputStream = blob.getBinaryStream();
		int inByte;
		byte[] returnBytes;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		while ((inByte = inputStream.read()) != -1) {
			byteArrayOutputStream.write(inByte);
		}
		returnBytes = byteArrayOutputStream.toByteArray();
		return returnBytes;
	}
	
	// ///////////////////////////////////////////////////////////////////
	public CachedRowSet executeQuery(String query, int fromRowIndex, int toRowIndex) throws DaoException {
		String sql = "select * FROM (?) AS T  limit ?,?";
		String compiledSql = DaoUtil.compileSql(sql, new Object[]{query,fromRowIndex, toRowIndex-fromRowIndex});
		return executeQuery(compiledSql);
	}

	@Override
	// ///////////////////////////////////////////////////////////////////
	public int getRowsCount(String query) throws NumberFormatException, DaoException {
		String sql = "SELECT COUNT(*) FROM (" + query + ") as T";
		// System.out.println(sql);
		return new Integer(exeuteSingleOutputQuery(sql).toString());
	}

}
