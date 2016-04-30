package com.fs.commons.desktop.swing.comp.listeners;

import javax.swing.JTable;

public class CellFocusEvent {
	JTable table;
	int row;
	int col;

	public CellFocusEvent(JTable table, int row, int col) {
		setTable(table);
		setRow(row);
		setCol(col);
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
	@Override
	public String toString() {
		return getRow()+","+getCol();
	}

}
