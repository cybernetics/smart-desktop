package com.fs.commons.dao.oracle;

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
import com.fs.commons.dao.Session;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;

public class OracleAbstractDao extends AbstractDao {

	public OracleAbstractDao() {
		super();
	}

	public OracleAbstractDao(DataSource connectionManager) {
		super(connectionManager);
	}

	public OracleAbstractDao(Session session) {
		super(session);
	}

	/**
	 * @param squenceName
	 * @return
	 * @throws DaoException
	 * @throws SQLException
	 */
	public int getNextSequence(String squenceName) throws DaoException, SQLException {
		Object output = super.exeuteSingleOutputQuery("SELECT " + squenceName + ".NEXTVAL from DUAL");
		if (output instanceof Integer || output instanceof Long) {
			return new Integer(output.toString());
		}
		throw new DaoException("Unable to get sequence of sequence : " + squenceName + " , value returned " + output);
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
				return "SELECT SYSDATE  FROM DUAL";
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
		String sql = "SELECT *  from ( select a.*, rownum r  from ( "+query+") a  where rownum <= " + (toRowIndex) + " ) where r > " + fromRowIndex;

		return executeQuery(sql);
	}
}
