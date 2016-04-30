package com.fs.commons.desktop.swing;

public interface RowData {
	public Object get(int columnIndex);

	public void set(int column, Object value);

	public int size();

}
