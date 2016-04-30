package com.fs.commons.desktop.swing.comp.model;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.desktop.swin.ConversionUtil;

public class FSTableRecord {
	public enum RecordStatus {
		NEW, LATEST, MODIFIED, DELETED;
	}

	Vector<FSTableColumnValue> columnsValues = new Vector<FSTableColumnValue>();
	RecordStatus status;

	// ////////////////////////////////////////////////////////////////////
	public void addEmptyValues(Vector<FSTableColumn> tableColumns) {
		for (FSTableColumn col : tableColumns) {
			FSTableColumnValue value = new FSTableColumnValue(col);
			columnsValues.add(value);
		}
	}

	// ////////////////////////////////////////////////////////////////////

	public Vector<FSTableColumnValue> getColumnsValues() {
		return columnsValues;
	}

	// ////////////////////////////////////////////////////////////////////
	public void setColumnsValues(Vector<FSTableColumnValue> values) {
		this.columnsValues = values;
	}

	// ////////////////////////////////////////////////////////////////////
	public RecordStatus getStatus() {
		return status;
	}

	// ////////////////////////////////////////////////////////////////////
	public void setStatus(RecordStatus status) {
		this.status = status;
	}

	// ////////////////////////////////////////////////////////////////////

	public void setColumnValue(int columnIndex, Object value) {
		columnsValues.get(columnIndex).setValue(value);
	}

	// ////////////////////////////////////////////////////////////////////
	public int getColumnValueAsInteger(int col) {
		// Object value = getColumnValue(col);
		// if (value == null) {
		// return 0;
		// }
		// if (value instanceof Boolean) {
		// return (Boolean) value ? 1 : 0;
		// }
		// return new Integer(value.toString());
		return (int) getColumnValueAsDouble(col);
	}

	// ////////////////////////////////////////////////////////////////////
	public Float getColumnValueAsFloat(int col) {
		Object value = getColumnValue(col);
		if (value == null) {
			return null;
		}
		return new Float(value.toString());
	}

	// ////////////////////////////////////////////////////////////////////
	public double getColumnValueAsDouble(int col) {
		Object value = getColumnValue(col);
		if (value == null) {
			return 0;
		}
		return new Double(value.toString());
	}

	// ////////////////////////////////////////////////////////////////////
	public Object getColumnValue(int col) {
		return columnsValues.get(col).getValue();
	}

	// ////////////////////////////////////////////////////////////////////
	public Object getColumnValue(String name) {
		return getColumnValue(getColumnIndex(name));
	}

	// ////////////////////////////////////////////////////////////////////
	public int getColumnIndex(String name) {
		for (int i = 0; i < columnsValues.size(); i++) {
			FSTableColumnValue value = columnsValues.get(i);
			if (value.getTableColumn().getName().toLowerCase().equals(name.toLowerCase())) {
				return i;
			}
		}
		return -1;
	}

	// ////////////////////////////////////////////////////////////////////
	public Vector<Object> toValuesVector() {
		Vector<Object> values = new Vector<Object>();
		for (FSTableColumnValue value : columnsValues) {
			values.add(value);
		}
		return values;
	}

	// ////////////////////////////////////////////////////////////////////
	public FSTableColumn getColumn(int index) {
		return columnsValues.get(index).getTableColumn();
	}

	// ////////////////////////////////////////////////////////////////////
	public Hashtable<String, Object> toHash() {
		Hashtable<String, Object> values = new Hashtable();
		for (int i = 0; i < columnsValues.size(); i++) {
			FSTableColumnValue value = columnsValues.get(i);
			values.put(getColumn(i).getName(), value);
		}
		return values;

	}

	public boolean isModified() {
		return getStatus()==RecordStatus.MODIFIED;
	}

	public Double getColumnValueAsDouble(int col, double defaultValue) {
		Double value = getColumnValueAsDouble(col);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	public void setColumnValue(String colName, Object value) {
		setColumnValue(getColumnIndex(colName), value);
	}

	public void setColumnEnabled(int col, boolean enable) {
		columnsValues.get(col).setEnabled(enable);
	}

	public boolean isColumnEnabled(int col) {
		return columnsValues.get(col).isEnabled();
	}

	public int getColumnValueAsInteger(String colName) {
		return getColumnValueAsInteger(getColumnIndex(colName));
	}

	public Date getColumnValueAsDate(int colIndex) {
		return ConversionUtil.toDate(getColumnValue(colIndex));
	}

	public double getColumnValueAsDouble(String colName) {
		return getColumnValueAsDouble(getColumnIndex(colName));
	}

	public Date getColumnValueAsDate(String colName) {
		return getColumnValueAsDate(getColumnIndex(colName));
	}

	public Field getField(String name) {
		// TODO Auto-generated method stub
		throw new IllegalStateException("unimplemneted method");
	}

	public void setFieldValue(String name, Object value) {
		setColumnValue(name, value);
	}

	public Object getFieldValue(String name) {
		return getColumnValue(name);
	}

	public void addEmptyValue(FSTableColumn col) {
		FSTableColumnValue value = new FSTableColumnValue(col);
		columnsValues.add(value);
	}

}
