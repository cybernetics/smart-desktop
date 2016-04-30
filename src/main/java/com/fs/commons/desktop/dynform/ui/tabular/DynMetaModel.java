package com.fs.commons.desktop.dynform.ui.tabular;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.locale.Lables;

public class DynMetaModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final TableMeta meta;
	private ArrayList<Record> records=new ArrayList<>();
	private ArrayList<Record> deletedRecords = new ArrayList<Record>();
	private DynamicDao dao;
	private Record filterRecord;

	// ////////////////////////////////////////////////////
	public DynMetaModel(TableMeta meta,boolean loadRecords) throws RecordNotFoundException, DaoException {
		this(meta, new DynamicDao(meta),loadRecords);
	}

	// ////////////////////////////////////////////////////
	public DynMetaModel(TableMeta meta, DynamicDao dynamicDao,boolean loadRecords) throws RecordNotFoundException, DaoException {
		this.meta = meta;
		dao = dynamicDao;
		setFilterRecord(dao.createEmptyRecord(false));
		if(loadRecords){
			loadRecords();
		}
	}

	/**
	 * @return the filterRecord
	 */
	public Record getFilterRecord() {
		return filterRecord;
	}

	/**
	 * @param filterRecord
	 *            the filterRecord to set
	 */
	public void setFilterRecord(Record filterRecord) {
		this.filterRecord = filterRecord;
	}

	/**
	 * @return the dao
	 */
	public DynamicDao getDao() {
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(DynamicDao dao) {
		this.dao = dao;
	}

	// ////////////////////////////////////////////////////
	private void loadRecords() throws RecordNotFoundException, DaoException {
		records = dao.lstRecords(filterRecord);
	}

	// ////////////////////////////////////////////////////
	public TableMeta getMeta() {
		return meta;
	}

	@Override
	public int getColumnCount() {
		return meta.getVisibleFieldsCount();
	}

	@Override
	public int getRowCount() {
		return records==null?0: records.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex >= records.size()) {
			return null;
		}
		FieldMeta fieldMeta = meta.getVisibleFields().get(columnIndex);
		Record record = records.get(rowIndex);
		return record.getFieldValue(fieldMeta.getName());
	}

	@Override
	public String getColumnName(int column) {
		return Lables.get(getColumnActualName(column));
	}

	public String getColumnActualName(int column) {
		return meta.getVisibleFields().get(column).getName();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		FieldMeta fieldMeta = meta.getVisibleFields().get(columnIndex);
		return fieldMeta.getFieldClass();
	}

	/**
	 * 
	 * @param column
	 * @return
	 */
	public FieldMeta getVisibleField(int column) {
		return meta.getVisibleFields().get(column);
	}

	@Override
	public void setValueAt(Object newValue, int rowIndex, int columnIndex) {
		if (rowIndex >= records.size()) {
			// the record maybe deleted before stop editing , so we just ignore
			// this call
		} else {
			Record record = records.get(rowIndex);
			Field field = record.getField(meta.getVisibleFields().get(columnIndex).getName());
			Object oldValue = field.getValueObject();
			if ((newValue == null && oldValue!=null) 
				|| (oldValue == null && newValue!=null)
				|| (newValue != null && oldValue != null && !newValue.equals(oldValue))) {				
				record.setModified(true);
			}
			field.setValue(newValue);
		}
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	/**
	 * 
	 * @param numberOfRecords
	 */
	public void createEmptyRecords(int numberOfRecords) {
		for (int i = 0; i < numberOfRecords; i++) {
			records.add(dao.createEmptyRecord(true));
		}
		fireTableDataChanged();
	}

	/**
	 * 
	 * @param selectedRow
	 * @return
	 */
	public boolean isNewRow(int row) {
		return records.get(row).isNewRecord();
	}

	/**
	 * 
	 * @param selectedRow
	 */
	public void deleteRow(int row) {
		Record record = records.remove(row);
		record.setDeleted(true);
		deletedRecords.add(record);
		fireTableDataChanged();
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Record> getAllRecords() {
		ArrayList<Record> allRecords = new ArrayList<Record>(this.records);
		allRecords.addAll(deletedRecords);
		return allRecords;
	}

	/*
	 * 
	 */
	public void reload() throws RecordNotFoundException, DaoException {
		records.clear();
		deletedRecords.clear();
		loadRecords();
		fireTableDataChanged();
	}

	/**
	 * @return
	 * 
	 */
	public ArrayList<Record> getRecords() {
		return records;

	}

	/**
	 * @param fieldName
	 * @param trxId
	 */
	public void setFilterValue(String fieldName, String value) {
		getFilterRecord().setFieldValue(fieldName, value);
	}

	public void clearRecords() {
		records=new ArrayList();
	}

	public Record getRecord(int row) {
		return records.get(row);
	}

	public double getColunmSum(int col) {
		double sum=0;
		for(int i=0;i<getRowCount();i++){
			sum+=Integer.parseInt(getValueAt(i, col).toString());
		}
		return sum;
	}
}
