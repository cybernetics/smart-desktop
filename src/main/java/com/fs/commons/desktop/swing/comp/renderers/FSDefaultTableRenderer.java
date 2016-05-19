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

import java.awt.Color;
import java.awt.Component;
import java.sql.Time;
import java.text.Format;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.desktop.swing.comp.model.FSTableModel;
import com.fs.commons.util.FormatUtil;

public class FSDefaultTableRenderer extends DefaultTableCellRenderer {
	/**
	 *
	 */
	private static final long serialVersionUID = 3671580631267035219L;
//	private static final Color SELECTED_ROW_COLOR = new Color(150, 150, 255);
//	private static final Color ODD_ROW_COLOR = new Color(255, 255, 255);
//	private static final Color EVEN_ROW_COLOR = Colors.TABLE_EVEN_ROW;
	private Format format;

	// private Font font=new Font("arial",Font.PLAIN,11);

	// //////////////////////////////////////////////////////////////////////
	public Format getFormat() {
		return this.format;
	}

	@Override
	public Component getTableCellRendererComponent(final JTable table, Object value, final boolean isSelected, final boolean hasFocus, final int row,
			final int column) {
		// setFont(font);
		setComponentOrientation(table.getComponentOrientation());
		Format format = this.format;
		// the above statement is very imporant to avoid caching the last column
		// format
		if (format == null) {
			if (table.getModel() instanceof FSTableModel) {
				final FSTableModel model = (FSTableModel) table.getModel();
				format = model.getFormatter(column);
			}
		}
		if (format != null && value != null) {
			try {
				value = format.format(value);
			} catch (final Exception e) {
				System.out.println("failed to format col:" + column + " with value :" + value + "  with class:" + value.getClass().getName()
						+ "  with formatter : " + format.getClass().getName());
			}
		}
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//		if (!isSelected) {
//			setBackground(row % 2 == 0 ? EVEN_ROW_COLOR : ODD_ROW_COLOR);
//		} else {
//			setBackground(SELECTED_ROW_COLOR);
//		}

		final Class columnClass = table.getModel().getColumnClass(column);
		// Modify this to use the setColumnFormat in FSTable
		// System.out.println("value "+value+" of class
		// :"+(value==null?null:value.getClass().getName()));
		if (Time.class.isAssignableFrom(columnClass) && value instanceof Time) {
			final Time value2 = (Time) value;
			setText(FormatUtil.formatTime(value2));
		} else if (Date.class.isAssignableFrom(columnClass) && value instanceof Date) {
			final Date value2 = (Date) value;
			setText(FormatUtil.formatShortDate(value2));
		}
		// solve the duplicates of this in all the renderes
		if (isSelected && table.getSelectedColumn() == column) {
			setBorder(BorderFactory.createLineBorder(Color.blue));
		} else {
			setBorder(BorderFactory.createLineBorder(Colors.MAIN_PANEL_BG));
		}
		return this;
	}

	// //////////////////////////////////////////////////////////////////////
	public void setFormat(final Format format) {
		this.format = format;
	}

}
