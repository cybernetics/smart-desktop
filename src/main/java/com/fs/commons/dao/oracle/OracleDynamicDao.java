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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.JKRecordNotFoundException;
import com.fs.commons.dao.JKSession;
import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.jk.db.dataaccess.plain.JKUpdater;

public class OracleDynamicDao extends DynamicDao {
	private final OracleAbstractDao oracleDao = new OracleAbstractDao();

	// /////////////////////////////////////////////////////////////////
	public OracleDynamicDao(final String tableMetaName) {
		super(tableMetaName);
	}

	// /////////////////////////////////////////////////////////////////
	public OracleDynamicDao(final String tableMetaName, final JKSession session) {
		super(tableMetaName, session);
	}

	// //////////////////////////////////////////////////////////////
	public OracleDynamicDao(final TableMeta tableMeta) {
		super(tableMeta);
	}

	// /////////////////////////////////////////////////////////////////
	public OracleDynamicDao(final TableMeta meta, final JKSession session) {
		super(meta, session);
	}

	// ////////////////////////////////////////////////////////////////////////////
	@Override
	protected int getGeneratedKeys(final PreparedStatement ps) throws SQLException {
		// this method should be fixed to support the last insert id in smarter
		// way , since its not supported by oracle
		return 0;
	}

	@Override
	public Date getSystemDate() throws JKRecordNotFoundException, JKDataAccessException {
		return this.oracleDao.getSystemDate();
	}

	// //////////////////////////////////////////////////////////////
	@Override
	public String insertRecord(final Record record) throws JKDataAccessException {
		if (record.getIdValue() == null) {
			final int nextId = this.oracleDao.getNextId(this.tableMeta.getTableName(), record.getIdField().getFieldName());
			record.setIdValue(nextId);
		}
		callBeforeAddEventOnTriggers(record);
		final JKUpdater updater = new JKUpdater() {
			@Override
			public String getQuery() {
				return OracleDynamicDao.this.sqlBuilder.buildInsert(record);
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				DaoUtil.setParamters(record, ps, true);
			}
		};
		executeUpdate(updater);
		callAfterAddEventOnTriggers(record);
		addInsertAudit(record);
		return record.getIdValue().toString();
	}

	// //////////////////////////////////////////////////////////////
	@Override
	public void setSession(final JKSession session) {
		super.setSession(session);
		this.oracleDao.setSession(session);
	}
}
