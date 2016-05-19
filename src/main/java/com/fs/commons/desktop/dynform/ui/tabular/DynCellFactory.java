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
package com.fs.commons.desktop.dynform.ui.tabular;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.desktop.dynform.ui.ComponentFactory;
import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.desktop.swing.comp.JKTable;
import com.jk.exceptions.handler.ExceptionUtil;

public class DynCellFactory {
	static class DynFieldEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		private JComponent component;

		/**
		 *
		 * @param field
		 */
		public DynFieldEditor(final FieldMeta field) {
			try {
				this.component = (JComponent) ComponentFactory.createComponent(field, true);
				this.component.setPreferredSize(null);
			} catch (final JKDataAccessException e) {
				ExceptionUtil.handle(e);
			}
		}

		@Override
		public Object getCellEditorValue() {
			return ((BindingComponent<?>) this.component).getValue();
		}

		@Override
		public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected, final int row,
				final int column) {
			((BindingComponent<Object>) this.component).setValue(value);
			if (isSelected) {
				this.component.setBackground(Colors.CELL_EDITOR_BG);
			} else {
				this.component.setBackground(Colors.TABLE_ODD_ROW);
			}
			return this.component;
		}

		@Override
		public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus,
				final int row, final int column) {
			((BindingComponent<Object>) this.component).setValue(value);
			if (isSelected) {
				this.component.setBackground(Colors.JK_LABEL_BG);
			} else {
				this.component.setBackground(Colors.TABLE_ODD_ROW);
			}

			if (hasFocus) {
				final JKTable jkTable = (JKTable) table;
				if (jkTable.isEditable(column)) {
					table.editCellAt(row, column);
				} else {
					jkTable.transferFocusToNextColunm();
				}
			}
			return this.component;
		}

	}

}
