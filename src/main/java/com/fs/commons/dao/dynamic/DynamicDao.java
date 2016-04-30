package com.fs.commons.dao.dynamic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.fs.commons.dao.AbstractDao;
import com.fs.commons.dao.DaoFinder;
import com.fs.commons.dao.DaoUpdater;
import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.Session;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.dynamic.trigger.Trigger;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.security.Audit;
import com.fs.commons.security.AuditType;

public class DynamicDao extends AbstractDao {
	protected final TableMeta tableMeta;
	protected MetaSqlBuilder sqlBuilder;

	// //////////////////////////////////////////////////////////////
	public DynamicDao(TableMeta tableMeta) {
		this(tableMeta, tableMeta.getDataSource());
	}

	// //////////////////////////////////////////////////////////////
	public DynamicDao(String tableMetaName) {
		this(AbstractTableMetaFactory.getTableMeta(tableMetaName));
	}

	// //////////////////////////////////////////////////////////////
	protected DynamicDao(TableMeta tableMeta, DataSource connectionManager) {
		super(connectionManager);
		this.tableMeta = tableMeta;
		sqlBuilder = new MetaSqlBuilder(tableMeta);
	}

	// //////////////////////////////////////////////////////////////
	public DynamicDao(TableMeta meta, Session session) {
		this(meta);
		setSession(session);
	}

	// //////////////////////////////////////////////////////////////
	public DynamicDao(String tableMetaName, Session session) {
		this(AbstractTableMetaFactory.getTableMeta(session.getConnectionManager(), tableMetaName));
		setSession(session);
	}

	// //////////////////////////////////////////////////////////////
	public String insertRecord(final Record record) throws DaoException {
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
		String id = executeUpdate(updater) + "";
		record.setIdValue(id);
		callAfterAddEventOnTriggers(record);
		addInsertAudit(record);
		return id;
	}

	// //////////////////////////////////////////////////////////////
	protected void callAfterAddEventOnTriggers(Record record) throws DaoException {
		ArrayList<Trigger> triggers = tableMeta.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).afterAdd(record);
		}
	}

	// //////////////////////////////////////////////////////////////
	protected void callBeforeAddEventOnTriggers(Record record) throws DaoException {
		ArrayList<Trigger> triggers = tableMeta.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).beforeAdd(record);
		}
	}

	// //////////////////////////////////////////////////////////////
	protected void addInsertAudit(Record record) throws DaoException {
		AuditType aUDIT_ADD_RECORD = AuditType.AUDIT_ADD_RECORD;
		Audit audit = createAudit(record, aUDIT_ADD_RECORD);
		addAudit(audit);
	}

	// //////////////////////////////////////////////////////////////
	public Audit createAudit(Record record, AuditType auditType) {
		Audit audit = new Audit();
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
	protected void addUpdateAudit(Record oldRecord, Record newRecord) throws DaoException {
		Audit audit = createAudit(newRecord, AuditType.AUDIT_UPDATE_RECORD);
		audit.setOldValue(oldRecord.toString(true));
		addAudit(audit);
	}

	// //////////////////////////////////////////////////////////////
	protected void addDeleteAudit(Record record) throws DaoException {
		Audit audit = createAudit(record, AuditType.AUDIT_DELETE_RECORD);
		addAudit(audit);
	}

	// //////////////////////////////////////////////////////////////
	public void insertRecords(ArrayList<Record> records) throws DaoException {
		if (records.size() > 0) {
			String insert = sqlBuilder.buildFatInsert(records);
			executeUpdate(insert);
		}
	}

	// //////////////////////////////////////////////////////////////
	public void updateRecord(final Record record) throws RecordNotFoundException, DaoException {
		DaoUpdater updater = new DaoUpdater() {

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				DaoUtil.setParamters(record, ps, false);
			}

			@Override
			public String getUpdateSql() {
				return sqlBuilder.buildUpdate(record);
			}
		};
		Record oldRecord = findRecord(record.getIdValue());
		callBeforeUpdateEventOnTriggers(oldRecord, record);
		executeUpdate(updater);
		callAfterUpdateEventOnTriggers(oldRecord, record);
		addUpdateAudit(oldRecord, record);
	}

	// //////////////////////////////////////////////////////////////
	protected void callAfterUpdateEventOnTriggers(Record oldRecord, Record newRecord) throws DaoException {
		ArrayList<Trigger> triggers = tableMeta.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).afterUpdate(oldRecord, newRecord);
		}
	}

	// //////////////////////////////////////////////////////////////
	protected void callBeforeUpdateEventOnTriggers(Record oldRecord, Record newRecord) throws DaoException {
		ArrayList<Trigger> triggers = tableMeta.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).beforeUpdate(oldRecord, newRecord);
		}
	}

	// //////////////////////////////////////////////////////////////
	public void deleteRecord(final Record record) throws RecordNotFoundException, DaoException {
		callBeforeDeleteEventOnTriggers(record);
		deleteRecord(record.getIdField());
		callAfterDeleteEventOnTriggers(record);
	}

	// //////////////////////////////////////////////////////////////
	protected void callAfterDeleteEventOnTriggers(Record record) throws DaoException {
		ArrayList<Trigger> triggers = tableMeta.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).afterDelete(record);
		}
	}

	// //////////////////////////////////////////////////////////////
	protected void callBeforeDeleteEventOnTriggers(Record record) throws DaoException {
		ArrayList<Trigger> triggers = tableMeta.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).beforeDelete(record);
		}
	}

	// //////////////////////////////////////////////////////////////
	public void deleteRecord(final Object recordId) throws RecordNotFoundException, DaoException {
		Record record = createEmptyRecord(false);
		record.setIdValue(recordId);
		deleteRecord(record);
	}

	// //////////////////////////////////////////////////////////////
	public void deleteRecord(final Field field) throws RecordNotFoundException, DaoException {
		deleteRecord(field, true);
	}

	// //////////////////////////////////////////////////////////////
	public void deleteRecord(final Field field, boolean addAudit) throws RecordNotFoundException, DaoException {
		DaoUpdater updater = new DaoUpdater() {

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
			}

			@Override
			public String getUpdateSql() {
				return sqlBuilder.buildDelete(field);
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
	public Record findRecord(final Object id) throws RecordNotFoundException, DaoException {
		// System.err.println("tring to find ID (" + id + ")on table " +
		// tableMeta.getTableName());
		DaoFinder finder = new DaoFinder() {

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
			}

			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return DaoUtil.readRecord(rs, tableMeta);
			}

			@Override
			public String getFinderSql() {
				return sqlBuilder.buildFindById(id.toString());
			}
		};
		Record record = (Record) findRecord(finder);
		callAfterFindEventOnTriggers(record);
		return record;
	}

	protected void callAfterFindEventOnTriggers(Record record) throws DaoException {
		ArrayList<Trigger> triggers = tableMeta.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).afterFind(record);
		}
	}

	// //////////////////////////////////////////////////////////////
	public ArrayList<Record> lstRecords(final Record filter) throws DaoException {
		DaoFinder finder = new DaoFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
			}

			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return DaoUtil.readRecord(rs, tableMeta);
			}

			@Override
			public String getFinderSql() {
				return sqlBuilder.buildFindByFilter(filter);
			}
		};
		return lstRecords(finder);
	}

	// //////////////////////////////////////////////////////////////
	public Record findRecord(final Record filter) throws RecordNotFoundException, DaoException {
		DaoFinder finder = new DaoFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
			}

			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return DaoUtil.readRecord(rs, tableMeta);
			}

			@Override
			public String getFinderSql() {
				return sqlBuilder.buildFindByFilter(filter);
			}
		};
		return (Record) findRecord(finder);
	}

	// //////////////////////////////////////////////////////////////
	public ArrayList<Record> lstRecords() throws RecordNotFoundException, DaoException {
		return lstRecords(createEmptyRecord(false));
	}

	// //////////////////////////////////////////////////////////////
	public boolean isIdExists(Object id) throws DaoException {
		try {
			findRecord(id.toString());
			return true;
		} catch (RecordNotFoundException e) {
			// just each the exception
			return false;
		} catch (DaoException e) {
			throw e;
		}
	}

	// //////////////////////////////////////////////////////////////
	public Record getFirstRecordInTable() throws RecordNotFoundException, DaoException {
		ArrayList<Record> r = lstRecords();
		if (r.size() > 0) {
			return r.get(0);
		}
		return null;
	}

	// //////////////////////////////////////////////////////////////
	public Record createEmptyRecord(boolean setDefaultValues) {
		return this.tableMeta.createEmptyRecord(setDefaultValues);
	}

	// //////////////////////////////////////////////////////////////
	public Record createEmptyRecord(boolean setDefaultValues, Record defaults) {
		return this.tableMeta.createEmptyRecord(setDefaultValues, defaults);
	}

	// //////////////////////////////////////////////////////////////
	public ArrayList<Record> findByFieldValue(String fieldName, Object value) throws DaoException {
		Record filterRecord = tableMeta.createEmptyRecord();
		filterRecord.setFieldValue(fieldName, value);
		return lstRecords(filterRecord);
	}

	// //////////////////////////////////////////////////////////////
	public Record findRecordByFieldValue(String fieldName, Object value) throws RecordNotFoundException, DaoException {
		Record filterRecord = tableMeta.createEmptyRecord();
		filterRecord.setFieldValue(fieldName, value);
		return findRecord(filterRecord);
	}

	public Record findRecordByFieldValue(HashMap<String, String> fieldNameToValue) throws RecordNotFoundException, DaoException {
		Record filterRecord = tableMeta.createEmptyRecord();
		for (String fieldName : fieldNameToValue.keySet()) {
			filterRecord.setFieldValue(fieldName, fieldNameToValue.get(fieldName));
		}
		return findRecord(filterRecord);
	}

	// //////////////////////////////////////////////////////////////
	public void saveRecords(ArrayList<Record> records) throws DaoException {
		DaoException allExceptions = new DaoException();
		for (Record record : records) {
			try {
				saveRecord(record);
			} catch (DaoException e) {
				allExceptions.add(e);
			}
		}
	}

	// //////////////////////////////////////////////////////////////
	public boolean saveRecord(Record record) throws DaoException, RecordNotFoundException {
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
	public void deleteByFieldValue(String fieldName, final Object value) throws RecordNotFoundException, DaoException {
		Record record = createEmptyRecord(false);
		record.setFieldValue(fieldName, value);
		deleteRecord(record.getField(fieldName), false);
	}

	// //////////////////////////////////////////////////////////////
	public void executeUpdateQuery(final String sql, final Object[] objects) throws DaoException {
		DaoUpdater updater = new DaoUpdater() {

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				for (int i = 0; i < objects.length; i++) {
					ps.setObject(i + 1, objects[i]);
				}
			}

			@Override
			public String getUpdateSql() {
				return sql;
			}
		};
		executeUpdate(updater);
	}

	// //////////////////////////////////////////////////////////////
	public void deleteAllRecords() throws DaoException {
		DaoUpdater updater = new DaoUpdater() {

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
			}

			@Override
			public String getUpdateSql() {
				return sqlBuilder.buildDelete();
			}
		};
		executeUpdate(updater);
	}

	// //////////////////////////////////////////////////////////////
	public MetaSqlBuilder getSqlBuilder() {
		return sqlBuilder;
	}

	// //////////////////////////////////////////////////////////////
	public ArrayList<Record> lstRecordsByReportSql() throws DaoException {
		DaoFinder daoFinder = new DaoFinder() {

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
			}

			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return DaoUtil.readRecord(rs, tableMeta);
			}

			@Override
			public String getFinderSql() {
				return tableMeta.getReportSql();
			}
		};
		return lstRecords(daoFinder);
	}

	public TableMeta getTableMeta() {
		return tableMeta;
	}

	/**
	 * 
	 * @param recordId
	 * @return
	 * @throws TableMetaNotFoundException
	 */
	public String getNotAssignedValuesQuery(int field1Id) throws TableMetaNotFoundException {
		StringBuffer buf = new StringBuffer();
		ArrayList<ForiegnKeyFieldMeta> list = tableMeta.lstForiegnKeyFields();
		ForiegnKeyFieldMeta field1 = list.get(0);
		ForiegnKeyFieldMeta field2 = list.get(1);
		TableMeta refTable = AbstractTableMetaFactory.getTableMeta(field2.getParentTable().getDataSource(), field2.getReferenceTable());
		String sql = refTable.getShortReportSql();
		sql += sql.toUpperCase().contains("WHERE") ? " AND " : " WHERE ";
		buf.append(sql);
		buf.append(field2.getReferenceField() + " NOT IN (");
		buf.append(" SELECT " + field2.getName() + " FROM " + field2.getParentTable().getTableName());
		buf.append(" WHERE " + field1.getName() + " = " + field1Id);
		buf.append(")");
		return buf.toString();
	}

	/**
	 * 
	 * @param recordId
	 * @return
	 * @throws TableMetaNotFoundException
	 */
	public String getAssignedValuesQuery(int field1Id) throws TableMetaNotFoundException {
		StringBuffer buf = new StringBuffer();
		ArrayList<ForiegnKeyFieldMeta> list = tableMeta.lstForiegnKeyFields();
		ForiegnKeyFieldMeta field1 = list.get(0);
		ForiegnKeyFieldMeta field2 = list.get(1);
		TableMeta refTable = AbstractTableMetaFactory.getTableMeta(field2.getParentTable().getDataSource(), field2.getReferenceTable());
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

	public void deleteByFieldsValues(HashMap<String, String> fieldNameToValue) throws DaoException {
		try {
			Record filterRecord = tableMeta.createEmptyRecord();
			Set<String> keySet = fieldNameToValue.keySet();
			for (String fieldName : keySet) {
				filterRecord.setFieldValue(fieldName, fieldNameToValue.get(fieldName));
			}
			ArrayList<Record> lstRecords = lstRecords(filterRecord);
			for (Record record : lstRecords) {
				deleteRecord(record);
			}

		} catch (RecordNotFoundException e) {
		}
	}

	/*
	 * @param filter
	 * 
	 * @throws DaoException
	 */
	public void deleteRecords(final Record filter) throws DaoException {
		ArrayList<Record> findRecord = lstRecords(filter);
		for (Record record : findRecord) {
			deleteRecord(record);
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public void cloneDetails(Object sourceIdValue, Object targetIdValue) throws DaoException {
		// TODO : refactor this method to be extarced to facade and avoid direct
		// calls from UI
		ArrayList<ForiegnKeyFieldMeta> detailFields = this.tableMeta.getDetailFields();
		Session session = getDataSource().createSession();
		boolean commit = false;
		try {
			for (ForiegnKeyFieldMeta foriegnKeyFieldMeta : detailFields) {
				DynamicDao detailDao = DaoFactory.createDynamicDao(foriegnKeyFieldMeta.getParentTable());
				detailDao.setSession(session);
				// Find detaile record for the original id
				List<Record> detailRecords = detailDao.findByFieldValue(foriegnKeyFieldMeta.getName(), sourceIdValue);
				// iterate over the records , change the id to the new id , and
				// reset the primary key
				for (Record detailRecord : detailRecords) {
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

	// private Session getSession() throws DaoException {
	// Session session = tableMeta.getDataSource().createSession();
	// setSession(session);
	// return session;
	// }
}
