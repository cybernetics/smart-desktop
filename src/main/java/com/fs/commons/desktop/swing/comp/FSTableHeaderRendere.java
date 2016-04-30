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

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import com.fs.commons.desktop.swing.Colors;
import com.lowagie.text.Font;

public class FSTableHeaderRendere extends JLabel implements TableCellRenderer {
	/**
	 *
	 */
	private static final long serialVersionUID = -3307450163008360355L;

	public FSTableHeaderRendere() {
		init();
	}

	@Override
	public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus,
			final int row, final int column) {
		setText(value.toString());
		return this;
	}

	private void init() {
		setBackground(Colors.JK_LABEL_BG);
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setBorder(UIManager.getBorder("TitledBorder.border"));
		setFont(getFont().deriveFont(Font.BOLD));
	}

}
