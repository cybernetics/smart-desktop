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

	public void addEmptyValue(final FSTableColumn col) {
		final FSTableColumnValue value = new FSTableColumnValue(col);
		this.columnsValues.add(value);
	}

	// ////////////////////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////////////////////
	public void addEmptyValues(final Vector<FSTableColumn> tableColumns) {
		for (final FSTableColumn col : tableColumns) {
			final FSTableColumnValue value = new FSTableColumnValue(col);
			this.columnsValues.add(value);
		}
	}

	// ////////////////////////////////////////////////////////////////////
	public FSTableColumn getColumn(final int index) {
		return this.columnsValues.get(index).getTableColumn();
	}

	// ////////////////////////////////////////////////////////////////////
	public int getColumnIndex(final String name) {
		for (int i = 0; i < this.columnsValues.size(); i++) {
			final FSTableColumnValue value = this.columnsValues.get(i);
			if (value.getTableColumn().getName().toLowerCase().equals(name.toLowerCase())) {
				return i;
			}
		}
		return -1;
	}

	public Vector<FSTableColumnValue> getColumnsValues() {
		return this.columnsValues;
	}

	// ////////////////////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////////////////////
	public Object getColumnValue(final int col) {
		return this.columnsValues.get(col).getValue();
	}

	// ////////////////////////////////////////////////////////////////////
	public Object getColumnValue(final String name) {
		return getColumnValue(getColumnIndex(name));
	}

	public Date getColumnValueAsDate(final int colIndex) {
		return ConversionUtil.toDate(getColumnValue(colIndex));
	}

	public Date getColumnValueAsDate(final String colName) {
		return getColumnValueAsDate(getColumnIndex(colName));
	}

	// ////////////////////////////////////////////////////////////////////
	public double getColumnValueAsDouble(final int col) {
		final Object value = getColumnValue(col);
		if (value == null) {
			return 0;
		}
		return new Double(value.toString());
	}

	public Double getColumnValueAsDouble(final int col, final double defaultValue) {
		Double value = getColumnValueAsDouble(col);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	public double getColumnValueAsDouble(final String colName) {
		return getColumnValueAsDouble(getColumnIndex(colName));
	}

	// ////////////////////////////////////////////////////////////////////
	public Float getColumnValueAsFloat(final int col) {
		final Object value = getColumnValue(col);
		if (value == null) {
			return null;
		}
		return new Float(value.toString());
	}

	// ////////////////////////////////////////////////////////////////////
	public int getColumnValueAsInteger(final int col) {
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

	public int getColumnValueAsInteger(final String colName) {
		return getColumnValueAsInteger(getColumnIndex(colName));
	}

	public Field getField(final String name) {
		// TODO Auto-generated method stub
		throw new IllegalStateException("unimplemneted method");
	}

	public Object getFieldValue(final String name) {
		return getColumnValue(name);
	}

	// ////////////////////////////////////////////////////////////////////
	public RecordStatus getStatus() {
		return this.status;
	}

	public boolean isColumnEnabled(final int col) {
		return this.columnsValues.get(col).isEnabled();
	}

	public boolean isModified() {
		return getStatus() == RecordStatus.MODIFIED;
	}

	public void setColumnEnabled(final int col, final boolean enable) {
		this.columnsValues.get(col).setEnabled(enable);
	}

	// ////////////////////////////////////////////////////////////////////
	public void setColumnsValues(final Vector<FSTableColumnValue> values) {
		this.columnsValues = values;
	}

	public void setColumnValue(final int columnIndex, final Object value) {
		this.columnsValues.get(columnIndex).setValue(value);
	}

	public void setColumnValue(final String colName, final Object value) {
		setColumnValue(getColumnIndex(colName), value);
	}

	public void setFieldValue(final String name, final Object value) {
		setColumnValue(name, value);
	}

	// ////////////////////////////////////////////////////////////////////
	public void setStatus(final RecordStatus status) {
		this.status = status;
	}

	// ////////////////////////////////////////////////////////////////////
	public Hashtable<String, Object> toHash() {
		final Hashtable<String, Object> values = new Hashtable();
		for (int i = 0; i < this.columnsValues.size(); i++) {
			final FSTableColumnValue value = this.columnsValues.get(i);
			values.put(getColumn(i).getName(), value);
		}
		return values;

	}

	// ////////////////////////////////////////////////////////////////////
	public Vector<Object> toValuesVector() {
		final Vector<Object> values = new Vector<Object>();
		for (final FSTableColumnValue value : this.columnsValues) {
			values.add(value);
		}
		return values;
	}

}
