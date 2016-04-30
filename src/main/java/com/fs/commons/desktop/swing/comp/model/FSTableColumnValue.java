package com.fs.commons.desktop.swing.comp.model;

public class FSTableColumnValue<T> {
	FSTableColumn tableColumn;
	T value;
	private boolean enabled=true;

	public FSTableColumnValue() {
	}

	public FSTableColumnValue(FSTableColumn tableColumn) {
		this.tableColumn = tableColumn;
	}

	public FSTableColumn getTableColumn() {
		return tableColumn;
	}

	public void setTableColumn(FSTableColumn tableColumn) {
		this.tableColumn = tableColumn;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	

}
