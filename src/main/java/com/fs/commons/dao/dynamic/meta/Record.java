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
package com.fs.commons.dao.dynamic.meta;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import com.fs.commons.bean.util.BeanUtil;
import com.fs.commons.bean.util.BeanUtilException;
import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.desktop.swin.ConversionUtil;
import com.fs.commons.desktop.swing.comp.model.FSTableRecord;
import com.fs.commons.desktop.swing.comp.model.FSTableRecord.RecordStatus;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

public class Record {
	Field idField;
	boolean newRecord;
	boolean deleted;
	TableMeta tableMeta;
	ArrayList<Field> fields = new ArrayList<Field>();
	private boolean modified;
	// TODO : fix my name
	String gui;

	public Record(final TableMeta meta) {
		this.tableMeta = meta;
	}

	public void addField(final Field field) {
		this.fields.add(field);
	}

	public String concatFieldsValue(final String separator, final String... fieldNames) {
		final StringBuffer buf = new StringBuffer();
		for (final String fieldName : fieldNames) {
			buf.append(getFieldValue(fieldName).toString());
			buf.append(separator);
		}
		return buf.toString();
	}

	public Field getField(final int index) {
		return this.fields.get(index);
	}

	public Field getField(final String name) {
		for (int i = 0; i < this.fields.size(); i++) {
			if (this.fields.get(i).getMeta().getName().equals(name)) {
				return this.fields.get(i);
			}
		}
		return null;
	}

	public ArrayList<Field> getFields() {
		return this.fields;
	}

	public int getFieldsCount() {
		return this.fields.size();
	}

	// public void setFields(ArrayList<Field> fields) {
	// this.fields = fields;
	// }

	public ArrayList<Object> getFieldsValues() {
		final ArrayList<Object> objs = new ArrayList<Object>();
		for (int i = 0; i < this.fields.size(); i++) {
			final Field field = this.fields.get(i);
			objs.add(field.getValueObject());
		}
		return objs;
	}

	/**
	 *
	 * @param string
	 * @return
	 */
	public Object getFieldValue(final String fieldName) {
		return getField(fieldName).getValueObject();
	}

	public Boolean getFieldValueAsBoolean(final String fieldName) {
		return getField(fieldName).getValueAsBoolean();
	}

	public Date getFieldValueAsDate(final String fieldName) {
		return ConversionUtil.toDate(getFieldValue(fieldName));
	}

	public double getFieldValueAsDouble(final String fieldName) {
		return getField(fieldName).getValueAsDouble();
	}

	public float getFieldValueAsFloat(final String fieldName) {
		return getField(fieldName).getValueAsFloat();
	}

	public int getFieldValueAsInteger(final String fieldName) {
		return getField(fieldName).getValueAsInteger();
	}

	public java.sql.Date getFieldValueAsSqlDate(final String fieldName) {
		final Date value = getFieldValueAsDate(fieldName);
		if (value != null) {
			return new java.sql.Date(value.getTime());
		}
		return null;
	}

	public String getFieldValueAsString(final String fieldName) {
		return getField(fieldName).getValue();
	}

	public Time getFieldValueAsTime(final String fieldName) {
		return ConversionUtil.toTime(getFieldValue(fieldName));
	}

	// /////////////////////////////////////////////////////////////////////////
	public ArrayList<Object> getFieldValues() {
		final ArrayList<Field> fields = getFields();
		final ArrayList<Object> values = new ArrayList<Object>();
		for (final Field field : fields) {
			values.add(field.getValueObject());
		}
		return values;
	}

	/**
	 *
	 * @param masterFieldName
	 * @return
	 */
	public Field getForiegnKeyFieldByMasterFieldName(final String masterFieldName) {
		for (int i = 0; i < this.fields.size(); i++) {
			if (this.fields.get(i).getMeta() instanceof ForiegnKeyFieldMeta) {
				final ForiegnKeyFieldMeta field = (ForiegnKeyFieldMeta) this.fields.get(i).getMeta();
				if (field.getReferenceField().equals(masterFieldName)) {
					return this.fields.get(i);
				}
			}
		}
		return null;
	}

	public String getGui() {
		return this.gui;
	}

	public Field getIdField() {
		return this.idField;
	}

	public Object getIdValue() {
		return this.idField.getValue();
	}

	public int getIdValueAsInteger() {
		return this.idField.getValueAsInteger();
	}

	/**
	 *
	 * @return
	 * @throws DaoException
	 * @throws RecordNotFoundException
	 */
	public String getSummaryValue() throws RecordNotFoundException, DaoException {
		final ArrayList<Field> fields = lstSummaryFields();
		final StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < fields.size(); i++) {
			final Field field = fields.get(i);
			if (field.getValue() != null) {
				String summaryValue;
				if (field.getMeta() instanceof ForiegnKeyFieldMeta) {
					final ForiegnKeyFieldMeta meta = (ForiegnKeyFieldMeta) field.getMeta();
					final DynamicDao dao = new DynamicDao(meta.getReferenceTable());
					summaryValue = dao.findRecord(field.getValue()).getSummaryValue();
				} else {
					summaryValue = field.getValue() + " ";
				}
				buffer.append(summaryValue);
			}
		}
		return buffer.toString();
	}

	/**
	 * @return the tableMeta
	 */
	public TableMeta getTableMeta() {
		return this.tableMeta;
	}

	/**
	 *
	 * @param columnIndex
	 * @return
	 */
	public Field getVisibleField(final int visibleIndex) {
		int counter = 0;
		for (final Field field : this.fields) {
			if (field.getMeta().isVisible()) {
				if (counter++ == visibleIndex) {
					return field;
				}
			}
		}
		return null;
	}

	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return this.deleted;
	}

	public boolean isIdAutoIncrement() {
		final IdFieldMeta meta = (IdFieldMeta) getIdField().getMeta();
		return meta.isAutoIncrement();
	}

	/**
	 * @return the modified
	 */
	public boolean isModified() {
		if (this.modified) {
			if (isNewRecord()) {
				// id new record visible fields as null or having default
				// values, this will be considered as unmodified record ,
				// mainly used in tabular component
				for (final Field field : this.fields) {
					final FieldMeta fieldMeta = field.getMeta();
					final String value = field.getValue();
					if (fieldMeta.isVisible() && value != null && !value.equals("") && !value.equals(fieldMeta.getDefaultValue())) {
						return true;
					}
				}
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the newRecord
	 */
	public boolean isNewRecord() {
		final IdFieldMeta field = (IdFieldMeta) getIdField().getMeta();
		// ignore user value if id is already set
		if (field.isAutoIncrement() && getIdValue() != null && !getIdValue().equals("")) {
			return false;
		}
		return this.newRecord;
	}

	/**
	 *
	 * @return
	 */
	public ArrayList<Field> lstSummaryFields() {
		// TODO : use the lstSummaryFields in tableMeta
		final ArrayList<Field> summaryFields = new ArrayList<Field>();
		for (int i = 0; i < this.fields.size(); i++) {
			final Field field = this.fields.get(i);
			if (field.getMeta().isSummaryField()) {
				summaryFields.add(field);
			}
		}
		if (summaryFields.size() == 0 && this.fields.size() > 1) {
			summaryFields.add(this.fields.get(1));
		}
		return summaryFields;
	}

	public void populateFrom(final FSTableRecord fsTableRecord) {
		final ArrayList<Field> fields = getFields();
		setNewRecord(fsTableRecord.getStatus() == RecordStatus.NEW);
		for (final Field field : fields) {
			field.setValue(fsTableRecord.getFieldValue(field.getFieldName()));
		}

	}

	public void populateTo(final FSTableRecord fsTableRecord) throws TableMetaNotFoundException, DaoException {
		final ArrayList<Field> fields = getFields();
		for (final Field field : fields) {
			fsTableRecord.addEmptyValue(field.getMeta().toFSTableColumn());
			fsTableRecord.setFieldValue(field.getFieldName(), field.getValue());
			fsTableRecord.setStatus(RecordStatus.LATEST);
		}
	}

	public void prepareForAdd() {
		setIdValue(null);
		setModified(true);
		setNewRecord(true);
	}

	/**
	 * @param deleted
	 *            the deleted to set
	 */
	public void setDeleted(final boolean deleted) {
		this.deleted = deleted;
		if (deleted) {
			setModified(false);
		}
	}

	public void setFieldValue(final String fieldName, final Object value) {
		for (int i = 0; i < this.fields.size(); i++) {
			if (this.fields.get(i).getMeta().getName().equals(fieldName)) {
				this.fields.get(i).setValue(value);
				return;
			}
		}
		throw new RuntimeException("Field " + fieldName + " didnt set in  this record");
	}

	public void setGui(final String gui) {
		this.gui = gui;
	}

	public void setIdField(final Field idField) {
		this.idField = idField;
	}

	public void setIdValue(final Object value) {
		this.idField.setValue(value);
	}

	/**
	 *
	 * @param b
	 */
	public void setModified(final boolean modified) {
		this.modified = modified;
	}

	/**
	 * @param newRecord
	 *            the newRecord to set
	 */
	public void setNewRecord(final boolean newRecord) {
		this.newRecord = newRecord;
	}

	/**
	 *
	 * @param record
	 */
	public void setValues(final boolean overwriteIfNull, final Record... records) {
		for (final Record record : records) {
			for (final Field field : this.fields) {
				if (record.getField(field.getFieldName()) != null) {
					if (record.getFieldValue(field.getFieldName()) != null || overwriteIfNull) {
						field.setValue(record.getFieldValue(field.getFieldName()));
					}
				}
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	public void setValues(final Hashtable<String, Object> hash) {
		final Enumeration<String> keys = hash.keys();
		while (keys.hasMoreElements()) {
			final String key = keys.nextElement();
			if (getField(key) != null) {
				setFieldValue(key, hash.get(key));
			}
		}
	}

	/**
	 *
	 * @param record
	 * @param account
	 * @param supplier
	 */
	public void setValues(final Record... record) {
		setValues(false, record);
	}

	public Object toObject() {
		if (getTableMeta().getBeanName() == null) {
			throw new IllegalStateException("Please set 'class' property for meta " + getTableMeta().getTableName());
		}
		final Object bean = GeneralUtility.createClass(getTableMeta().getBeanName());
		try {
			final BeanUtil util = new BeanUtil(bean.getClass());
			final Hashtable<Object, Object> map = new Hashtable<Object, Object>();
			if (getIdField().getValueObject() != null) {
				map.put(getIdField().getMeta().getPropertyName(), getIdField().getValueObject());
			}
			final ArrayList<Field> fields = getFields();
			for (final Field field : fields) {
				if (field.getValueObject() != null) {
					if (field.getMeta() instanceof ForiegnKeyFieldMeta) {
						final ForiegnKeyFieldMeta meta = (ForiegnKeyFieldMeta) field.getMeta();
						final TableMeta referenceTableMeta = meta.getReferenceTableMeta();
						if (referenceTableMeta.getBeanName() != null) {
							final Record record = referenceTableMeta.createEmptyRecord();
							record.setIdValue(field.getValueObject());
							map.put(field.getMeta().getPropertyName(), record.toObject());
						} else {
							System.err.println("Error while set property for field " + field.getFieldName());
							System.err.println(" class property is null for meta " + referenceTableMeta.getTableName());
						}
					} else {
						map.put(field.getMeta().getPropertyName(), field.getValueObject());
					}
				}
			}
			util.populate(map, bean);
		} catch (final BeanUtilException e) {
			ExceptionUtil.handleException(e);
		}
		return bean;
	}

	@Override
	public String toString() {
		return toString(false);
	}

	/**
	 *
	 * @param valuesOnly
	 * @return
	 */
	public String toString(final boolean valuesOnly) {
		final StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < this.fields.size(); i++) {
			final Field field = this.fields.get(i);
			if (valuesOnly) {
				buffer.append(Lables.get(field.getFieldName()));
				buffer.append(" : ");
				buffer.append(field.getValue() == null ? "-" : field.getValue());
			} else {
				buffer.append(field.toString());
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}

}
