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

import java.text.Format;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.desktop.swing.comp.model.FSTableRecord.RecordStatus;
import com.fs.commons.util.FinancialUtility;

public class FSTableModel extends AbstractTableModel {
	/**
	 *
	 * @author jalal
	 *
	 */
	class ColumnVisiblityManagar {
		final Vector<FSTableColumn> columns;

		// ///////////////////////////////////////////////////////////
		public ColumnVisiblityManagar(final Vector<FSTableColumn> columns) {
			this.columns = columns;
			refreshVisibility();
		}

		// ///////////////////////////////////////////////////////////
		public int getActualIndexFromVisibleIndex(final int visibleIndex) {
			return getFSTableColumnFromVisibleIndex(visibleIndex).getIndex();
		}

		// ///////////////////////////////////////////////////////////
		public Vector<FSTableColumn> getColumns() {
			return this.columns;
		}

		// ///////////////////////////////////////////////////////////
		public FSTableColumn getFSTableColumnFromVisibleIndex(final int visibleIndex) {
			for (final FSTableColumn col : this.columns) {
				if (col.getVisibleIndex() == visibleIndex) {
					return col;
				}
			}
			throw new ArrayIndexOutOfBoundsException(visibleIndex);
		}
		// ///////////////////////////////////////////////////////////

		public int getVisibleColumnCount() {
			int count = 0;
			for (final FSTableColumn col : this.columns) {
				if (col.isVisible()) {
					count++;
				}
			}
			return count;
		}

		// ///////////////////////////////////////////////////////////
		public int getVisibleIndexFromActualIndex(final int actualIndex) {
			return this.columns.get(actualIndex).getVisibleIndex();
		}

		// ///////////////////////////////////////////////////////////
		protected void refreshVisibility() {
			int visibleIndex = 0;
			for (final FSTableColumn col : this.columns) {
				if (col.isVisible()) {
					col.setVisibleIndex(visibleIndex++);
				} else {
					col.setVisibleIndex(-1);
				}
			}
		}

	}

	/**
	 *
	 */
	private static final long serialVersionUID = -6003811835691538215L;

	private final Vector<FSTableColumn> tableColumns = new Vector<FSTableColumn>();
	private final Vector<FSTableRecord> records = new Vector<FSTableRecord>();
	private final ColumnVisiblityManagar visibilityManager = new ColumnVisiblityManagar(this.tableColumns);
	private final Vector<FSTableRecord> deletedRecords = new Vector<FSTableRecord>();
	boolean modified;

	boolean allowDelete;

	// /////////////////////////////////////////////////////////////////////////
	public FSTableModel() {
	}

	// ///////////////////////////////////////////////////////////////////
	public void addFSTableColumn(final FSTableColumn col) {
		col.setIndex(this.tableColumns.size());
		this.tableColumns.add(col);
		this.visibilityManager.refreshVisibility();
		fireTableStructureChanged();
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public FSTableRecord addRecord() {
		final FSTableRecord record = createEmptyRecord();
		addRecord(record);
		return record;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public void addRecord(final FSTableRecord record) {
		geteRecords().add(record);
		fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
	}

	// //////////////////////////////////////////////////////////////////////
	public void clearRecords() {
		this.records.clear();
		fireTableDataChanged();
	}

	// /////////////////////////////////////////////////////////
	protected FSTableRecord createEmptyRecord() {
		final FSTableRecord record = new FSTableRecord();
		record.addEmptyValues(this.tableColumns);
		record.setStatus(RecordStatus.NEW);
		return record;
	}

	// /////////////////////////////////////////////////////////
	public FSTableRecord deleteRow(final int selectedRow) {
		final FSTableRecord removed = removeRecord(selectedRow);
		removed.setStatus(RecordStatus.DELETED);
		// Object removed = getDataVector().remove(selectedRow);
		if (removed != null) {
			this.deletedRecords.add(removed);
		}
		fireTableRowsDeleted(selectedRow, selectedRow);
		return removed;
	}

	// //////////////////////////////////////////////////////////////////////
	public void deleteRows(final int[] rows) {
		for (int i = rows.length - 1; i >= 0; i--) {
			deleteRow(rows[i]);
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////

	public void fireTableColumnDataChanged(final int col) {
		for (int i = 0; i < getRowCount(); i++) {
			fireTableCellUpdated(i, col);
		}
	}

	public int getActualColumnCount() {
		return this.tableColumns.size();
	}

	public int getActualColumnIndexFromVisible(final int visibleIndex) {
		return getTableColumn(visibleIndex, true).getIndex();
	}

	// /////////////////////////////////////////////////////////////////////
	public String getActualColumnName(final int index) {
		return getTableColumn(index).getName();
	}

	public ArrayList<Record> getAllRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	// /////////////////////////////////////////////////////////
	public TableCellEditor getCellEditor(final int column) {
		return getTableColumn(column).getEditor();
	}

	// /////////////////////////////////////////////////////////
	public TableCellRenderer getCellRenderer(final int column) {
		return getTableColumn(column).getRenderer();
	}

	// ///////////////////////////////////////////////////////////////////////////////
	@Override
	public Class getColumnClass(final int columnIndex) {
		return getTableColumn(columnIndex).getColumnClass();
		// try {
		// String columnClassName =
		// getTableColumn(columnIndex).getColumnClassName();
		// //
		// System.out.println("Coluinm name : "+getColumnName(columnIndex)+"
		// class = "+
		// // columnClassName);
		// Class<?> clas = Class.forName(columnClassName);
		// if (clas.isInstance(BigDecimal.class)) {
		// return Double.class;
		// }
		// return clas;
		// } catch (Exception e) {
		// ExceptionUtil.handleException(e);
		// return null;
		// }
	}

	// // /////////////////////////////////////////////////////////
	// public FSTableColumn getTableColumn(int col) {
	// return getTableColumn(col, true);
	// }

	// /////////////////////////////////////////////////////////////////////
	@Override
	public int getColumnCount() {
		return this.visibilityManager.getVisibleColumnCount();
	}

	@Override
	public String getColumnName(final int visibleColumnIndex) {
		// FSTableColumn tableColumn = getTableColumn(visibleColumnIndex);
		// return
		// tableColumn.getHumanName()+"-"+tableColumn.getVisibleIndex()+'-'+tableColumn.getIndex();
		return getTableColumn(visibleColumnIndex).getHumanName();
	}

	// /////////////////////////////////////////////////////////////////////
	public int getColumnType(final int col) {
		return getTableColumn(col).getColumnType();
	}

	// /////////////////////////////////////////////////////////////////////
	public int getColunmIndex(final String name) {
		for (int i = 0; i < getColumnCount(); i++) {
			if (getActualColumnName(i).trim().equalsIgnoreCase(name)) {
				return i;
			}
		}
		return -1;
	}

	// /////////////////////////////////////////////////////////////////////////////
	public int getColunmIndexByName(final String colName) {
		for (final FSTableColumn col : this.tableColumns) {
			if (col.getName().equalsIgnoreCase(colName)) {
				return col.getVisibleIndex();
			}
		}
		return -1;
	}

	// /////////////////////////////////////////////////////////////////////////
	public double getColunmSum(final int col) {
		double sum = 0;
		for (int i = 0; i < getRowCount(); i++) {
			try {
				final double number = getValueAtAsDouble(i, col);
				sum = FinancialUtility.addAmounts(sum, number);
			} catch (final NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return sum;
	}

	public Vector<Record> getDaoRecords() {
		// TODO
		return null;
	}

	// /////////////////////////////////////////////////////////
	public Vector<FSTableRecord> getDeletedRecords() {
		return this.deletedRecords;
	}

	// ///////////////////////////////////////////////////////////////////
	public Vector<Vector> getDeletedRecordsAsDataVector() {
		final Vector<Vector> data = new Vector<Vector>();
		for (final FSTableRecord rec : this.deletedRecords) {
			data.add(rec.toValuesVector());
		}
		return data;
	}

	private Vector<FSTableRecord> geteRecords() {
		return this.records;
	}

	// /////////////////////////////////////////////////////////
	public Format getFormatter(final int col) {
		return getTableColumn(col).getFormatter();
	}

	// /////////////////////////////////////////////////////////////////////////
	public int getIntegerColunmSum(final int col) {
		return (int) getColunmSum(col);
	}

	// /////////////////////////////////////////////////////////
	public int getPrefferedWidth(final int column) {
		return getTableColumn(column).getPreferredWidth();
	}

	// /////////////////////////////////////////////////////////////////////////////
	public FSTableRecord getRecord(final int row) {
		if (row >= getRowCount()) {
			throw new IllegalStateException("Row : " + row + " is out of index");
		}
		return this.records.get(row);
	}

	// ///////////////////////////////////////////////////////////////////
	public Vector<FSTableRecord> getRecords() {
		return this.records;
	}

	// ///////////////////////////////////////////////////////////////////
	public Vector<Vector> getRecordsAsDataVector() {
		final Vector<Vector> data = new Vector<Vector>();
		for (final FSTableRecord rec : this.records) {
			data.add(rec.toValuesVector());
		}
		return data;
	}

	// // /////////////////////////////////////////////////////////
	// public Vector<FSTableColumn> getTableColumns() {
	// return tableColumns;
	// }

	// /////////////////////////////////////////////////////////////////////////////
	@Override
	public int getRowCount() {
		return this.tableColumns.size() == 0 || this.records == null ? 0 : this.records.size();
	}

	// /////////////////////////////////////////////////////////////////////////////
	public FSTableColumn getTableColumn(final int visibleColumnIndex) {
		return getTableColumn(visibleColumnIndex, true);
	}

	// /////////////////////////////////////////////////////////
	/**
	 * return NULL of col is out of bound
	 */
	public FSTableColumn getTableColumn(final int col, final boolean visibleIndex) {
		int actualIndex;
		if (visibleIndex) {
			actualIndex = this.visibilityManager.getActualIndexFromVisibleIndex(col);
		} else {
			actualIndex = col;
		}
		return this.tableColumns.get(actualIndex);
	}

	// /////////////////////////////////////////////////////////////////////////////
	@Override
	public Object getValueAt(final int row, final int visibleColumnIndex) {
		final int actualIndex = this.visibilityManager.getActualIndexFromVisibleIndex(visibleColumnIndex);
		return getRecords().get(row).getColumnValue(actualIndex);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public double getValueAtAsDouble(final int row, final int col) {
		final Object valueAt = getValueAt(row, col);
		double number = 0;
		if (valueAt != null && !valueAt.toString().equals("")) {
			number = Double.parseDouble(valueAt.toString().trim());
		}
		return number;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public float getValueAtAsFloat(final int row, final int col) {
		final Object valueAt = getValueAt(row, col);
		float number = 0;
		if (valueAt != null && !valueAt.toString().equals("")) {
			number = Float.parseFloat(valueAt.toString().trim());
		}
		return number;
	}

	public int getValueAtAsInteger(final int row, final int col) {
		final Object valueAt = getValueAt(row, col);
		int number = 0;
		if (valueAt != null && !valueAt.toString().equals("")) {
			number = Integer.parseInt(valueAt.toString().trim());
		}
		return number;

	}

	// //////////////////////////////////////////////////////////////////////
	public int getVisibleColumnIndexFromActual(final int actualIndex) {
		return getTableColumn(actualIndex, false).getVisibleIndex();
	}

	// //
	// /////////////////////////////////////////////////////////////////////////////
	// public FSTableColumn getTableColumn(int index, boolean createIfNotExists)
	// {
	// if (index < tableColumns.size()) {
	// return tableColumns.get(index);
	// }
	// if (create) {
	// FSTableColumn col = createTableColumn(index);
	// addFSTableColumn(col);
	// return col;
	// }
	// throw new ArrayIndexOutOfBoundsException(index);
	// }

	// /////////////////////////////////////////////////////////
	public void insertRecord(final int selectedRow) {
		insertRecord(selectedRow, createEmptyRecord());
	}

	// /////////////////////////////////////////////////////////
	public void insertRecord(final int row, final FSTableRecord record) {
		this.records.insertElementAt(record, row);
		fireTableRowsInserted(row, row);
	}

	// /////////////////////////////////////////////////////////
	public boolean isAllDataValid() {
		// Make this method smatert
		for (final FSTableColumn col : this.tableColumns) {
			final int lastRow = getRowCount() - 1;
			if (col.isVisible() && col.isRequired()) {
				final Object colValue = getValueAt(lastRow, col.getIndex());
				if (colValue == null || colValue.toString().equals("")) {
					return false;// dont allow
				}
			}
		}
		return true;
	}

	// //////////////////////////////////////////////////////////////////////
	public boolean isAllowDelete() {
		return this.allowDelete || isEditable();
	}

	// /////////////////////////////////////////////////////////
	public boolean isDataModified() {
		if (this.deletedRecords.size() > 0) {
			return true;
		}
		for (final FSTableRecord rec : this.records) {
			if (rec.getStatus() == RecordStatus.MODIFIED) {
				return true;
			}
		}
		return false;
	}

	// /////////////////////////////////////////////////////////
	public boolean isEditable() {
		// return true if any cell is editable
		for (final FSTableColumn col : this.tableColumns) {
			if (col.isEditable()) {
				return true;
			}
		}
		return false;
	}

	// /////////////////////////////////////////////////////////
	public boolean isEditable(final int column) {
		return getTableColumn(column).isEditable();
	}

	/**
	 *
	 * @param row
	 * @param column
	 * @return
	 */
	public boolean isEditable(final int row, final int column) {
		if (isEditable(column)) {
			final int actualIndex = getTableColumn(column).getIndex();
			final FSTableRecord record = getRecord(row);
			return record.isColumnEnabled(actualIndex);
		}
		return false;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public boolean isNumericClumn(final int visibleColIndex) {
		return getTableColumn(visibleColIndex).isNumeric();
	}

	// /////////////////////////////////////////////////////////
	protected boolean isValidTableColumnIndex(final int actualIndex) {
		return actualIndex >= 0 && actualIndex < this.tableColumns.size();
	}

	// /////////////////////////////////////////////////////////////////////
	public boolean isVisible(final int col) {
		return getTableColumn(col).isVisible();
	}

	public void refreshVisibility() {
		this.visibilityManager.refreshVisibility();
		fireTableStructureChanged();
	}

	// /////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////
	public FSTableRecord removeRecord(final int row) {
		return this.records.remove(row);
	}

	// ///////////////////////////////////////////////////////////////////
	public void resetRecords() {
		this.records.clear();
		// fireTableDataChanged();
	}

	// //////////////////////////////////////////////////////////////////////
	public void setAllowDelete(final boolean allowDelete) {
		this.allowDelete = allowDelete;
	}

	// //////////////////////////////////////////////////////////////////////////////////
	public void setColumnValue(final int row, final int col, final Object value, final boolean visibleIndex) {
		int actualColumn = col;
		if (visibleIndex) {
			actualColumn = getActualColumnIndexFromVisible(col);
		}
		getRecord(row).setColumnValue(actualColumn, value);
		fireTableCellUpdated(row, col);
	}

	// /////////////////////////////////////////////////////////
	public void setEditable(final boolean editable) {
		for (final FSTableColumn col : this.tableColumns) {
			col.setEditable(editable);
		}
	}

	// /////////////////////////////////////////////////////////
	public void setEditable(final int column, final boolean editable) {
		getTableColumn(column).setEditable(editable);
	}

	/**
	 *
	 * @param row
	 * @param col
	 * @param enable
	 */
	public void setEditable(final int row, final int col, final boolean enable) {
		final int actualIndex = getTableColumn(col).getIndex();
		getRecord(row).setColumnEnabled(actualIndex, enable);
	}

	// /////////////////////////////////////////////////////////
	public void setEditor(final int colunm, final TableCellEditor cellEditor) {
		getTableColumn(colunm).setEditor(cellEditor);
	}

	// /////////////////////////////////////////////////////////
	public void setFormatter(final int col, final Format formatter) {
		getTableColumn(col).setFormatter(formatter);
	}

	// /////////////////////////////////////////////////////////
	public void setPreferredWidth(final int col, final int width) {
		getTableColumn(col).setPreferredWidth(width);
	}

	// /////////////////////////////////////////////////////////
	public void setRenderer(final int col, final TableCellRenderer cellRenderer) {
		getTableColumn(col).setRenderer(cellRenderer);
	}

	// /////////////////////////////////////////////////////////
	public void setRequired(final int col, final boolean required) {
		getTableColumn(col).setRequired(required);
	}

	// /////////////////////////////////////////////////////////
	@Override
	public void setValueAt(final Object value, final int rowIndex, final int visibleIndex) {
		final int actualColIndex = this.visibilityManager.getActualIndexFromVisibleIndex(visibleIndex);
		final FSTableRecord record = this.records.get(rowIndex);
		record.setColumnValue(actualColIndex, value);
		record.setStatus(RecordStatus.MODIFIED);
		fireTableCellUpdated(rowIndex, visibleIndex);
		this.modified = true;
	}

	// /////////////////////////////////////////////////////////
	public void setVisible(final int col, final boolean visible) {
		getTableColumn(col).setVisible(visible);
		refreshVisibility();
	}

	// //////////////////////////////////////////////////////////////////////
	public void setVisibleByActualIndex(final int colunmIndex, final boolean visible) {
		getTableColumn(colunmIndex, false).setVisible(visible);
		refreshVisibility();
	}

}
