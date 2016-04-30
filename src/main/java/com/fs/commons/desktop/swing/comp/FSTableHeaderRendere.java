package com.fs.commons.desktop.swing.comp;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import com.fs.commons.desktop.swing.Colors;
import com.lowagie.text.Font;

public class FSTableHeaderRendere extends JLabel implements TableCellRenderer {
	public FSTableHeaderRendere() {
		init();
	}

	private void init() {
		setBackground(Colors.JK_LABEL_BG);
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setBorder(UIManager.getBorder("TitledBorder.border"));
		setFont(getFont().deriveFont(Font.BOLD));
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		setText(value.toString());
		return this;
	}

}
