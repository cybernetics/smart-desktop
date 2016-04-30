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
	private OracleAbstractDao oracleDao = new OracleAbstractDao();

	// /////////////////////////////////////////////////////////////////
	public OracleDynamicDao(String tableMetaName, Session session) {
		super(tableMetaName, session);
	}

	// /////////////////////////////////////////////////////////////////
	public OracleDynamicDao(String tableMetaName) {
		super(tableMetaName);
	}

	// /////////////////////////////////////////////////////////////////
	public OracleDynamicDao(TableMeta meta, Session session) {
		super(meta, session);
	}

	// //////////////////////////////////////////////////////////////
	@Override
	public void setSession(Session session) {
		super.setSession(session);
		oracleDao.setSession(session);
	}

	// //////////////////////////////////////////////////////////////
	public OracleDynamicDao(TableMeta tableMeta) {
		super(tableMeta);
	}

	// //////////////////////////////////////////////////////////////
	public String insertRecord(final Record record) throws DaoException {
		if (record.getIdValue() == null) {
			int nextId = oracleDao.getNextId(tableMeta.getTableName(), record.getIdField().getFieldName());
			record.setIdValue(nextId);
		}
		callBeforeAddEventOnTriggers(record);
		DaoUpdater updater = new DaoUpdater() {
			@Override
			public String getUpdateSql() {
				return sqlBuilder.buildInsert(record);
			}

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				DaoUtil.setParamters(record, ps, true);
			}
		};
		executeUpdate(updater);
		callAfterAddEventOnTriggers(record);
		addInsertAudit(record);
		return record.getIdValue().toString();
	}

	// ////////////////////////////////////////////////////////////////////////////
	@Override
	protected int getGeneratedKeys(PreparedStatement ps) throws SQLException {
		// this method should be fixed to support the last insert id in smarter
		// way , since its not supported by oracle
		return 0;
	}
	
	@Override
	public Date getSystemDate() throws RecordNotFoundException, DaoException {
		return oracleDao.getSystemDate();
	}
}
