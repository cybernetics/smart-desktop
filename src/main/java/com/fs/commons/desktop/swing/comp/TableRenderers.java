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
		@Override
		public Component getTableCellRendererComponent(final JTable jt, final Object value, final boolean isSelected, final boolean hasFocus,
				final int row, final int column) {
			final Component comp = super.getTableCellRendererComponent(jt, value, isSelected, hasFocus, row, column);
			final JLabel label = (JLabel) comp;
			if (!isSelected) {
				label.setBackground(row % 2 == 0 ? this.evenRowColor : this.oddRowColor);
				label.setForeground(Color.black);
			}
			final JKTable table = (JKTable) jt;
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
		public void setAlingment(final Object value) {
			try {
				if (value == null || value.equals("-") || value.toString().contains("%")) {
					setHorizontalAlignment(JKLabel.CENTER);
				} else {
					Double.parseDouble(value.toString());
				}
				setHorizontalAlignment(JKLabel.CENTER);
			} catch (final NumberFormatException e) {
				setHorizontalAlignment(JKLabel.LEADING);
				setBorder(gap);// padding
			}
		}
	}

	static Border gap = BorderFactory.createEmptyBorder(3, 3, 3, 3);

}
// /////////////////////////////////////////////////////////////////////////////
