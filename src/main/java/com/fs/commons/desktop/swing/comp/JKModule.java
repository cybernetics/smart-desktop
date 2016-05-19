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
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.SwingConstants;

import com.fs.commons.desktop.swing.Colors;

public class JKModule extends JKButton {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	static Color forColor = Colors.MODULE_BUTTON_FC;

	static Color backColor = Colors.MODULE_BUTTON_BG;

	// static Color selectedBackColor = new Color(22, 125, 219);

	// static Border FOCUS_BORDER =
	// BorderFactory.createLineBorder(Color.orange);

	// Border DEFAULT_BORDER = getBorder();

	static Dimension dim = new Dimension(180, 40);

	public JKModule(final String str) {
		super(str);
		init();
	}

	@Override
	void init() {
		super.init();
		setFocusable(false);
		setHorizontalAlignment(CENTER);
		setBackground(backColor);
		setForeground(forColor);

		setPreferredSize(dim);
		// setFont(new Font(getFontName(),Font.BOLD,18));
		// addMouseMotionListener(new MouseMotionAdapter() {
		//
		// @Override
		// public void mouseMoved(MouseEvent e) {
		// }
		// });
		// }

		// @Override
		// public void setSelected(boolean selected) {
		// super.setSelected(selected);
		// if (selected) {
		// setForeground(Color.black);
		// // setBorder(FOCUS_BORDER);
		// } else {
		// setForeground(forColor);
		// // setBorder(DEFAULT_BORDER);
		// }
	}

	@Override
	public void setIcon(final Icon defaultIcon) {
		setVerticalTextPosition(SwingConstants.BOTTOM);
		setHorizontalTextPosition(SwingConstants.CENTER);
	}
}
