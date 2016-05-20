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

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.locale.Lables;

/**
 * <p>
 * Title:
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 *
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class JKMenuItem extends JKButton {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	static Dimension size = new Dimension(180, 50);

	// static Color bgColor = new Color(206, 222, 239);
	static Color backColor = Colors.MI_BUTTON_BG;// new Color(229, 238, 255);
	// static Font font=new java.awt.Font("tahoma",Font.BOLD,11);

	// static Color selectedBackColor = new Color(255, 211, 73);
	// static Color selectedBackColor = new Color(255, 211, 73);

	static Color forColor = Colors.MI_BUTTON_FC;// new Color(22, 125, 219);

	// static Border selectedBorder=BorderFactory.createLoweredBevelBorder();
	// static Border notSelectedBorder=BorderFactory.createEtchedBorder();

	// static final int lineCharCounts=10;

	public JKMenuItem(final String str) {
		super(Lables.get(str));

		init();
		setFocusable(false);
		setBackground(backColor);
		setForeground(forColor);
		setPreferredSize(size);
		setHorizontalAlignment(LEADING);
		// setFont(font);	
	

	}
	//
	// /**
	// *
	// */
	// @Override
	// void init() {
	// super.init();
	// // setBackground(bgColor);
	// // setBorder(notSelectedBorder);
	// }

}
