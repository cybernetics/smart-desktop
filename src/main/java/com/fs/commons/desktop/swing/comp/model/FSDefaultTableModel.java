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

import java.io.Serializable;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * This is an implementation of <code>TableModel</code> that uses a
 * <code>Vector</code> of <code>Vectors</code> to store the cell value objects.
 * <p>
 * <strong>Warning:</strong> <code>DefaultTableModel</code> returns a column
 * class of <code>Object</code>. When <code>DefaultTableModel</code> is used
 * with a <code>TableRowSorter</code> this will result in extensive use of
 * <code>toString</code>, which for non-<code>String</code> data types is
 * expensive. If you use <code>DefaultTableModel</code> with a
 * <code>TableRowSorter</code> you are strongly encouraged to override
 * <code>getColumnClass</code> to return the appropriate type.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @version 1.43 04/07/06
 * @author Philip Milne
 *
 * @see TableModel
 * @see #getDataVector
 */
public class FSDefaultTableModel extends AbstractTableModel implements Serializable {

	//
	// Instance Variables
	//

	/**
	 *
	 */
	private static final long serialVersionUID = -8125197035369796500L;

	/**
	 * Returns a vector that contains the same objects as the array.
	 * 
	 * @param anArray
	 *            the array to be converted
	 * @return the new vector; if <code>anArray</code> is <code>null</code>,
	 *         returns <code>null</code>
	 */
	protected static Vector convertToVector(final Object[] anArray) {
		if (anArray == null) {
			return null;
		}
		final Vector v = new Vector(anArray.length);
		for (final Object element : anArray) {
			v.addElement(element);
		}
		return v;
	}

	/**
	 * Returns a vector of vectors that contains the same objects as the array.
	 * 
	 * @param anArray
	 *            the double array to be converted
	 * @return the new vector of vectors; if <code>anArray</code> is
	 *         <code>null</code>, returns <code>null</code>
	 */
	protected static Vector convertToVector(final Object[][] anArray) {
		if (anArray == null) {
			return null;
		}
		final Vector v = new Vector(anArray.length);
		for (final Object[] element : anArray) {
			v.addElement(convertToVector(element));
		}
		return v;
	}

	//
	// Constructors
	//

	private static int gcd(final int i, final int j) {
		return j == 0 ? i : gcd(j, i % j);
	}

	private static Vector newVector(final int size) {
		final Vector v = new Vector(size);
		v.setSize(size);
		return v;
	}

	private static Vector nonNullVector(final Vector v) {
		return v != null ? v : new Vector();
	}

	private static void rotate(final Vector v, final int a, final int b, final int shift) {
		final int size = b - a;
		final int r = size - shift;
		final int g = gcd(size, r);
		for (int i = 0; i < g; i++) {
			int to = i;
			final Object tmp = v.elementAt(a + to);
			for (int from = (to + r) % size; from != i; from = (to + r) % size) {
				v.setElementAt(v.elementAt(a + from), a + to);
				to = from;
			}
			v.setElementAt(tmp, a + to);
		}
	}

	/**
	 * The <code>Vector</code> of <code>Vectors</code> of <code>Object</code>
	 * values.
	 */
	protected Vector dataVector;

	/** The <code>Vector</code> of column identifiers. */
	protected Vector columnIdentifiers;

	/**
	 * Constructs a default <code>DefaultTableModel</code> which is a table of
	 * zero columns and zero rows.
	 */
	public FSDefaultTableModel() {
		this(0, 0);
	}

	/**
	 * Constructs a <code>DefaultTableModel</code> with <code>rowCount</code>
	 * and <code>columnCount</code> of <code>null</code> object values.
	 *
	 * @param rowCount
	 *            the number of rows the table holds
	 * @param columnCount
	 *            the number of columns the table holds
	 *
	 * @see #setValueAt
	 */
	public FSDefaultTableModel(final int rowCount, final int columnCount) {
		this(newVector(columnCount), rowCount);
	}

	/**
	 * Constructs a <code>DefaultTableModel</code> with as many columns as there
	 * are elements in <code>columnNames</code> and <code>rowCount</code> of
	 * <code>null</code> object values. Each column's name will be taken from
	 * the <code>columnNames</code> array.
	 *
	 * @param columnNames
	 *            <code>array</code> containing the names of the new columns; if
	 *            this is <code>null</code> then the model has no columns
	 * @param rowCount
	 *            the number of rows the table holds
	 * @see #setDataVector
	 * @see #setValueAt
	 */
	public FSDefaultTableModel(final Object[] columnNames, final int rowCount) {
		this(convertToVector(columnNames), rowCount);
	}

	/**
	 * Constructs a <code>DefaultTableModel</code> and initializes the table by
	 * passing <code>data</code> and <code>columnNames</code> to the
	 * <code>setDataVector</code> method. The first index in the
	 * <code>Object[][]</code> array is the row index and the second is the
	 * column index.
	 *
	 * @param data
	 *            the data of the table
	 * @param columnNames
	 *            the names of the columns
	 * @see #getDataVector
	 * @see #setDataVector
	 */
	public FSDefaultTableModel(final Object[][] data, final Object[] columnNames) {
		setDataVector(data, columnNames);
	}

	/**
	 * Constructs a <code>DefaultTableModel</code> with as many columns as there
	 * are elements in <code>columnNames</code> and <code>rowCount</code> of
	 * <code>null</code> object values. Each column's name will be taken from
	 * the <code>columnNames</code> vector.
	 *
	 * @param columnNames
	 *            <code>vector</code> containing the names of the new columns;
	 *            if this is <code>null</code> then the model has no columns
	 * @param rowCount
	 *            the number of rows the table holds
	 * @see #setDataVector
	 * @see #setValueAt
	 */
	public FSDefaultTableModel(final Vector columnNames, final int rowCount) {
		setDataVector(newVector(rowCount), columnNames);
	}

	/**
	 * Constructs a <code>DefaultTableModel</code> and initializes the table by
	 * passing <code>data</code> and <code>columnNames</code> to the
	 * <code>setDataVector</code> method.
	 *
	 * @param data
	 *            the data of the table, a <code>Vector</code> of
	 *            <code>Vector</code>s of <code>Object</code> values
	 * @param columnNames
	 *            <code>vector</code> containing the names of the new columns
	 * @see #getDataVector
	 * @see #setDataVector
	 */
	public FSDefaultTableModel(final Vector data, final Vector columnNames) {
		setDataVector(data, columnNames);
	}

	//
	// Manipulating rows
	//

	/**
	 * Adds a column to the model. The new column will have the identifier
	 * <code>columnName</code>, which may be null. This method will send a
	 * <code>tableChanged</code> notification message to all the listeners. This
	 * method is a cover for <code>addColumn(Object, Vector)</code> which uses
	 * <code>null</code> as the data vector.
	 *
	 * @param columnName
	 *            the identifier of the column being added
	 */
	public void addColumn(final Object columnName) {
		addColumn(columnName, (Vector) null);
	}

	/**
	 * Adds a column to the model. The new column will have the identifier
	 * <code>columnName</code>. <code>columnData</code> is the optional array of
	 * data for the column. If it is <code>null</code> the column is filled with
	 * <code>null</code> values. Otherwise, the new data will be added to model
	 * starting with the first element going to row 0, etc. This method will
	 * send a <code>tableChanged</code> notification message to all the
	 * listeners.
	 *
	 * @see #addColumn(Object, Vector)
	 */
	public void addColumn(final Object columnName, final Object[] columnData) {
		addColumn(columnName, convertToVector(columnData));
	}

	/**
	 * Adds a column to the model. The new column will have the identifier
	 * <code>columnName</code>, which may be null. <code>columnData</code> is
	 * the optional vector of data for the column. If it is <code>null</code>
	 * the column is filled with <code>null</code> values. Otherwise, the new
	 * data will be added to model starting with the first element going to row
	 * 0, etc. This method will send a <code>tableChanged</code> notification
	 * message to all the listeners.
	 *
	 * @param columnName
	 *            the identifier of the column being added
	 * @param columnData
	 *            optional data of the column being added
	 */
	public void addColumn(final Object columnName, final Vector columnData) {
		this.columnIdentifiers.addElement(columnName);
		if (columnData != null) {
			final int columnSize = columnData.size();
			if (columnSize > getRowCount()) {
				this.dataVector.setSize(columnSize);
			}
			justifyRows(0, getRowCount());
			final int newColumn = getColumnCount() - 1;
			for (int i = 0; i < columnSize; i++) {
				final Vector row = (Vector) this.dataVector.elementAt(i);
				row.setElementAt(columnData.elementAt(i), newColumn);
			}
		} else {
			justifyRows(0, getRowCount());
		}

		fireTableStructureChanged();
	}

	/**
	 * Adds a row to the end of the model. The new row will contain
	 * <code>null</code> values unless <code>rowData</code> is specified.
	 * Notification of the row being added will be generated.
	 *
	 * @param rowData
	 *            optional data of the row being added
	 */
	public void addRow(final Object[] rowData) {
		addRow(convertToVector(rowData));
	}

	/**
	 * Adds a row to the end of the model. The new row will contain
	 * <code>null</code> values unless <code>rowData</code> is specified.
	 * Notification of the row being added will be generated.
	 *
	 * @param rowData
	 *            optional data of the row being added
	 */
	public void addRow(final Vector rowData) {
		insertRow(getRowCount(), rowData);
	}

	/**
	 * Returns the number of columns in this data table.
	 * 
	 * @return the number of columns in the model
	 */
	@Override
	public int getColumnCount() {
		return this.columnIdentifiers.size();
	}

	/**
	 * Returns the column name.
	 *
	 * @return a name for this column using the string value of the appropriate
	 *         member in <code>columnIdentifiers</code>. If
	 *         <code>columnIdentifiers</code> does not have an entry for this
	 *         index, returns the default name provided by the superclass.
	 */
	@Override
	public String getColumnName(final int column) {
		Object id = null;
		// This test is to cover the case when
		// getColumnCount has been subclassed by mistake ...
		if (column < this.columnIdentifiers.size() && column >= 0) {
			id = this.columnIdentifiers.elementAt(column);
		}
		return id == null ? super.getColumnName(column) : id.toString();
	}

	/**
	 * Returns the <code>Vector</code> of <code>Vectors</code> that contains the
	 * table's data values. The vectors contained in the outer vector are each a
	 * single row of values. In other words, to get to the cell at row 1, column
	 * 5:
	 * <p>
	 *
	 * <code>((Vector)getDataVector().elementAt(1)).elementAt(5);</code>
	 * <p>
	 *
	 * @return the vector of vectors containing the tables data values
	 *
	 * @see #newDataAvailable
	 * @see #newRowsAdded
	 * @see #setDataVector
	 */
	public Vector getDataVector() {
		return this.dataVector;
	}

	/**
	 * Returns the number of rows in this data table.
	 * 
	 * @return the number of rows in the model
	 */
	@Override
	public int getRowCount() {
		return this.dataVector.size();
	}

	/**
	 * Returns an attribute value for the cell at <code>row</code> and
	 * <code>column</code>.
	 *
	 * @param row
	 *            the row whose value is to be queried
	 * @param column
	 *            the column whose value is to be queried
	 * @return the value Object at the specified cell
	 * @exception ArrayIndexOutOfBoundsException
	 *                if an invalid row or column was given
	 */
	@Override
	public Object getValueAt(final int row, final int column) {
		final Vector rowVector = (Vector) this.dataVector.elementAt(row);
		return rowVector.elementAt(column);
	}

	/**
	 * Inserts a row at <code>row</code> in the model. The new row will contain
	 * <code>null</code> values unless <code>rowData</code> is specified.
	 * Notification of the row being added will be generated.
	 *
	 * @param row
	 *            the row index of the row to be inserted
	 * @param rowData
	 *            optional data of the row being added
	 * @exception ArrayIndexOutOfBoundsException
	 *                if the row was invalid
	 */
	public void insertRow(final int row, final Object[] rowData) {
		insertRow(row, convertToVector(rowData));
	}

	/**
	 * Inserts a row at <code>row</code> in the model. The new row will contain
	 * <code>null</code> values unless <code>rowData</code> is specified.
	 * Notification of the row being added will be generated.
	 *
	 * @param row
	 *            the row index of the row to be inserted
	 * @param rowData
	 *            optional data of the row being added
	 * @exception ArrayIndexOutOfBoundsException
	 *                if the row was invalid
	 */
	public void insertRow(final int row, final Vector rowData) {
		this.dataVector.insertElementAt(rowData, row);
		justifyRows(row, row + 1);
		fireTableRowsInserted(row, row);
	}

	/**
	 * Returns true regardless of parameter values.
	 *
	 * @param row
	 *            the row whose value is to be queried
	 * @param column
	 *            the column whose value is to be queried
	 * @return true
	 * @see #setValueAt
	 */
	@Override
	public boolean isCellEditable(final int row, final int column) {
		return true;
	}

	//
	// Manipulating columns
	//

	private void justifyRows(final int from, final int to) {
		// Sometimes the DefaultTableModel is subclassed
		// instead of the AbstractTableModel by mistake.
		// Set the number of rows for the case when getRowCount
		// is overridden.
		this.dataVector.setSize(getRowCount());

		for (int i = from; i < to; i++) {
			if (this.dataVector.elementAt(i) == null) {
				this.dataVector.setElementAt(new Vector(), i);
			}
			// ((Vector)dataVector.elementAt(i)).setSize(getColumnCount());
		}
	}

	/**
	 * Moves one or more rows from the inclusive range <code>start</code> to
	 * <code>end</code> to the <code>to</code> position in the model. After the
	 * move, the row that was at index <code>start</code> will be at index
	 * <code>to</code>. This method will send a <code>tableChanged</code>
	 * notification message to all the listeners.
	 * <p>
	 *
	 * <pre>
	 *  Examples of moves:
	 *  <p>
	 *  1. moveRow(1,3,5);
	 *          a|B|C|D|e|f|g|h|i|j|k   - before
	 *          a|e|f|g|h|B|C|D|i|j|k   - after
	 *  <p>
	 *  2. moveRow(6,7,1);
	 *          a|b|c|d|e|f|G|H|i|j|k   - before
	 *          a|G|H|b|c|d|e|f|i|j|k   - after
	 *  <p>
	 * </pre>
	 *
	 * @param start
	 *            the starting row index to be moved
	 * @param end
	 *            the ending row index to be moved
	 * @param to
	 *            the destination of the rows to be moved
	 * @exception ArrayIndexOutOfBoundsException
	 *                if any of the elements would be moved out of the table's
	 *                range
	 * 
	 */
	public void moveRow(final int start, final int end, final int to) {
		final int shift = to - start;
		int first, last;
		if (shift < 0) {
			first = to;
			last = end;
		} else {
			first = start;
			last = to + end - start;
		}
		rotate(this.dataVector, first, last + 1, shift);

		fireTableRowsUpdated(first, last);
	}

	/**
	 * Equivalent to <code>fireTableChanged</code>.
	 *
	 * @param event
	 *            the change event
	 *
	 */
	public void newDataAvailable(final TableModelEvent event) {
		fireTableChanged(event);
	}

	/**
	 * Ensures that the new rows have the correct number of columns. This is
	 * accomplished by using the <code>setSize</code> method in
	 * <code>Vector</code> which truncates vectors which are too long, and
	 * appends <code>null</code>s if they are too short. This method also sends
	 * out a <code>tableChanged</code> notification message to all the
	 * listeners.
	 *
	 * @param e
	 *            this <code>TableModelEvent</code> describes where the rows
	 *            were added. If <code>null</code> it assumes all the rows were
	 *            newly added
	 * @see #getDataVector
	 */
	public void newRowsAdded(final TableModelEvent e) {
		justifyRows(e.getFirstRow(), e.getLastRow() + 1);
		fireTableChanged(e);
	}

	/**
	 * Removes the row at <code>row</code> from the model. Notification of the
	 * row being removed will be sent to all the listeners.
	 *
	 * @param row
	 *            the row index of the row to be removed
	 * @return
	 * @exception ArrayIndexOutOfBoundsException
	 *                if the row was invalid
	 */
	public Object removeRow(final int row) {
		final Object removed = this.dataVector.remove(row);
		fireTableRowsDeleted(row, row);
		return removed;
	}

	/**
	 * Equivalent to <code>fireTableChanged</code>.
	 *
	 * @param event
	 *            the change event
	 *
	 */
	public void rowsRemoved(final TableModelEvent event) {
		fireTableChanged(event);
	}

	//
	// Implementing the TableModel interface
	//

	/**
	 * Sets the number of columns in the model. If the new size is greater than
	 * the current size, new columns are added to the end of the model with
	 * <code>null</code> cell values. If the new size is less than the current
	 * size, all columns at index <code>columnCount</code> and greater are
	 * discarded.
	 *
	 * @param columnCount
	 *            the new number of columns in the model
	 *
	 * @see #setColumnCount
	 * @since 1.3
	 */
	public void setColumnCount(final int columnCount) {
		this.columnIdentifiers.setSize(columnCount);
		justifyRows(0, getRowCount());
		fireTableStructureChanged();
	}

	/**
	 * Replaces the column identifiers in the model. If the number of
	 * <code>newIdentifier</code>s is greater than the current number of
	 * columns, new columns are added to the end of each row in the model. If
	 * the number of <code>newIdentifier</code>s is less than the current number
	 * of columns, all the extra columns at the end of a row are discarded.
	 * <p>
	 *
	 * @param newIdentifiers
	 *            array of column identifiers. If <code>null</code>, set the
	 *            model to zero columns
	 * @see #setNumRows
	 */
	public void setColumnIdentifiers(final Object[] newIdentifiers) {
		setColumnIdentifiers(convertToVector(newIdentifiers));
	}

	/**
	 * Replaces the column identifiers in the model. If the number of
	 * <code>newIdentifier</code>s is greater than the current number of
	 * columns, new columns are added to the end of each row in the model. If
	 * the number of <code>newIdentifier</code>s is less than the current number
	 * of columns, all the extra columns at the end of a row are discarded.
	 * <p>
	 *
	 * @param columnIdentifiers
	 *            vector of column identifiers. If <code>null</code>, set the
	 *            model to zero columns
	 * @see #setNumRows
	 */
	public void setColumnIdentifiers(final Vector columnIdentifiers) {
		setDataVector(this.dataVector, columnIdentifiers);
	}

	/**
	 * Replaces the value in the <code>dataVector</code> instance variable with
	 * the values in the array <code>dataVector</code>. The first index in the
	 * <code>Object[][]</code> array is the row index and the second is the
	 * column index. <code>columnIdentifiers</code> are the names of the new
	 * columns.
	 *
	 * @param dataVector
	 *            the new data vector
	 * @param columnIdentifiers
	 *            the names of the columns
	 * @see #setDataVector(Vector, Vector)
	 */
	public void setDataVector(final Object[][] dataVector, final Object[] columnIdentifiers) {
		setDataVector(convertToVector(dataVector), convertToVector(columnIdentifiers));
	}

	/**
	 * Replaces the current <code>dataVector</code> instance variable with the
	 * new <code>Vector</code> of rows, <code>dataVector</code>. Each row is
	 * represented in <code>dataVector</code> as a <code>Vector</code> of
	 * <code>Object</code> values. <code>columnIdentifiers</code> are the names
	 * of the new columns. The first name in <code>columnIdentifiers</code> is
	 * mapped to column 0 in <code>dataVector</code>. Each row in
	 * <code>dataVector</code> is adjusted to match the number of columns in
	 * <code>columnIdentifiers</code> either by truncating the
	 * <code>Vector</code> if it is too long, or adding <code>null</code> values
	 * if it is too short.
	 * <p>
	 * Note that passing in a <code>null</code> value for
	 * <code>dataVector</code> results in unspecified behavior, an possibly an
	 * exception.
	 *
	 * @param dataVector
	 *            the new data vector
	 * @param columnIdentifiers
	 *            the names of the columns
	 * @see #getDataVector
	 */
	public void setDataVector(final Vector dataVector, final Vector columnIdentifiers) {
		this.dataVector = nonNullVector(dataVector);
		this.columnIdentifiers = nonNullVector(columnIdentifiers);
		justifyRows(0, getRowCount());
		fireTableStructureChanged();
	}

	/**
	 * Obsolete as of Java 2 platform v1.3. Please use <code>setRowCount</code>
	 * instead.
	 */
	/*
	 * Sets the number of rows in the model. If the new size is greater than the
	 * current size, new rows are added to the end of the model If the new size
	 * is less than the current size, all rows at index <code>rowCount</code>
	 * and greater are discarded. <p>
	 *
	 * @param rowCount the new number of rows
	 * 
	 * @see #setRowCount
	 */
	public void setNumRows(final int rowCount) {
		final int old = getRowCount();
		if (old == rowCount) {
			return;
		}
		this.dataVector.setSize(rowCount);
		if (rowCount <= old) {
			fireTableRowsDeleted(rowCount, old - 1);
		} else {
			justifyRows(old, rowCount);
			fireTableRowsInserted(old, rowCount - 1);
		}
	}

	//
	// Protected Methods
	//

	/**
	 * Sets the number of rows in the model. If the new size is greater than the
	 * current size, new rows are added to the end of the model If the new size
	 * is less than the current size, all rows at index <code>rowCount</code>
	 * and greater are discarded.
	 * <p>
	 *
	 * @see #setColumnCount
	 * @since 1.3
	 */
	public void setRowCount(final int rowCount) {
		setNumRows(rowCount);
	}

	/**
	 * Sets the object value for the cell at <code>column</code> and
	 * <code>row</code>. <code>aValue</code> is the new value. This method will
	 * generate a <code>tableChanged</code> notification.
	 *
	 * @param aValue
	 *            the new value; this can be null
	 * @param row
	 *            the row whose value is to be changed
	 * @param column
	 *            the column whose value is to be changed
	 * @exception ArrayIndexOutOfBoundsException
	 *                if an invalid row or column was given
	 */
	@Override
	public void setValueAt(final Object aValue, final int row, final int column) {
		final Vector rowVector = (Vector) this.dataVector.elementAt(row);
		rowVector.setElementAt(aValue, column);
		fireTableCellUpdated(row, column);
	}

} // End of class DefaultTableModel
