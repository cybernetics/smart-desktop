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

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.desktop.swing.Colors;

public class FSBindingComponentRenderer extends DefaultTableCellRenderer {

	/**
	 *
	 */
	private static final long serialVersionUID = 4706126381880677529L;
	private final BindingComponent component;

	public FSBindingComponentRenderer(final BindingComponent component) {
		this.component = component;
		// The below call will be very usefull to hide some compponents in some
		// FS components like FieldFilterWithPanel
		component.setEnabled(false);
	}

	@Override
	public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus,
			final int row, final int column) {
		this.component.setValue(value);
		this.component.requestFocus();
		if (isSelected && table.getSelectedColumn() == column) {
			this.component.setBorder(BorderFactory.createLineBorder(Color.blue));
		} else {
			this.component.setBorder(BorderFactory.createLineBorder(Colors.MAIN_PANEL_BG));
		}
		return (Component) this.component;
	}
}
