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
package com.fs.commons.desktop.swing.comp.editors;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;

public class FSBindingComponentEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = -885818003283810185L;
	private final BindingComponent component;
	private JTable table;

	public FSBindingComponentEditor(final BindingComponent component) {
		this.component = component;
		component.setAutoTransferFocus(false);
		this.component.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(final FocusEvent e) {
				stopCellEditing();
			}
		});
		/**
		 * This will be usedfull when we change the value from outside the table
		 */
		component.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChanged(final Object oldValue, final Object newValue) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (FSBindingComponentEditor.this.table != null) {
							if (newValue != component.getValue()) {
								final int col = FSBindingComponentEditor.this.table.getSelectedColumn();
								final int row = FSBindingComponentEditor.this.table.getSelectedRow();
								if (col != -1 && row != -1) {
									FSBindingComponentEditor.this.table.setValueAt(newValue, row, col);
								}
							}
						}
					}
				});
			}
		});
		// component.addKeyListener(new KeyAdapter() {
		// @Override
		// public void keyPressed(KeyEvent e) {
		// if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		// e.consume();
		// // to avoid losing focus by default on FSComponents
		// }
		// }
		// });
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
	}

	@Override
	public boolean equals(final Object obj) {
		return obj == this.component;
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
		this.table = table;
		this.component.setValue(value);
		if (this.component instanceof JKTextField) {
			final JKTextField txt = (JKTextField) this.component;
			txt.setType(table.getColumnClass(column));
			txt.setHorizontalAlignment(SwingConstants.LEADING);
			txt.selectAll();
		}
		if (isSelected && table.getSelectedColumn() == column) {
			this.component.setBorder(BorderFactory.createLineBorder(Color.blue));
		} else {
			this.component.setBorder(BorderFactory.createLineBorder(Colors.MAIN_PANEL_BG));
		}
		// System.out.println("Prepapring Editor at : "+row+" , "+column+" , "+
		// component.getClass().getName());
		return (Component) this.component;
	}

	public void setValue(final Object value) {
		this.component.setValue(value);
	}

	@Override
	public boolean stopCellEditing() {
		// TODO Auto-generated method stub
		return super.stopCellEditing();
	}
}