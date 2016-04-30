package com.fs.commons.desktop.swing.comp.renderers;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.desktop.swing.comp.JKTextField;

public class JKBindingComponentEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

	private final BindingComponent component;


	public JKBindingComponentEditor(BindingComponent component) {
		this.component = component;
	}


	// Implement the one CellEditor method that AbstractCellEditor doesn't.
	public Object getCellEditorValue() {
		Object value = component.getValue();
		return value;
	}

	// Implement the one method defined by TableCellEditor.
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		component.setValue(value);
		if(component instanceof JKTextField){
			JKTextField txt=(JKTextField) component;
			txt.setType(table.getColumnClass(column));
			txt.setHorizontalAlignment(txt.LEADING);		
			txt.selectAll();
		}
//		System.out.println(row+" , "+column+" , "+ component.getClass().getName());
		return (Component) component;
	}
	
	public BindingComponent getComponent() {
		return component;
	}
	
	public void setValue(Object value){
		component.setValue(value);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
	}
}