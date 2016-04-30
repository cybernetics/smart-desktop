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

	public Class getColumnClass() {
		return this.columnClass == null ? Object.class : this.columnClass;
	}

	public String getColumnClassName() {
		return this.columnClassName;
	}

	public int getColumnType() {
		// Handle this on appropriate way
		return this.columnType;
	}

	public String getColumnTypeName() {
		return this.columnTypeName;
	}

	public TableCellEditor getEditor() {
		return this.cellEditor;
	}

	public Format getFormatter() {
		return this.formatter;
	}

	public String getHumanName() {
		return this.humanName;
	}

	public int getIndex() {
		return this.index;
	}

	public int getMaxLength() {
		return this.maxLength;
	}

	public String getName() {
		return this.name;
	}

	public int getPreferredWidth() {
		return this.preferredWidth;
	}

	public TableCellRenderer getRenderer() {
		return this.cellRenderer;
	}

	public int getVisibleIndex() {
		return isVisible() ? this.visibleIndex : -1;
	}

	public boolean isEditable() {
		return this.editable && isVisible();
	}

	public boolean isNumeric() {
		final Class c = getColumnClass();
		return c.equals(Integer.class) || c.equals(Float.class) || c.equals(Long.class) || c.equals(BigDecimal.class);
	}

	public boolean isRequired() {
		return this.required;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void setColumnClass(final Class columnClass) {
		this.columnClass = columnClass;
	}

	public void setColumnClassName(final String columnClassName) throws ClassNotFoundException {
		if (columnClassName.equals("byte[]")) {
			setColumnClass(Object.class);
		} else {
			setColumnClass(Class.forName(columnClassName));
		}
		this.columnClassName = columnClassName;
	}

	public void setColumnType(final int columnType) {
		this.columnType = columnType;
	}

	public void setColumnTypeName(final String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}

	public void setDefaultValue(final Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setEditable(final boolean editable) {
		this.editable = editable;
	}

	public void setEditor(final TableCellEditor cellEditor) {
		this.cellEditor = cellEditor;
	}

	public void setFormatter(final Format formatter) {
		this.formatter = formatter;
	}

	public void setHumanName(final String humanName) {
		this.humanName = humanName;
	}

	public void setIndex(final int index) {
		this.index = index;
	}

	public void setMaxLength(final int maxLength) {
		this.maxLength = maxLength;
	}

	public void setName(final String name) {
		this.name = name;
		setHumanName(Lables.get(name, true));
	}

	public void setPreferredWidth(final int preferredWidth) {
		this.preferredWidth = preferredWidth;
	}

	public void setRenderer(final TableCellRenderer cellRenderer) {
		this.cellRenderer = cellRenderer;
	}

	public void setRequired(final boolean required) {
		this.required = required;
	}

	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

	public void setVisibleIndex(final int visibleIndex) {
		this.visibleIndex = visibleIndex;
	}

}