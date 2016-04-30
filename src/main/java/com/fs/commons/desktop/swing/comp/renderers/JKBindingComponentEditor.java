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
package com.fs.commons.desktop.swing.comp.renderers;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.desktop.swing.comp.JKTextField;

public class JKBindingComponentEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = -8281451184713845624L;
	private final BindingComponent component;

	public JKBindingComponentEditor(final BindingComponent component) {
		this.component = component;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
	}

	// Implement the one CellEditor method that AbstractCellEditor doesn't.
	@Override
	public Object getCellEditorValue() {
		final Object value = this.component.getValue();
		return value;
	}

	public BindingComponent getComponent() {
		return this.component;
	}

	// Implement the one method defined by TableCellEditor.
	@Override
	public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected, final int row, final int column) {
		this.component.setValue(value);
		if (this.component instanceof JKTextField) {
			final JKTextField txt = (JKTextField) this.component;
			txt.setType(table.getColumnClass(column));
			txt.setHorizontalAlignment(SwingConstants.LEADING);
			txt.selectAll();
		}
		// System.out.println(row+" , "+column+" , "+
		// component.getClass().getName());
		return (Component) this.component;
	}

	public void setValue(final Object value) {
		this.component.setValue(value);
	}
}