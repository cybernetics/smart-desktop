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

	public Record(TableMeta meta) {
		this.tableMeta = meta;
	}

	/**
	 * @return the tableMeta
	 */
	public TableMeta getTableMeta() {
		return tableMeta;
	}

	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * @param deleted
	 *            the deleted to set
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
		if (deleted) {
			setModified(false);
		}
	}

	/**
	 * @return the newRecord
	 */
	public boolean isNewRecord() {
		IdFieldMeta field = (IdFieldMeta) getIdField().getMeta();
		// ignore user value if id is already set
		if (field.isAutoIncrement() && (getIdValue() != null && !getIdValue().equals(""))) {
			return false;
		}
		return newRecord;
	}

	/**
	 * @param newRecord
	 *            the newRecord to set
	 */
	public void setNewRecord(boolean newRecord) {
		this.newRecord = newRecord;
	}

	public ArrayList<Field> getFields() {
		return fields;
	}

	// public void setFields(ArrayList<Field> fields) {
	// this.fields = fields;
	// }

	public Field getField(int index) {
		return fields.get(index);
	}

	public int getFieldsCount() {
		return fields.size();
	}

	public void addField(Field field) {
		fields.add(field);
	}

	public void setFieldValue(String fieldName, Object value) {
		for (int i = 0; i < fields.size(); i++) {
			if (fields.get(i).getMeta().getName().equals(fieldName)) {
				fields.get(i).setValue(value);
				return;
			}
		}
		throw new RuntimeException("Field " + fieldName + " didnt set in  this record");
	}

	public Field getIdField() {
		return idField;
	}

	public void setIdField(Field idField) {
		this.idField = idField;
	}

	public void setIdValue(Object value) {
		idField.setValue(value);
	}

	public Object getIdValue() {
		return idField.getValue();
	}

	public int getIdValueAsInteger() {
		return idField.getValueAsInteger();
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
	public String toString(boolean valuesOnly) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
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

	public Field getField(String name) {
		for (int i = 0; i < fields.size(); i++) {
			if (fields.get(i).getMeta().getName().equals(name)) {
				return fields.get(i);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param masterFieldName
	 * @return
	 */
	public Field getForiegnKeyFieldByMasterFieldName(String masterFieldName) {
		for (int i = 0; i < fields.size(); i++) {
			if (fields.get(i).getMeta() instanceof ForiegnKeyFieldMeta) {
				ForiegnKeyFieldMeta field = (ForiegnKeyFieldMeta) fields.get(i).getMeta();
				if (field.getReferenceField().equals(masterFieldName)) {
					return fields.get(i);
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Field> lstSummaryFields() {
		// TODO : use the lstSummaryFields in tableMeta
		ArrayList<Field> summaryFields = new ArrayList<Field>();
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			if (field.getMeta().isSummaryField()) {
				summaryFields.add(field);
			}
		}
		if (summaryFields.size() == 0 && fields.size() > 1) {
			summaryFields.add(fields.get(1));
		}
		return summaryFields;
	}

	/**
	 * 
	 * @return
	 * @throws DaoException
	 * @throws RecordNotFoundException
	 */
	public String getSummaryValue() throws RecordNotFoundException, DaoException {
		ArrayList<Field> fields = lstSummaryFields();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			if (field.getValue() != null) {
				String summaryValue;
				if (field.getMeta() instanceof ForiegnKeyFieldMeta) {
					ForiegnKeyFieldMeta meta = (ForiegnKeyFieldMeta) field.getMeta();
					DynamicDao dao = new DynamicDao(meta.getReferenceTable());
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
	 * 
	 * @param string
	 * @return
	 */
	public Object getFieldValue(String fieldName) {
		return getField(fieldName).getValueObject();
	}

	public int getFieldValueAsInteger(String fieldName) {
		return getField(fieldName).getValueAsInteger();
	}

	public float getFieldValueAsFloat(String fieldName) {
		return getField(fieldName).getValueAsFloat();
	}

	public String getFieldValueAsString(String fieldName) {
		return getField(fieldName).getValue();
	}

	public Boolean getFieldValueAsBoolean(String fieldName) {
		return getField(fieldName).getValueAsBoolean();
	}

	/**
	 * 
	 * @param columnIndex
	 * @return
	 */
	public Field getVisibleField(int visibleIndex) {
		int counter = 0;
		for (Field field : fields) {
			if (field.getMeta().isVisible()) {
				if (counter++ == visibleIndex) {
					return field;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param b
	 */
	public void setModified(boolean modified) {
		this.modified = modified;
	}

	/**
	 * @return the modified
	 */
	public boolean isModified() {
		if (modified) {
			if (isNewRecord()) {
				// id new record visible fields as null or having default
				// values, this will be considered as unmodified record ,
				// mainly used in tabular component
				for (Field field : fields) {
					FieldMeta fieldMeta = field.getMeta();
					String value = field.getValue();
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
	 * 
	 * @param record
	 */
	public void setValues(boolean overwriteIfNull, Record... records) {
		for (Record record : records) {
			for (Field field : fields) {
				if (record.getField(field.getFieldName()) != null) {
					if (record.getFieldValue(field.getFieldName()) != null || overwriteIfNull) {
						field.setValue(record.getFieldValue(field.getFieldName()));
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param record
	 * @param account
	 * @param supplier
	 */
	public void setValues(Record... record) {
		setValues(false, record);
	}

	public void prepareForAdd() {
		setIdValue(null);
		setModified(true);
		setNewRecord(true);
	}

	public String concatFieldsValue(String separator, String... fieldNames) {
		StringBuffer buf = new StringBuffer();
		for (String fieldName : fieldNames) {
			buf.append(getFieldValue(fieldName).toString());
			buf.append(separator);
		}
		return buf.toString();
	}

	public Object toObject() {
		if (getTableMeta().getBeanName() == null) {
			throw new IllegalStateException("Please set 'class' property for meta " + getTableMeta().getTableName());
		}
		Object bean = GeneralUtility.createClass(getTableMeta().getBeanName());
		try {
			BeanUtil util = new BeanUtil(bean.getClass());
			Hashtable<Object, Object> map = new Hashtable<Object, Object>();
			if (getIdField().getValueObject() != null) {
				map.put(getIdField().getMeta().getPropertyName(), getIdField().getValueObject());
			}
			ArrayList<Field> fields = getFields();
			for (Field field : fields) {
				if (field.getValueObject() != null) {
					if (field.getMeta() instanceof ForiegnKeyFieldMeta) {
						ForiegnKeyFieldMeta meta = (ForiegnKeyFieldMeta) field.getMeta();
						TableMeta referenceTableMeta = meta.getReferenceTableMeta();
						if (referenceTableMeta.getBeanName() != null) {
							Record record = referenceTableMeta.createEmptyRecord();
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
		} catch (BeanUtilException e) {
			ExceptionUtil.handleException(e);
		}
		return bean;
	}

	// /////////////////////////////////////////////////////////////////////////
	public void setValues(Hashtable<String, Object> hash) {
		Enumeration<String> keys = hash.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if (getField(key) != null) {
				setFieldValue(key, hash.get(key));
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	public ArrayList<Object> getFieldValues() {
		ArrayList<Field> fields = getFields();
		ArrayList<Object> values = new ArrayList<Object>();
		for (Field field : fields) {
			values.add(field.getValueObject());
		}
		return values;
	}

	public boolean isIdAutoIncrement() {
		IdFieldMeta meta = (IdFieldMeta) getIdField().getMeta();
		return meta.isAutoIncrement();
	}

	public String getGui() {
		return gui;
	}

	public void setGui(String gui) {
		this.gui = gui;
	}

	public double getFieldValueAsDouble(String fieldName) {
		return getField(fieldName).getValueAsDouble();
	}

	public ArrayList<Object> getFieldsValues() {
		ArrayList<Object> objs = new ArrayList<Object>();
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			objs.add(field.getValueObject());
		}
		return objs;
	}

	public Date getFieldValueAsDate(String fieldName) {
		return ConversionUtil.toDate(getFieldValue(fieldName));
	}

	public java.sql.Date getFieldValueAsSqlDate(String fieldName) {
		Date value = getFieldValueAsDate(fieldName);
		if (value != null) {
			return new java.sql.Date(value.getTime());
		}
		return null;
	}

	public Time getFieldValueAsTime(String fieldName) {
		return ConversionUtil.toTime(getFieldValue(fieldName));
	}

	public void populateTo(FSTableRecord fsTableRecord) throws TableMetaNotFoundException, DaoException {
		ArrayList<Field> fields = getFields();
		for (Field field : fields) {
			fsTableRecord.addEmptyValue(field.getMeta().toFSTableColumn());
			fsTableRecord.setFieldValue(field.getFieldName(), field.getValue());
			fsTableRecord.setStatus(RecordStatus.LATEST);
		}
	}
	
	public void populateFrom(FSTableRecord fsTableRecord){
		ArrayList<Field> fields = getFields();
		setNewRecord(fsTableRecord.getStatus()==RecordStatus.NEW);
		for (Field field : fields) {
			field.setValue(fsTableRecord.getFieldValue(field.getFieldName()));
		}

	}
	
}
