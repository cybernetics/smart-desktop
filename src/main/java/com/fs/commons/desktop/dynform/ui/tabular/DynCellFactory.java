package com.fs.commons.desktop.dynform.ui.tabular;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.dynform.ui.ComponentFactory;
import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.desktop.swing.comp.JKTable;
import com.fs.commons.util.ExceptionUtil;

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
		public DynFieldEditor(FieldMeta field) {
			try {
				component = (JComponent) ComponentFactory.createComponent(field,true);
				component.setPreferredSize(null);								
			} catch (DaoException e) {
				ExceptionUtil.handleException(e);
			}
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			((BindingComponent<Object>) component).setValue(value);
			if (isSelected) {
				component.setBackground(Colors.CELL_EDITOR_BG);
			} else {
				component.setBackground(Colors.TABLE_ODD_ROW);
			}
			return component;
		}

		@Override
		public Object getCellEditorValue() {
			return ((BindingComponent<?>) component).getValue();
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			((BindingComponent<Object>) component).setValue(value);
			if (isSelected) {
				component.setBackground(Colors.JK_LABEL_BG);
			} else {
				component.setBackground(Colors.TABLE_ODD_ROW);
			}

			if (hasFocus ){
				JKTable jkTable = (JKTable) table;
				if(jkTable.isEditable(column)) {
					table.editCellAt(row, column);
				}else{
					jkTable.transferFocusToNextColunm();
				}				
			}
			return component;
		}

	}

}
