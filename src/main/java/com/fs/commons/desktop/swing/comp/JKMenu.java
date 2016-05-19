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
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.util.GeneralUtility;

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
public class JKMenu extends JKButton {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// static Color bgColor=new Color(206,222,239);
	static Color forColor = Colors.MENU_BUTTON_FC;// new Color(22, 125, 219);

	// static Color backColor = new Color(191, 215, 255);
	static Color backColor = Colors.MENU_BUTTON_BG;// new Color(229, 238, 255);

	// static Color selectedBackColor = new Color(229, 238, 255);

	// static Border FOCUS_BORDER =
	// BorderFactory.createLineBorder(Color.orange);

	// Border DEFAULT_BORDER = getBorder();

	// static Color selectedBackColor = new Color(255, 211, 73);

	static Dimension dim = new Dimension(180, 40);

	public JKMenu(final String str) {
		super(str);
		init();

		// setSelectedBGColor(selectedBackColor);
		// setFont(new Font("Tahoma", Font.PLAIN, 12));
	}

//	@Override
	void init() {
		super.init();
		setFocusable(false);
		setBackground(backColor);
		setForeground(forColor);
		setPreferredSize(dim);
		// setBackground(bgColor);

	}

	@Override
	public void setSelected(final boolean selected) {
		super.setSelected(selected);
		if (selected) {
			setForeground(Color.black);
			// setBorder(FOCUS_BORDER);
		} else {
			setForeground(forColor);
			// setBorder(DEFAULT_BORDER);
		}
	}

	public void setShortcutText(String string, boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void setIcon(String iconName) {
		setIcon(GeneralUtility.getIcon(iconName));
	}

}
