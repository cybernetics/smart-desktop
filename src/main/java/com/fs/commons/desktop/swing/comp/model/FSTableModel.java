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
	private final Vector<FSTableColumn> tableColumns = new Vector<FSTableColumn>();

	private final Vector<FSTableRecord> records = new Vector<FSTableRecord>();
	private final ColumnVisiblityManagar visibilityManager = new ColumnVisiblityManagar(tableColumns);
	private final Vector<FSTableRecord> deletedRecords = new Vector<FSTableRecord>();
	boolean modified;
	boolean allowDelete;

	// /////////////////////////////////////////////////////////////////////////
	public FSTableModel() {
	}

	// /////////////////////////////////////////////////////////////////////////
	public int getIntegerColunmSum(int col) {
		return (int) getColunmSum(col);
	}

	// /////////////////////////////////////////////////////////////////////////
	public double getColunmSum(int col) {
		double sum = 0;
		for (int i = 0; i < getRowCount(); i++) {
			try {
				double number = getValueAtAsDouble(i, col);
				sum = FinancialUtility.addAmounts(sum, number);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return sum;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public double getValueAtAsDouble(int row, int col) {
		Object valueAt = getValueAt(row, col);
		double number = 0;
		if (valueAt != null && !valueAt.toString().equals("")) {
			number = Double.parseDouble(valueAt.toString().trim());
		}
		return number;
	}

	public int getValueAtAsInteger(int row, int col) {
		Object valueAt = getValueAt(row, col);
		int number = 0;
		if (valueAt != null && !valueAt.toString().equals("")) {
			number = Integer.parseInt(valueAt.toString().trim());
		}
		return number;

	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public float getValueAtAsFloat(int row, int col) {
		Object valueAt = getValueAt(row, col);
		float number = 0;
		if (valueAt != null && !valueAt.toString().equals("")) {
			number = Float.parseFloat(valueAt.toString().trim());
		}
		return number;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public boolean isNumericClumn(int visibleColIndex) {
		return getTableColumn(visibleColIndex).isNumeric();
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public FSTableRecord addRecord() {
		FSTableRecord record = createEmptyRecord();
		addRecord(record);
		return record;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public void addRecord(FSTableRecord record) {
		geteRecords().add(record);
		fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////

	private Vector<FSTableRecord> geteRecords() {
		return records;
	}

	// /////////////////////////////////////////////////////////
	protected FSTableRecord createEmptyRecord() {
		FSTableRecord record = new FSTableRecord();
		record.addEmptyValues(tableColumns);
		record.setStatus(RecordStatus.NEW);
		return record;
	}

	// /////////////////////////////////////////////////////////
	public FSTableRecord deleteRow(int selectedRow) {
		FSTableRecord removed = removeRecord(selectedRow);
		removed.setStatus(RecordStatus.DELETED);
		// Object removed = getDataVector().remove(selectedRow);
		if (removed != null) {
			deletedRecords.add(removed);
		}
		fireTableRowsDeleted(selectedRow, selectedRow);
		return removed;
	}

	// /////////////////////////////////////////////////////////
	public FSTableRecord removeRecord(int row) {
		return records.remove(row);
	}

	// /////////////////////////////////////////////////////////
	public Vector<FSTableRecord> getDeletedRecords() {
		return deletedRecords;
	}

	// /////////////////////////////////////////////////////////
	public void insertRecord(int selectedRow) {
		insertRecord(selectedRow, createEmptyRecord());
	}

	// /////////////////////////////////////////////////////////
	public void insertRecord(int row, FSTableRecord record) {
		records.insertElementAt(record, row);
		fireTableRowsInserted(row, row);
	}

	// /////////////////////////////////////////////////////////
	public void setRequired(int col, boolean required) {
		getTableColumn(col).setRequired(required);
	}

	// // /////////////////////////////////////////////////////////
	// public FSTableColumn getTableColumn(int col) {
	// return getTableColumn(col, true);
	// }

	// /////////////////////////////////////////////////////////
	/**
	 * return NULL of col is out of bound
	 */
	public FSTableColumn getTableColumn(int col, boolean visibleIndex) {
		int actualIndex;
		if (visibleIndex) {
			actualIndex = visibilityManager.getActualIndexFromVisibleIndex(col);
		} else {
			actualIndex = col;
		}
		return tableColumns.get(actualIndex);
	}

	// /////////////////////////////////////////////////////////
	protected boolean isValidTableColumnIndex(int actualIndex) {
		return actualIndex >= 0 && actualIndex < tableColumns.size();
	}

	// /////////////////////////////////////////////////////////
	public int getPrefferedWidth(int column) {
		return getTableColumn(column).getPreferredWidth();
	}

	// /////////////////////////////////////////////////////////
	public boolean isEditable() {
		// return true if any cell is editable
		for (FSTableColumn col : tableColumns) {
			if (col.isEditable()) {
				return true;
			}
		}
		return false;
	}

	// /////////////////////////////////////////////////////////
	public boolean isEditable(int column) {
		return getTableColumn(column).isEditable();
	}

	// /////////////////////////////////////////////////////////
	public void setVisible(int col, boolean visible) {
		getTableColumn(col).setVisible(visible);
		refreshVisibility();
	}

	// /////////////////////////////////////////////////////////
	public TableCellRenderer getCellRenderer(int column) {
		return getTableColumn(column).getRenderer();
	}

	// /////////////////////////////////////////////////////////
	public TableCellEditor getCellEditor(int column) {
		return getTableColumn(column).getEditor();
	}

	// /////////////////////////////////////////////////////////
	public void setPreferredWidth(int col, int width) {
		getTableColumn(col).setPreferredWidth(width);
	}

	// /////////////////////////////////////////////////////////
	public void setFormatter(int col, Format formatter) {
		getTableColumn(col).setFormatter(formatter);
	}

	// /////////////////////////////////////////////////////////
	public Format getFormatter(int col) {
		return getTableColumn(col).getFormatter();
	}

	// /////////////////////////////////////////////////////////
	public void setEditable(boolean editable) {
		for (FSTableColumn col : tableColumns) {
			col.setEditable(editable);
		}
	}

	// /////////////////////////////////////////////////////////
	public void setEditable(int column, boolean editable) {
		getTableColumn(column).setEditable(editable);
	}

	// /////////////////////////////////////////////////////////
	public void setRenderer(int col, TableCellRenderer cellRenderer) {
		getTableColumn(col).setRenderer(cellRenderer);
	}

	// /////////////////////////////////////////////////////////
	public void setEditor(int colunm, TableCellEditor cellEditor) {
		getTableColumn(colunm).setEditor(cellEditor);
	}

	// /////////////////////////////////////////////////////////
	public boolean isAllDataValid() {
		// Make this method smatert
		for (FSTableColumn col : tableColumns) {
			int lastRow = getRowCount() - 1;
			if (col.isVisible() && col.isRequired()) {
				Object colValue = getValueAt(lastRow, col.getIndex());
				if (colValue == null || colValue.toString().equals("")) {
					return false;// dont allow
				}
			}
		}
		return true;
	}

	// // /////////////////////////////////////////////////////////
	// public Vector<FSTableColumn> getTableColumns() {
	// return tableColumns;
	// }

	// /////////////////////////////////////////////////////////
	@Override
	public void setValueAt(Object value, int rowIndex, int visibleIndex) {
		int actualColIndex = visibilityManager.getActualIndexFromVisibleIndex(visibleIndex);
		FSTableRecord record = records.get(rowIndex);
		record.setColumnValue(actualColIndex, value);
		record.setStatus(RecordStatus.MODIFIED);
		fireTableCellUpdated(rowIndex, visibleIndex);
		modified = true;
	}

	// /////////////////////////////////////////////////////////
	public boolean isDataModified() {
		if (deletedRecords.size() > 0) {
			return true;
		}
		for (FSTableRecord rec : records) {
			if (rec.getStatus() == RecordStatus.MODIFIED) {
				return true;
			}
		}
		return false;
	}

	// ///////////////////////////////////////////////////////////////////
	public Vector<FSTableRecord> getRecords() {
		return records;
	}
	
	public Vector<Record> getDaoRecords() {
		//TODO
		return null;
	}

	// ///////////////////////////////////////////////////////////////////
	public Vector<Vector> getRecordsAsDataVector() {
		Vector<Vector> data = new Vector<Vector>();
		for (FSTableRecord rec : records) {
			data.add(rec.toValuesVector());
		}
		return data;
	}

	// ///////////////////////////////////////////////////////////////////
	public Vector<Vector> getDeletedRecordsAsDataVector() {
		Vector<Vector> data = new Vector<Vector>();
		for (FSTableRecord rec : deletedRecords) {
			data.add(rec.toValuesVector());
		}
		return data;
	}

	// ///////////////////////////////////////////////////////////////////
	public void resetRecords() {
		records.clear();
		// fireTableDataChanged();
	}

	// ///////////////////////////////////////////////////////////////////
	public void addFSTableColumn(FSTableColumn col) {
		col.setIndex(tableColumns.size());
		tableColumns.add(col);
		visibilityManager.refreshVisibility();
		fireTableStructureChanged();
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

	// /////////////////////////////////////////////////////////////////////////////
	public FSTableRecord getRecord(int row) {
		if (row >= getRowCount()) {
			throw new IllegalStateException("Row : " + row + " is out of index");
		}
		return records.get(row);
	}

	// /////////////////////////////////////////////////////////////////////////////
	public int getColunmIndexByName(String colName) {
		for (FSTableColumn col : tableColumns) {
			if (col.getName().equalsIgnoreCase(colName)) {
				return col.getVisibleIndex();
			}
		}
		return -1;
	}

	// /////////////////////////////////////////////////////////////////////////////
	public FSTableColumn getTableColumn(int visibleColumnIndex) {
		return getTableColumn(visibleColumnIndex, true);
	}

	// /////////////////////////////////////////////////////////////////////////////
	@Override
	public int getRowCount() {
		return tableColumns.size() == 0 || records == null ? 0 : records.size();
	}

	// /////////////////////////////////////////////////////////////////////////////
	@Override
	public Object getValueAt(int row, int visibleColumnIndex) {
		int actualIndex = visibilityManager.getActualIndexFromVisibleIndex(visibleColumnIndex);
		return getRecords().get(row).getColumnValue(actualIndex);
	}

	// ///////////////////////////////////////////////////////////////////////////////
	@Override
	public Class getColumnClass(int columnIndex) {
		return getTableColumn(columnIndex).getColumnClass();
		// try {
		// String columnClassName =
		// getTableColumn(columnIndex).getColumnClassName();
		// //
		// System.out.println("Coluinm name : "+getColumnName(columnIndex)+" class = "+
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

	// /////////////////////////////////////////////////////////////////////
	@Override
	public int getColumnCount() {
		return visibilityManager.getVisibleColumnCount();
	}

	// /////////////////////////////////////////////////////////////////////
	public int getColunmIndex(String name) {
		for (int i = 0; i < getColumnCount(); i++) {
			if (getActualColumnName(i).trim().equalsIgnoreCase(name)) {
				return i;
			}
		}
		return -1;
	}

	// /////////////////////////////////////////////////////////////////////
	public String getActualColumnName(int index) {
		return getTableColumn(index).getName();
	}

	// /////////////////////////////////////////////////////////////////////
	public boolean isVisible(int col) {
		return getTableColumn(col).isVisible();
	}

	// /////////////////////////////////////////////////////////////////////
	public int getColumnType(int col) {
		return getTableColumn(col).getColumnType();
	}

	public int getActualColumnCount() {
		return tableColumns.size();
	}

	// /////////////////////////////////////////////////////////////////////

	@Override
	public String getColumnName(int visibleColumnIndex) {
//		FSTableColumn tableColumn = getTableColumn(visibleColumnIndex);
//		return tableColumn.getHumanName()+"-"+tableColumn.getVisibleIndex()+'-'+tableColumn.getIndex();
		return getTableColumn(visibleColumnIndex).getHumanName();
	}

	public void refreshVisibility() {
		visibilityManager.refreshVisibility();
		fireTableStructureChanged();
	}

	// //////////////////////////////////////////////////////////////////////
	public void setVisibleByActualIndex(int colunmIndex, boolean visible) {
		getTableColumn(colunmIndex, false).setVisible(visible);
		refreshVisibility();
	}

	// //////////////////////////////////////////////////////////////////////
	public void deleteRows(int[] rows) {
		for (int i = rows.length - 1; i >= 0; i--) {
			deleteRow(rows[i]);
		}
	}

	// //////////////////////////////////////////////////////////////////////
	public boolean isAllowDelete() {
		return allowDelete || isEditable();
	}

	// //////////////////////////////////////////////////////////////////////
	public void setAllowDelete(boolean allowDelete) {
		this.allowDelete = allowDelete;
	}

	// //////////////////////////////////////////////////////////////////////
	public void clearRecords() {
		records.clear();
		fireTableDataChanged();
	}

	// //////////////////////////////////////////////////////////////////////
	public int getVisibleColumnIndexFromActual(int actualIndex) {
		return getTableColumn(actualIndex, false).getVisibleIndex();
	}

	public int getActualColumnIndexFromVisible(int visibleIndex) {
		return getTableColumn(visibleIndex, true).getIndex();
	}

	/**
	 * 
	 * @author jalal
	 * 
	 */
	class ColumnVisiblityManagar {
		final Vector<FSTableColumn> columns;

		public int getVisibleColumnCount() {
			int count = 0;
			for (FSTableColumn col : columns) {
				if (col.isVisible()) {
					count++;
				}
			}
			return count;
		}

		// ///////////////////////////////////////////////////////////
		public ColumnVisiblityManagar(Vector<FSTableColumn> columns) {
			this.columns = columns;
			refreshVisibility();
		}

		// ///////////////////////////////////////////////////////////
		protected void refreshVisibility() {
			int visibleIndex = 0;
			for (FSTableColumn col : columns) {
				if (col.isVisible()) {
					col.setVisibleIndex(visibleIndex++);
				} else {
					col.setVisibleIndex(-1);
				}
			}
		}

		// ///////////////////////////////////////////////////////////
		public Vector<FSTableColumn> getColumns() {
			return columns;
		}

		// ///////////////////////////////////////////////////////////
		public int getVisibleIndexFromActualIndex(int actualIndex) {
			return columns.get(actualIndex).getVisibleIndex();
		}

		// ///////////////////////////////////////////////////////////
		public int getActualIndexFromVisibleIndex(int visibleIndex) {
			return getFSTableColumnFromVisibleIndex(visibleIndex).getIndex();
		}

		// ///////////////////////////////////////////////////////////
		public FSTableColumn getFSTableColumnFromVisibleIndex(int visibleIndex) {
			for (FSTableColumn col : columns) {
				if (col.getVisibleIndex() == visibleIndex) {
					return col;
				}
			}
			throw new ArrayIndexOutOfBoundsException(visibleIndex);
		}
		// ///////////////////////////////////////////////////////////

	}

	public void fireTableColumnDataChanged(int col) {
		for (int i = 0; i < getRowCount(); i++) {
			fireTableCellUpdated(i, col);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////
	public void setColumnValue(int row, int col, Object value, boolean visibleIndex) {
		int actualColumn = col;
		if (visibleIndex) {
			actualColumn = getActualColumnIndexFromVisible(col);
		}
		getRecord(row).setColumnValue(actualColumn, value);
		fireTableCellUpdated(row, col);
	}

	/**
	 * 
	 * @param row
	 * @param col
	 * @param enable
	 */
	public void setEditable(int row, int col, boolean enable) {
		int actualIndex = getTableColumn(col).getIndex();
		getRecord(row).setColumnEnabled(actualIndex, enable);
	}

	/**
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public boolean isEditable(int row, int column) {
		if(isEditable(column)){
			int actualIndex = getTableColumn(column).getIndex();
			FSTableRecord record = getRecord(row);
			return 	record.isColumnEnabled(actualIndex);
		}
		return false;
	}

	public ArrayList<Record> getAllRecords() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
