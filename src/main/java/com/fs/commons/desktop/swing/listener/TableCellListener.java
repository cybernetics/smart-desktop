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
package com.fs.commons.desktop.swing.listener;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

/*
 *  This class listens for changes made to the data in the table via the
 *  TableCellEditor. When editing is started, the value of the cell is saved
 *  When editing is stopped the new value is saved. When the oold and new
 *  values are different, then the provided Action is invoked.
 *
 *  The source of the Action is a TableCellListener instance.
 */
public class TableCellListener implements PropertyChangeListener, Runnable {
	private final JTable table;
	private Action action;

	private int row;
	private int column;
	private Object oldValue;
	private Object newValue;

	/**
	 * Create a TableCellListener.
	 *
	 * @param table
	 *            the table to be monitored for data changes
	 * @param action
	 *            the Action to invoke when cell data is changed
	 */
	public TableCellListener(final JTable table, final Action action) {
		this.table = table;
		this.action = action;
		this.table.addPropertyChangeListener(this);
	}

	/**
	 * Create a TableCellListener with a copy of all the data relevant to the
	 * change of data for a given cell.
	 *
	 * @param row
	 *            the row of the changed cell
	 * @param column
	 *            the column of the changed cell
	 * @param oldValue
	 *            the old data of the changed cell
	 * @param newValue
	 *            the new data of the changed cell
	 */
	private TableCellListener(final JTable table, final int row, final int column, final Object oldValue, final Object newValue) {
		this.table = table;
		this.row = row;
		this.column = column;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * Get the column that was last edited
	 *
	 * @return the column that was edited
	 */
	public int getColumn() {
		return this.column;
	}

	/**
	 * Get the new value in the cell
	 *
	 * @return the new value in the cell
	 */
	public Object getNewValue() {
		return this.newValue;
	}

	/**
	 * Get the old value of the cell
	 *
	 * @return the old value of the cell
	 */
	public Object getOldValue() {
		return this.oldValue;
	}

	/**
	 * Get the row that was last edited
	 *
	 * @return the row that was edited
	 */
	public int getRow() {
		return this.row;
	}

	/**
	 * Get the table of the cell that was changed
	 *
	 * @return the table of the cell that was changed
	 */
	public JTable getTable() {
		return this.table;
	}

	/*
	 * Save information of the cell about to be edited
	 */
	private void processEditingStarted() {
		// The invokeLater is necessary because the editing row and editing
		// column of the table have not been set when the "tableCellEditor"
		// PropertyChangeEvent is fired.
		// This results in the "run" method being invoked

		SwingUtilities.invokeLater(this);
	}

	/*
	 * Update the Cell history when necessary
	 */
	private void processEditingStopped() {
		this.newValue = this.table.getModel().getValueAt(this.row, this.column);

		// The data has changed, invoke the supplied Action

		if (this.newValue != null && !this.newValue.equals(this.oldValue)) {
			// Make a copy of the data in case another cell starts editing
			// while processing this change

			final TableCellListener tcl = new TableCellListener(getTable(), getRow(), getColumn(), getOldValue(), getNewValue());

			final ActionEvent event = new ActionEvent(tcl, ActionEvent.ACTION_PERFORMED, "");
			this.action.actionPerformed(event);
		}
	}

	//
	// Implement the PropertyChangeListener interface
	//
	@Override
	public void propertyChange(final PropertyChangeEvent e) {
		// A cell has started/stopped editing

		if ("tableCellEditor".equals(e.getPropertyName())) {
			if (this.table.isEditing()) {
				processEditingStarted();
			} else {
				processEditingStopped();
			}
		}
	}

	/*
	 * See above.
	 */
	@Override
	public void run() {
		this.row = this.table.convertRowIndexToModel(this.table.getEditingRow());
		this.column = this.table.convertColumnIndexToModel(this.table.getEditingColumn());
		this.oldValue = this.table.getModel().getValueAt(this.row, this.column);
		this.newValue = null;
	}
}
