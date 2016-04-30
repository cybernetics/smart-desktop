package com.fs.commons.desktop.dynform.ui.tabular;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.fs.commons.dao.dynamic.DaoFactory;
import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.comp.model.FSTableModel;
import com.fs.commons.desktop.swing.comp.model.FSTableRecord;

public class TableMetaModel extends FSTableModel {
	private final TableMeta tableMeta;
	private DynamicDao dao;

	/**
	 * 
	 * @param tableMeta
	 * @throws TableMetaNotFoundException
	 * @throws DaoException
	 */
	public TableMetaModel(TableMeta tableMeta) throws TableMetaNotFoundException, DaoException {
		this.tableMeta = tableMeta;
		dao = DaoFactory.createDynamicDao(tableMeta);
		init();
	}

	/**
	 * 
	 * @throws TableMetaNotFoundException
	 * @throws DaoException
	 */
	private void init() throws TableMetaNotFoundException, DaoException {
		Vector<FieldMeta> fieldList = tableMeta.getAllFields();
		for (FieldMeta fieldMeta : fieldList) {
			addFSTableColumn(fieldMeta.toFSTableColumn());
		}
	}

	@Override
	public FSTableRecord addRecord() {
		FSTableRecord record = super.addRecord();
		Vector<FieldMeta> fieldList = tableMeta.getAllFields();
		for (FieldMeta fieldMeta : fieldList) {
			record.setColumnValue(fieldMeta.getName(), fieldMeta.getDefaultValue());
		}
		return record;
	}

	public void setFilterValue(String fieldName, Object value) throws DaoException {
		clearRecords();
		ArrayList<Record> records = dao.findByFieldValue(fieldName, value);
		for (Record record : records) {
			FSTableRecord fsRecord= createEmptyRecord();
			record.populateTo(fsRecord);
			addRecord(fsRecord);
		}
	}

	public List<Record> getAllDaoRecords() throws TableMetaNotFoundException, DaoException {
		Vector<FSTableRecord> records = getRecords();
		List<Record> daoRecords=new Vector();
		for (FSTableRecord fsTableRecord : records) {
			Record daoRecord = tableMeta.createEmptyRecord();
			daoRecord.populateFrom(fsTableRecord);
			daoRecords.add(daoRecord);
		}		
		return daoRecords;
	}
}
