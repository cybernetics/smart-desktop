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

import com.fs.commons.dao.DaoUpdater;
import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.Session;
import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;

public class OracleDynamicDao extends DynamicDao {
	private final OracleAbstractDao oracleDao = new OracleAbstractDao();

	// /////////////////////////////////////////////////////////////////
	public OracleDynamicDao(final String tableMetaName) {
		super(tableMetaName);
	}

	// /////////////////////////////////////////////////////////////////
	public OracleDynamicDao(final String tableMetaName, final Session session) {
		super(tableMetaName, session);
	}

	// //////////////////////////////////////////////////////////////
	public OracleDynamicDao(final TableMeta tableMeta) {
		super(tableMeta);
	}

	// /////////////////////////////////////////////////////////////////
	public OracleDynamicDao(final TableMeta meta, final Session session) {
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
	public Date getSystemDate() throws RecordNotFoundException, DaoException {
		return this.oracleDao.getSystemDate();
	}

	// //////////////////////////////////////////////////////////////
	@Override
	public String insertRecord(final Record record) throws DaoException {
		if (record.getIdValue() == null) {
			final int nextId = this.oracleDao.getNextId(this.tableMeta.getTableName(), record.getIdField().getFieldName());
			record.setIdValue(nextId);
		}
		callBeforeAddEventOnTriggers(record);
		final DaoUpdater updater = new DaoUpdater() {
			@Override
			public String getUpdateSql() {
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
	public void setSession(final Session session) {
		super.setSession(session);
		this.oracleDao.setSession(session);
	}
}
