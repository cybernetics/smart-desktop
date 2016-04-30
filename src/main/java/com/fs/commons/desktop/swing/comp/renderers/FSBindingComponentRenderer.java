package com.fs.commons.desktop.swing.comp.renderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.desktop.swing.Colors;

public class FSBindingComponentRenderer extends DefaultTableCellRenderer {

	private final BindingComponent component;

	public FSBindingComponentRenderer(BindingComponent component) {
		this.component = component;
		// The below call will be very usefull to hide some compponents in some
		// FS components like FieldFilterWithPanel
		component.setEnabled(false);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		component.setValue(value);
		component.requestFocus();
		if(isSelected && table.getSelectedColumn()==column){
			component.setBorder(BorderFactory.createLineBorder(Color.blue));
		} else {
			component.setBorder(BorderFactory.createLineBorder(Colors.MAIN_PANEL_BG));
		}
		return (Component) component;
	}
}
