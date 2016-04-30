package com.fs.commons.desktop.swing.comp.model;

import java.math.BigDecimal;
import java.text.Format;
import java.util.Vector;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.fs.commons.locale.Lables;

public class FSTableColumn {
	// actual index
	int index;
	// visible index since mst likely there will be hidden fields
	int visibleIndex;
	String name;
	boolean required;
	boolean editable;
	private String humanName;
	private Format formatter;
	private int preferredWidth;
	private TableCellRenderer cellRenderer;
	private TableCellEditor cellEditor;
	private boolean visible = true;
	private Class columnClass;
	private String columnClassName;
	private int columnType;
	private String columnTypeName;
	Vector<FSColumnFilter> filters;
	private int maxLength;
	private Object defaultValue;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		setHumanName(Lables.get(name, true));
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isEditable() {
		return editable && isVisible();
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public void setHumanName(String humanName) {
		this.humanName = humanName;
	}

	public String getHumanName() {
		return humanName;
	}

	public Format getFormatter() {
		return formatter;
	}

	public void setFormatter(Format formatter) {
		this.formatter = formatter;
	}

	public void setPreferredWidth(int preferredWidth) {
		this.preferredWidth = preferredWidth;
	}

	public void setRenderer(TableCellRenderer cellRenderer) {
		this.cellRenderer = cellRenderer;
	}

	public TableCellRenderer getRenderer() {
		return cellRenderer;
	}

	public void setEditor(TableCellEditor cellEditor) {
		this.cellEditor = cellEditor;
	}

	public TableCellEditor getEditor() {
		return cellEditor;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Class getColumnClass() {
		return columnClass == null ? Object.class : columnClass;
	}

	public void setColumnClass(Class columnClass) {
		this.columnClass = columnClass;
	}

	public void setColumnClassName(String columnClassName) throws ClassNotFoundException {
		if (columnClassName.equals("byte[]")) {
			setColumnClass(Object.class);
		} else {
			setColumnClass(Class.forName(columnClassName));
		}
		this.columnClassName = columnClassName;
	}

	public String getColumnClassName() {
		return columnClassName;
	}

	public int getColumnType() {
		// Handle this on appropriate way
		return columnType;
	}

	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}

	public String getColumnTypeName() {
		return columnTypeName;
	}

	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getVisibleIndex() {
		return isVisible() ? visibleIndex : -1;
	}

	public void setVisibleIndex(int visibleIndex) {
		this.visibleIndex = visibleIndex;
	}

	public boolean isNumeric() {
		Class c = getColumnClass();
		return c.equals(Integer.class) || c.equals(Float.class) || c.equals(Long.class) || c.equals(BigDecimal.class);
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public int getPreferredWidth() {
		return preferredWidth;
	}

}