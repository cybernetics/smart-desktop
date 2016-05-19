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

import com.fs.commons.dao.JKAbstractPlainDataAccess;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.JKRecordNotFoundException;
import com.fs.commons.dao.JKSession;
import com.fs.commons.dao.connection.JKDataSource;
import com.jk.db.dataaccess.plain.JKFinder;

public class OracleAbstractDao extends JKAbstractPlainDataAccess {

	// /////////////////////////////////////////////////////////////////////
	public static byte[] blobToByteArray(final Blob blob) throws IOException, SQLException {
		final InputStream inputStream = blob.getBinaryStream();
		int inByte;
		byte[] returnBytes;
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		while ((inByte = inputStream.read()) != -1) {
			byteArrayOutputStream.write(inByte);
		}
		returnBytes = byteArrayOutputStream.toByteArray();
		return returnBytes;
	}

	public OracleAbstractDao() {
		super();
	}

	public OracleAbstractDao(final JKDataSource connectionManager) {
		super(connectionManager);
	}

	public OracleAbstractDao(final JKSession session) {
		super(session);
	}

	// ///////////////////////////////////////////////////////////////////
	@Override
	public CachedRowSet executeQuery(final String query, final int fromRowIndex, final int toRowIndex) throws JKDataAccessException {
		final String sql = "SELECT *  from ( select a.*, rownum r  from ( " + query + ") a  where rownum <= " + toRowIndex + " ) where r > "
				+ fromRowIndex;

		return executeQuery(sql);
	}

	/**
	 * @param squenceName
	 * @return
	 * @throws JKDataAccessException
	 * @throws SQLException
	 */
	public int getNextSequence(final String squenceName) throws JKDataAccessException, SQLException {
		final Object output = super.exeuteSingleOutputQuery("SELECT " + squenceName + ".NEXTVAL from DUAL");
		if (output instanceof Integer || output instanceof Long) {
			return new Integer(output.toString());
		}
		throw new JKDataAccessException("Unable to get sequence of sequence : " + squenceName + " , value returned " + output);
	}

	@Override
	public Timestamp getSystemDate() throws JKRecordNotFoundException, JKDataAccessException {
		final JKFinder finder = new JKFinder() {

			@Override
			public String getQuery() {
				return "SELECT SYSDATE  FROM DUAL";
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, JKRecordNotFoundException, JKDataAccessException {
				final Timestamp date = rs.getTimestamp(1);
				return date;
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
			}
		};
		return (java.sql.Timestamp) findRecord(finder);
	}

	// /////////////////////////////////////////////////////////////////////
	protected boolean isDuplicateKey(final SQLException e) throws SQLException {
		return isDuplicateKey(e, true);
	}

	// /////////////////////////////////////////////////////////////////////
	protected boolean isDuplicateKey(final SQLException e, final boolean throwException) throws SQLException {
		if (e.getErrorCode() == 1) {
			return true;
		}
		if (throwException) {
			throw e;
		}
		return false;
	}
}
