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
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;

public class FSBindingComponentEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

	private final BindingComponent component;
	private JTable table;

	public FSBindingComponentEditor(final BindingComponent component) {
		this.component = component;
		component.setAutoTransferFocus(false);
		this.component.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				stopCellEditing();
			}
		});
		/**
		 * This will be usedfull when we change the value from outside the table
		 */
		component.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChanged(Object oldValue, final Object newValue) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if (table != null) {
							if (newValue != component.getValue()) {
								int col = table.getSelectedColumn();
								int row = table.getSelectedRow();
								if (col != -1 && row != -1) {
									table.setValueAt(newValue, row, col);
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

	// Implement the one CellEditor method that AbstractCellEditor doesn't.
	public Object getCellEditorValue() {
		Object value = component.getValue();
		return value;
	}

	// Implement the one method defined by TableCellEditor.
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.table = table;
		component.setValue(value);
		if (component instanceof JKTextField) {
			JKTextField txt = (JKTextField) component;
			txt.setType(table.getColumnClass(column));
			txt.setHorizontalAlignment(txt.LEADING);
			txt.selectAll();
		}
		if (isSelected && table.getSelectedColumn() == column) {
			component.setBorder(BorderFactory.createLineBorder(Color.blue));
		} else {
			component.setBorder(BorderFactory.createLineBorder(Colors.MAIN_PANEL_BG));
		}
		// System.out.println("Prepapring Editor at : "+row+" , "+column+" , "+
		// component.getClass().getName());
		return (Component) component;
	}

	public BindingComponent getComponent() {
		return component;
	}

	public void setValue(Object value) {
		component.setValue(value);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	@Override
	public boolean equals(Object obj) {
		return obj == component;
	}

	@Override
	public boolean stopCellEditing() {
		// TODO Auto-generated method stub
		return super.stopCellEditing();
	}
}