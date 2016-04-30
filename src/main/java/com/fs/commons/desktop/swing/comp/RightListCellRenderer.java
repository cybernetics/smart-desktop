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
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import com.fs.commons.desktop.swing.SwingUtility;

public class RightListCellRenderer extends BasicComboBoxRenderer {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public RightListCellRenderer() {
		SwingUtility.setFont(this);
	}

	@Override
	public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected,
			final boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (!SwingUtility.isLeftOrientation()) {
			setHorizontalAlignment(JLabel.RIGHT);
		}
		return this;
	}
}
