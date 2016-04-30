package com.fs.commons.desktop.swing.comp;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.desktop.swing.SwingUtility;

public class TableRenderers {
	static Border gap = BorderFactory.createEmptyBorder(3, 3, 3, 3);

	public static class CustomDataRenderer extends DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		Color evenRowColor = Colors.TABLE_EVEN_ROW;// new Color(238, 246, 255);
		// // blue

		Color oddRowColor = Colors.TABLE_ODD_ROW;// new Color(255, 255, 255); //

		// gray

		public CustomDataRenderer() {
			setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		}

		/**
		 * 
		 */
		public Component getTableCellRendererComponent(JTable jt, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component comp = super.getTableCellRendererComponent(jt, value, isSelected, hasFocus, row, column);
			JLabel label = (JLabel) comp;
			if (!isSelected) {
				label.setBackground(row % 2 == 0 ? evenRowColor : oddRowColor);
				label.setForeground(Color.black);
			}
			JKTable table = (JKTable) jt;
			if (table.isEditable()) {
				if (table.isEditable(column)) {
					setBackground(Colors.CELL_EDITOR_BG);
					setForeground(Colors.CELL_EDTIOR_FG);
					setBorder(BorderFactory.createLoweredBevelBorder());
					if (hasFocus) {
						table.editCellAt(row, column);
					}
				} else if (hasFocus) {
					SwingUtility.pressKey(KeyEvent.VK_TAB);
				}
			}
			setAlingment(value);
			return label;
		}

		/**
		 * TODO : this methd has big over head , refacor
		 * 
		 * @param value
		 */
		public void setAlingment(Object value) {
			try {
				if (value == null || value.equals("-") || value.toString().contains("%")) {
					setHorizontalAlignment(JKLabel.CENTER);
				} else {
					Double.parseDouble(value.toString());
				}
				setHorizontalAlignment(JKLabel.CENTER);
			} catch (NumberFormatException e) {
				setHorizontalAlignment(JKLabel.LEADING);
				setBorder(gap);// padding
			}
		}
	}


}
// /////////////////////////////////////////////////////////////////////////////
