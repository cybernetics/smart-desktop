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
package com.fs.commons.desktop.dynform.ui.tabular;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.JKRecordNotFoundException;
import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.locale.Lables;

public class DynMetaModel extends AbstractTableModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final TableMeta meta;
	private ArrayList<Record> records = new ArrayList<>();
	private final ArrayList<Record> deletedRecords = new ArrayList<Record>();
	private DynamicDao dao;
	private Record filterRecord;

	// ////////////////////////////////////////////////////
	public DynMetaModel(final TableMeta meta, final boolean loadRecords) throws JKRecordNotFoundException, JKDataAccessException {
		this(meta, new DynamicDao(meta), loadRecords);
	}

	// ////////////////////////////////////////////////////
	public DynMetaModel(final TableMeta meta, final DynamicDao dynamicDao, final boolean loadRecords) throws JKRecordNotFoundException, JKDataAccessException {
		this.meta = meta;
		this.dao = dynamicDao;
		setFilterRecord(this.dao.createEmptyRecord(false));
		if (loadRecords) {
			loadRecords();
		}
	}

	public void clearRecords() {
		this.records = new ArrayList();
	}

	/**
	 *
	 * @param numberOfRecords
	 */
	public void createEmptyRecords(final int numberOfRecords) {
		for (int i = 0; i < numberOfRecords; i++) {
			this.records.add(this.dao.createEmptyRecord(true));
		}
		fireTableDataChanged();
	}

	/**
	 *
	 * @param selectedRow
	 */
	public void deleteRow(final int row) {
		final Record record = this.records.remove(row);
		record.setDeleted(true);
		this.deletedRecords.add(record);
		fireTableDataChanged();
	}

	/**
	 *
	 * @return
	 */
	public ArrayList<Record> getAllRecords() {
		final ArrayList<Record> allRecords = new ArrayList<Record>(this.records);
		allRecords.addAll(this.deletedRecords);
		return allRecords;
	}

	public String getColumnActualName(final int column) {
		return this.meta.getVisibleFields().get(column).getName();
	}

	@Override
	public Class<?> getColumnClass(final int columnIndex) {
		final FieldMeta fieldMeta = this.meta.getVisibleFields().get(columnIndex);
		return fieldMeta.getFieldClass();
	}

	@Override
	public int getColumnCount() {
		return this.meta.getVisibleFieldsCount();
	}

	@Override
	public String getColumnName(final int column) {
		return Lables.get(getColumnActualName(column));
	}

	public double getColunmSum(final int col) {
		double sum = 0;
		for (int i = 0; i < getRowCount(); i++) {
			sum += Integer.parseInt(getValueAt(i, col).toString());
		}
		return sum;
	}

	/**
	 * @return the dao
	 */
	public DynamicDao getDao() {
		return this.dao;
	}

	/**
	 * @return the filterRecord
	 */
	public Record getFilterRecord() {
		return this.filterRecord;
	}

	// ////////////////////////////////////////////////////
	public TableMeta getMeta() {
		return this.meta;
	}

	public Record getRecord(final int row) {
		return this.records.get(row);
	}

	/**
	 * @return
	 *
	 */
	public ArrayList<Record> getRecords() {
		return this.records;

	}

	@Override
	public int getRowCount() {
		return this.records == null ? 0 : this.records.size();
	}

	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		if (rowIndex >= this.records.size()) {
			return null;
		}
		final FieldMeta fieldMeta = this.meta.getVisibleFields().get(columnIndex);
		final Record record = this.records.get(rowIndex);
		return record.getFieldValue(fieldMeta.getName());
	}

	/**
	 *
	 * @param column
	 * @return
	 */
	public FieldMeta getVisibleField(final int column) {
		return this.meta.getVisibleFields().get(column);
	}

	/**
	 *
	 * @param selectedRow
	 * @return
	 */
	public boolean isNewRow(final int row) {
		return this.records.get(row).isNewRecord();
	}

	// ////////////////////////////////////////////////////
	private void loadRecords() throws JKRecordNotFoundException, JKDataAccessException {
		this.records = this.dao.lstRecords(this.filterRecord);
	}

	/*
	 *
	 */
	public void reload() throws JKRecordNotFoundException, JKDataAccessException {
		this.records.clear();
		this.deletedRecords.clear();
		loadRecords();
		fireTableDataChanged();
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(final DynamicDao dao) {
		this.dao = dao;
	}

	/**
	 * @param filterRecord
	 *            the filterRecord to set
	 */
	public void setFilterRecord(final Record filterRecord) {
		this.filterRecord = filterRecord;
	}

	/**
	 * @param fieldName
	 * @param trxId
	 */
	public void setFilterValue(final String fieldName, final String value) {
		getFilterRecord().setFieldValue(fieldName, value);
	}

	@Override
	public void setValueAt(final Object newValue, final int rowIndex, final int columnIndex) {
		if (rowIndex >= this.records.size()) {
			// the record maybe deleted before stop editing , so we just ignore
			// this call
		} else {
			final Record record = this.records.get(rowIndex);
			final Field field = record.getField(this.meta.getVisibleFields().get(columnIndex).getName());
			final Object oldValue = field.getValueObject();
			if (newValue == null && oldValue != null || oldValue == null && newValue != null
					|| newValue != null && oldValue != null && !newValue.equals(oldValue)) {
				record.setModified(true);
			}
			field.setValue(newValue);
		}
		fireTableCellUpdated(rowIndex, columnIndex);
	}
}
