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
package com.fs.commons.dao.dynamic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.JKAbstractPlainDataAccess;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.JKRecordNotFoundException;
import com.fs.commons.dao.JKSession;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.dynamic.trigger.Trigger;
import com.jk.db.dataaccess.plain.JKFinder;
import com.jk.db.dataaccess.plain.JKUpdater;
import com.jk.security.JKAudit;
import com.jk.security.JKAuditType;

public class DynamicDao extends JKAbstractPlainDataAccess {
	Logger logger = Logger.getLogger(getClass().getName());
	protected final TableMeta tableMeta;
	protected MetaSqlBuilder sqlBuilder;

	// //////////////////////////////////////////////////////////////
	public DynamicDao(final String tableMetaName) {
		this(AbstractTableMetaFactory.getTableMeta(tableMetaName));
		logger.info(tableMetaName);
	}

	// //////////////////////////////////////////////////////////////
	public DynamicDao(final String tableMetaName, final JKSession session) {
		this(AbstractTableMetaFactory.getTableMeta(session.getConnectionManager(), tableMetaName));
		setSession(session);
	}

	// //////////////////////////////////////////////////////////////
	public DynamicDao(final TableMeta tableMeta) {
		this(tableMeta, tableMeta.getDataSource());
	}

	// //////////////////////////////////////////////////////////////
	protected DynamicDao(final TableMeta tableMeta, final JKDataSource connectionManager) {
		super(connectionManager);
		this.tableMeta = tableMeta;
		this.sqlBuilder = new MetaSqlBuilder(tableMeta);
	}

	// //////////////////////////////////////////////////////////////
	public DynamicDao(final TableMeta meta, final JKSession session) {
		this(meta);
		setSession(session);
	}

	// //////////////////////////////////////////////////////////////
	protected void addDeleteAudit(final Record record) throws JKDataAccessException {
		logger.info(record.toString());
		final JKAudit audit = createAudit(record, JKAuditType.AUDIT_DELETE_RECORD);
		addAudit(audit);
	}

	// //////////////////////////////////////////////////////////////
	protected void addInsertAudit(final Record record) throws JKDataAccessException {
		logger.info(record.toString());
		final JKAuditType aUDIT_ADD_RECORD = JKAuditType.AUDIT_ADD_RECORD;
		final JKAudit audit = createAudit(record, aUDIT_ADD_RECORD);
		addAudit(audit);
	}

	// //////////////////////////////////////////////////////////////
	protected void addUpdateAudit(final Record oldRecord, final Record newRecord) throws JKDataAccessException {
		logger.info(oldRecord == null ? newRecord.toString() : oldRecord.toString());
		final JKAudit audit = createAudit(newRecord, JKAuditType.AUDIT_UPDATE_RECORD);
		audit.setOldValue(oldRecord.toString(true));
		addAudit(audit);
	}

	// //////////////////////////////////////////////////////////////
	protected void callAfterAddEventOnTriggers(final Record record) throws JKDataAccessException {
		final ArrayList<Trigger> triggers = this.tableMeta.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).afterAdd(record);
		}
	}

	// //////////////////////////////////////////////////////////////
	protected void callAfterDeleteEventOnTriggers(final Record record) throws JKDataAccessException {
		final ArrayList<Trigger> triggers = this.tableMeta.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).afterDelete(record);
		}
	}

	protected void callAfterFindEventOnTriggers(final Record record) throws JKDataAccessException {
		final ArrayList<Trigger> triggers = this.tableMeta.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).afterFind(record);
		}
	}

	// //////////////////////////////////////////////////////////////
	protected void callAfterUpdateEventOnTriggers(final Record oldRecord, final Record newRecord) throws JKDataAccessException {
		final ArrayList<Trigger> triggers = this.tableMeta.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).afterUpdate(oldRecord, newRecord);
		}
	}

	// //////////////////////////////////////////////////////////////
	protected void callBeforeAddEventOnTriggers(final Record record) throws JKDataAccessException {
		final ArrayList<Trigger> triggers = this.tableMeta.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).beforeAdd(record);
		}
	}

	// //////////////////////////////////////////////////////////////
	protected void callBeforeDeleteEventOnTriggers(final Record record) throws JKDataAccessException {
		final ArrayList<Trigger> triggers = this.tableMeta.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).beforeDelete(record);
		}
	}

	// //////////////////////////////////////////////////////////////
	protected void callBeforeUpdateEventOnTriggers(final Record oldRecord, final Record newRecord) throws JKDataAccessException {
		final ArrayList<Trigger> triggers = this.tableMeta.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).beforeUpdate(oldRecord, newRecord);
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public void cloneDetails(final Object sourceIdValue, final Object targetIdValue) throws JKDataAccessException {
		// TODO : refactor this method to be extarced to facade and avoid direct
		// calls from UI
		final ArrayList<ForiegnKeyFieldMeta> detailFields = this.tableMeta.getDetailFields();
		final JKSession session = getDataSource().createSession();
		boolean commit = false;
		try {
			for (final ForiegnKeyFieldMeta foriegnKeyFieldMeta : detailFields) {
				final DynamicDao detailDao = DaoFactory.createDynamicDao(foriegnKeyFieldMeta.getParentTable());
				detailDao.setSession(session);
				// Find detaile record for the original id
				final List<Record> detailRecords = detailDao.findByFieldValue(foriegnKeyFieldMeta.getName(), sourceIdValue);
				// iterate over the records , change the id to the new id , and
				// reset the primary key
				for (final Record detailRecord : detailRecords) {
					detailRecord.setIdValue(null);
					detailRecord.setFieldValue(foriegnKeyFieldMeta.getName(), targetIdValue);
					detailDao.insertRecord(detailRecord);
				}
			}
			commit = true;
		} finally {
			session.close(commit);
			setSession(null);
		}
	}

	// //////////////////////////////////////////////////////////////
	public JKAudit createAudit(final Record record, final JKAuditType auditType) {
		final JKAudit audit = new JKAudit();
		audit.setAuditType(auditType);
		audit.setBusinessRecordId(record.getIdValueAsInteger());
		audit.setTableName(record.getTableMeta().getTableName());
		audit.setNewValue(record.toString(true));
		if (new Boolean(System.getProperty("fs.security.audit.gui", "true"))) {
			audit.setGui(record.getGui());
		}
		return audit;
	}

	// //////////////////////////////////////////////////////////////
	public Record createEmptyRecord(final boolean setDefaultValues) {
		return this.tableMeta.createEmptyRecord(setDefaultValues);
	}

	// //////////////////////////////////////////////////////////////
	public Record createEmptyRecord(final boolean setDefaultValues, final Record defaults) {
		return this.tableMeta.createEmptyRecord(setDefaultValues, defaults);
	}

	// //////////////////////////////////////////////////////////////
	public void deleteAllRecords() throws JKDataAccessException {
		logger.info("deleteAllRecords for tablemeta : " + tableMeta);
		final JKUpdater updater = new JKUpdater() {

			@Override
			public String getQuery() {
				return DynamicDao.this.sqlBuilder.buildDelete();
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
			}
		};
		executeUpdate(updater);
	}

	public void deleteByFieldsValues(final HashMap<String, String> fieldNameToValue) throws JKDataAccessException {
		logger.info(fieldNameToValue.toString());
		try {
			final Record filterRecord = this.tableMeta.createEmptyRecord();
			final Set<String> keySet = fieldNameToValue.keySet();
			for (final String fieldName : keySet) {
				filterRecord.setFieldValue(fieldName, fieldNameToValue.get(fieldName));
			}
			final ArrayList<Record> lstRecords = lstRecords(filterRecord);
			for (final Record record : lstRecords) {
				deleteRecord(record);
			}

		} catch (final JKRecordNotFoundException e) {
		}
	}

	// //////////////////////////////////////////////////////////////
	public void deleteByFieldValue(final String fieldName, final Object value) throws JKRecordNotFoundException, JKDataAccessException {
		logger.info(fieldName + " = " + value);
		final Record record = createEmptyRecord(false);
		record.setFieldValue(fieldName, value);
		deleteRecord(record.getField(fieldName), false);
	}

	// //////////////////////////////////////////////////////////////
	public void deleteRecord(final Field field) throws JKRecordNotFoundException, JKDataAccessException {
		logger.info(field.toString());
		deleteRecord(field, true);
	}

	// //////////////////////////////////////////////////////////////
	public void deleteRecord(final Field field, final boolean addAudit) throws JKRecordNotFoundException, JKDataAccessException {
		logger.info(field + " , add audit : " + addAudit);
		final JKUpdater updater = new JKUpdater() {

			@Override
			public String getQuery() {
				return DynamicDao.this.sqlBuilder.buildDelete(field);
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
			}
		};
		Record record = null;
		if (addAudit) {
			record = findRecord(field.getValueAsInteger());
		}
		executeUpdate(updater);
		if (addAudit) {
			addDeleteAudit(record);
		}
	}

	// //////////////////////////////////////////////////////////////
	public void deleteRecord(final Object recordId) throws JKRecordNotFoundException, JKDataAccessException {
		final Record record = createEmptyRecord(false);
		record.setIdValue(recordId);
		deleteRecord(record);
	}

	// //////////////////////////////////////////////////////////////
	public void deleteRecord(final Record record) throws JKRecordNotFoundException, JKDataAccessException {
		callBeforeDeleteEventOnTriggers(record);
		deleteRecord(record.getIdField());
		callAfterDeleteEventOnTriggers(record);
	}

	/*
	 * @param filter
	 *
	 * @throws DaoException
	 */
	public void deleteRecords(final Record filter) throws JKDataAccessException {
		final ArrayList<Record> findRecord = lstRecords(filter);
		for (final Record record : findRecord) {
			deleteRecord(record);
		}
	}

	// //////////////////////////////////////////////////////////////
	public void executeUpdateQuery(final String sql, final Object[] objects) throws JKDataAccessException {
		final JKUpdater updater = new JKUpdater() {

			@Override
			public String getQuery() {
				return sql;
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				for (int i = 0; i < objects.length; i++) {
					ps.setObject(i + 1, objects[i]);
				}
			}
		};
		executeUpdate(updater);
	}

	// //////////////////////////////////////////////////////////////
	public ArrayList<Record> findByFieldValue(final String fieldName, final Object value) throws JKDataAccessException {
		final Record filterRecord = this.tableMeta.createEmptyRecord();
		filterRecord.setFieldValue(fieldName, value);
		return lstRecords(filterRecord);
	}

	// //////////////////////////////////////////////////////////////
	public Record findRecord(final Object id) throws JKRecordNotFoundException, JKDataAccessException {
		// System.err.println("tring to find ID (" + id + ")on table " +
		// tableMeta.getTableName());
		final JKFinder finder = new JKFinder() {

			@Override
			public String getQuery() {
				return DynamicDao.this.sqlBuilder.buildFindById(id.toString());
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, JKRecordNotFoundException, JKDataAccessException {
				return DaoUtil.readRecord(rs, DynamicDao.this.tableMeta);
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
			}
		};
		final Record record = (Record) findRecord(finder);
		callAfterFindEventOnTriggers(record);
		return record;
	}

	// //////////////////////////////////////////////////////////////
	public Record findRecord(final Record filter) throws JKRecordNotFoundException, JKDataAccessException {
		final JKFinder finder = new JKFinder() {
			@Override
			public String getQuery() {
				return DynamicDao.this.sqlBuilder.buildFindByFilter(filter);
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, JKRecordNotFoundException, JKDataAccessException {
				return DaoUtil.readRecord(rs, DynamicDao.this.tableMeta);
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
			}
		};
		return (Record) findRecord(finder);
	}

	public Record findRecordByFieldValue(final HashMap<String, String> fieldNameToValue) throws JKRecordNotFoundException, JKDataAccessException {
		final Record filterRecord = this.tableMeta.createEmptyRecord();
		for (final String fieldName : fieldNameToValue.keySet()) {
			filterRecord.setFieldValue(fieldName, fieldNameToValue.get(fieldName));
		}
		return findRecord(filterRecord);
	}

	// //////////////////////////////////////////////////////////////
	public Record findRecordByFieldValue(final String fieldName, final Object value) throws JKRecordNotFoundException, JKDataAccessException {
		final Record filterRecord = this.tableMeta.createEmptyRecord();
		filterRecord.setFieldValue(fieldName, value);
		return findRecord(filterRecord);
	}

	/**
	 *
	 * @param recordId
	 * @return
	 * @throws TableMetaNotFoundException
	 */
	public String getAssignedValuesQuery(final int field1Id) throws TableMetaNotFoundException {
		final StringBuffer buf = new StringBuffer();
		final ArrayList<ForiegnKeyFieldMeta> list = this.tableMeta.lstForiegnKeyFields();
		final ForiegnKeyFieldMeta field1 = list.get(0);
		final ForiegnKeyFieldMeta field2 = list.get(1);
		final TableMeta refTable = AbstractTableMetaFactory.getTableMeta(field2.getParentTable().getDataSource(), field2.getReferenceTable());
		// buf.append(refTable.getShortReportSql());
		String sql = refTable.getShortReportSql();
		sql += sql.toUpperCase().contains("WHERE") ? " AND " : " WHERE ";
		buf.append(sql);
		buf.append(field2.getReferenceField() + " IN (");
		buf.append(" SELECT " + field2.getName() + " FROM " + field2.getParentTable().getTableName());
		buf.append(" WHERE " + field1.getName() + " = " + field1Id);
		buf.append(")");
		return buf.toString();
	}

	// //////////////////////////////////////////////////////////////
	public Record getFirstRecordInTable() throws JKRecordNotFoundException, JKDataAccessException {
		logger.info("First record");
		final ArrayList<Record> r = lstRecords();
		if (r.size() > 0) {
			return r.get(0);
		}
		return null;
	}

	/**
	 *
	 * @param recordId
	 * @return
	 * @throws TableMetaNotFoundException
	 */
	public String getNotAssignedValuesQuery(final int field1Id) throws TableMetaNotFoundException {
		final StringBuffer buf = new StringBuffer();
		final ArrayList<ForiegnKeyFieldMeta> list = this.tableMeta.lstForiegnKeyFields();
		final ForiegnKeyFieldMeta field1 = list.get(0);
		final ForiegnKeyFieldMeta field2 = list.get(1);
		final TableMeta refTable = AbstractTableMetaFactory.getTableMeta(field2.getParentTable().getDataSource(), field2.getReferenceTable());
		String sql = refTable.getShortReportSql();
		sql += sql.toUpperCase().contains("WHERE") ? " AND " : " WHERE ";
		buf.append(sql);
		buf.append(field2.getReferenceField() + " NOT IN (");
		buf.append(" SELECT " + field2.getName() + " FROM " + field2.getParentTable().getTableName());
		buf.append(" WHERE " + field1.getName() + " = " + field1Id);
		buf.append(")");
		return buf.toString();
	}

	// //////////////////////////////////////////////////////////////
	public MetaSqlBuilder getSqlBuilder() {
		return this.sqlBuilder;
	}

	public TableMeta getTableMeta() {
		return this.tableMeta;
	}

	// //////////////////////////////////////////////////////////////
	public String insertRecord(final Record record) throws JKDataAccessException {
		callBeforeAddEventOnTriggers(record);
		final JKUpdater updater = new JKUpdater() {
			@Override
			public String getQuery() {
				return DynamicDao.this.sqlBuilder.buildInsert(record);
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				DaoUtil.setParamters(record, ps, true);
			}
		};
		final String id = executeUpdate(updater) + "";
		record.setIdValue(id);
		callAfterAddEventOnTriggers(record);
		addInsertAudit(record);
		return id;
	}

	// //////////////////////////////////////////////////////////////
	public void insertRecords(final ArrayList<Record> records) throws JKDataAccessException {
		if (records.size() > 0) {
			final String insert = this.sqlBuilder.buildFatInsert(records);
			executeUpdate(insert);
		}
	}

	// //////////////////////////////////////////////////////////////
	public boolean isIdExists(final Object id) throws JKDataAccessException {
		try {
			findRecord(id.toString());
			return true;
		} catch (final JKRecordNotFoundException e) {
			// just each the exception
			return false;
		} catch (final JKDataAccessException e) {
			throw e;
		}
	}

	// //////////////////////////////////////////////////////////////
	public ArrayList<Record> lstRecords() throws JKRecordNotFoundException, JKDataAccessException {
		logger.info("lstRecords : " + tableMeta.getTableName());
		return lstRecords(createEmptyRecord(false));
	}

	// //////////////////////////////////////////////////////////////
	public ArrayList<Record> lstRecords(final Record filter) throws JKDataAccessException {
		logger.info(filter.toString());
		final JKFinder finder = new JKFinder() {
			@Override
			public String getQuery() {
				return DynamicDao.this.sqlBuilder.buildFindByFilter(filter);
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, JKRecordNotFoundException, JKDataAccessException {
				return DaoUtil.readRecord(rs, DynamicDao.this.tableMeta);
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
			}
		};
		return lstRecords(finder);
	}

	// //////////////////////////////////////////////////////////////
	public ArrayList<Record> lstRecordsByReportSql() throws JKDataAccessException {
		final JKFinder daoFinder = new JKFinder() {

			@Override
			public String getQuery() {
				return DynamicDao.this.tableMeta.getReportSql();
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, JKRecordNotFoundException, JKDataAccessException {
				return DaoUtil.readRecord(rs, DynamicDao.this.tableMeta);
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
			}
		};
		return lstRecords(daoFinder);
	}

	// //////////////////////////////////////////////////////////////
	public boolean saveRecord(final Record record) throws JKDataAccessException, JKRecordNotFoundException {
		if (record.isNewRecord() && record.isModified()) {
			insertRecord(record);
			return true;
		} else if (!record.isNewRecord() && record.isDeleted()) {
			deleteRecord(record);
			return true;
		} else if (record.isModified()) {
			updateRecord(record);
			return true;
		}
		// nothing updated
		return false;
	}

	// //////////////////////////////////////////////////////////////
	public void saveRecords(final ArrayList<Record> records) throws JKDataAccessException {
		final JKDataAccessException allExceptions = new JKDataAccessException();
		for (final Record record : records) {
			try {
				saveRecord(record);
			} catch (final JKDataAccessException e) {
				allExceptions.add(e);
			}
		}
	}

	// //////////////////////////////////////////////////////////////
	public void updateRecord(final Record record) throws JKRecordNotFoundException, JKDataAccessException {
		final JKUpdater updater = new JKUpdater() {

			@Override
			public String getQuery() {
				return DynamicDao.this.sqlBuilder.buildUpdate(record);
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				DaoUtil.setParamters(record, ps, false);
			}
		};
		final Record oldRecord = findRecord(record.getIdValue());
		callBeforeUpdateEventOnTriggers(oldRecord, record);
		executeUpdate(updater);
		callAfterUpdateEventOnTriggers(oldRecord, record);
		addUpdateAudit(oldRecord, record);
	}

	// private Session getSession() throws DaoException {
	// Session session = tableMeta.getDataSource().createSession();
	// setSession(session);
	// return session;
	// }
}
