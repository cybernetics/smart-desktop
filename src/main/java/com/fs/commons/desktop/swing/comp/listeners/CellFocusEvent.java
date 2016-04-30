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
package com.fs.commons.desktop.swing.comp.listeners;

import javax.swing.JTable;

public class CellFocusEvent {
	JTable table;
	int row;
	int col;

	public CellFocusEvent(final JTable table, final int row, final int col) {
		setTable(table);
		setRow(row);
		setCol(col);
	}

	public int getCol() {
		return this.col;
	}

	public int getRow() {
		return this.row;
	}

	public JTable getTable() {
		return this.table;
	}

	public void setCol(final int col) {
		this.col = col;
	}

	public void setRow(final int row) {
		this.row = row;
	}

	public void setTable(final JTable table) {
		this.table = table;
	}

	@Override
	public String toString() {
		return getRow() + "," + getCol();
	}

}
